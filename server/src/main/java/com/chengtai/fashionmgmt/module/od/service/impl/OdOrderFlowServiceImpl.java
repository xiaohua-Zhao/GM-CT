package com.chengtai.fashionmgmt.module.od.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.BusinessValidationException;
import com.chengtai.fashionmgmt.common.BusinessValueUtils;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.ct.entity.CtStaff;
import com.chengtai.fashionmgmt.module.ct.mapper.CtStaffMapper;
import com.chengtai.fashionmgmt.module.fin.entity.FinPlan;
import com.chengtai.fashionmgmt.module.fin.mapper.FinPlanMapper;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderCloseRequest;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderCostPreview;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderDetailResponse;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderMetrics;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderProgressNode;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderReviewRequest;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderSaveRequest;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import com.chengtai.fashionmgmt.module.od.entity.OdOrderItem;
import com.chengtai.fashionmgmt.module.od.mapper.OdOrderItemMapper;
import com.chengtai.fashionmgmt.module.od.mapper.OdOrderMapper;
import com.chengtai.fashionmgmt.module.od.service.OdOrderFlowService;
import com.chengtai.fashionmgmt.module.sys.entity.SysCommon;
import com.chengtai.fashionmgmt.module.sys.mapper.SysCommonMapper;
import com.chengtai.fashionmgmt.module.sm.entity.SmSample;
import com.chengtai.fashionmgmt.module.sm.mapper.SmSampleMapper;
import com.chengtai.fashionmgmt.module.pl.entity.PlProdPlan;
import com.chengtai.fashionmgmt.module.pl.entity.PlTask;
import com.chengtai.fashionmgmt.module.pl.mapper.PlProdPlanMapper;
import com.chengtai.fashionmgmt.module.pl.mapper.PlTaskMapper;
import com.chengtai.fashionmgmt.module.sys.entity.SysProcess;
import com.chengtai.fashionmgmt.module.sys.mapper.SysProcessMapper;
import com.chengtai.fashionmgmt.module.sys.service.CodeGeneratorService;
import com.chengtai.fashionmgmt.module.od.dto.OdOrderAuditRequest;
import com.chengtai.fashionmgmt.module.od.dto.OrderCreateSamplesRequest;
import com.chengtai.fashionmgmt.module.od.dto.OrderCreateProductionPlanRequest;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** 货物订单流程服务实现。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OdOrderFlowServiceImpl implements OdOrderFlowService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int STATUS_INITIAL = 1;
    private static final int STATUS_WAIT_SAMPLE = 2;
    private static final int STATUS_PRODUCING = 3;
    private static final int STATUS_PART_DELIVERED = 4;
    private static final int STATUS_DONE = 5;
    private static final int STATUS_DELAYED = 6;
    private static final int STATUS_PAUSED = 7;
    private static final int STATUS_CLOSED = 8;
    private static final int STATUS_REAUDIT = 9;
    private static final int STATUS_SAMPLE_GENERATING = 10;
    private static final int STATUS_WAIT_PRODUCING = 11;

    private final OdOrderMapper orderMapper;
    private final OdOrderItemMapper itemMapper;
    private final FinPlanMapper planMapper;
    private final SysCommonMapper commonMapper;
    private final CtStaffMapper staffMapper;
    private final CodeGeneratorService codeGeneratorService;
    private final BusinessLogJson logJson;
    private final SmSampleMapper sampleMapper;
    private final PlProdPlanMapper prodPlanMapper;
    private final PlTaskMapper taskMapper;
    private final SysProcessMapper processMapper;

    /** 分页查询订单，并补充毛利额和毛利率。 */
    @Override
    public IPage<OdOrder> listOrders(long current, long size, String keyword, Long unitId, Long salesmanId,
                                     Integer status, String deliveryStart, String deliveryEnd, Long deptId) {
        QueryWrapper<OdOrder> query = new QueryWrapper<>();
        query.eq("is_del", 0);
        if (StringUtils.hasText(keyword)) {
            String value = keyword.trim();
            query.and(wrapper -> wrapper.like("order_no", value)
                    .or().like("contract_no", value)
                    .or().apply("unit_id IN (SELECT unit_id FROM ct_gm.t_sys_unit WHERE unit_name LIKE CONCAT('%', {0}, '%') AND is_del=0)", value));
        }
        query.eq(unitId != null, "unit_id", unitId)
                .eq(salesmanId != null, "salesman_id", salesmanId)
                .eq(status != null, "status", status)
                .eq(deptId != null, "dept_id", deptId)
                .ge(StringUtils.hasText(deliveryStart), "delivery_date_plan", deliveryStart)
                .le(StringUtils.hasText(deliveryEnd), "delivery_date_plan", deliveryEnd)
                .orderByDesc("created_at");
        IPage<OdOrder> page = orderMapper.selectPage(new Page<>(current, size), query);
        page.getRecords().forEach(this::fillMetrics);
        return page;
    }

    /** 查询订单详情、明细、财务收款节点、毛利指标和流程进度。 */
    @Override
    public OdOrderDetailResponse detail(Long orderId) {
        OdOrder order = requireOrder(orderId);
        List<OdOrderItem> items = itemMapper.selectList(new QueryWrapper<OdOrderItem>()
                .eq("order_id", orderId).eq("is_del", 0).orderByAsc("item_no"));
        List<FinPlan> plans = planMapper.selectList(new QueryWrapper<FinPlan>()
                .eq("source_type", 1).eq("source_id", orderId).eq("is_del", 0)
                .orderByAsc("plan_date", "seq_no"));
        CostBreakdown cost = calculateCost(orderId);
        order.setCostPlan(cost.total());
        order.setMaterialCost(cost.material());
        order.setLaborCost(cost.labor());
        OdOrderMetrics metrics = calculateMetrics(order);
        order.setGrossProfit(metrics.getGrossProfit());
        order.setGrossMarginRate(metrics.getGrossMarginRate());
        order.setEditable(isOrderEditable(order));
        List<SmSample> samples = sampleMapper.selectList(new QueryWrapper<SmSample>()
                .eq("order_id", orderId).eq("is_del", 0).orderByAsc("created_at"));
        List<PlProdPlan> productionPlans = prodPlanMapper.selectList(new QueryWrapper<PlProdPlan>()
                .eq("order_id", orderId).eq("is_del", 0).orderByDesc("created_at"));
        return new OdOrderDetailResponse(order, items, plans, metrics, buildProgress(order), samples, productionPlans);
    }

    /** 按明细款式和数量预览 BOM 物料成本及工序用工成本。 */
    @Override
    public OdOrderCostPreview previewCost(List<OdOrderItem> items) {
        CostBreakdown cost = calculatePreviewCost(items);
        return new OdOrderCostPreview(cost.material(), cost.labor(), cost.total());
    }

    /** 审核订单：通过进入已审核，不通过进入待复议。 */
    @Override
    @Transactional
    public OdOrder audit(Long orderId, OdOrderAuditRequest request) {
        OdOrder order = requireOrder(orderId);
        if (!Objects.equals(order.getStatus(), STATUS_INITIAL) && !Objects.equals(order.getStatus(), STATUS_REAUDIT)) {
            throw new BusinessValidationException("只有初始或待复议状态订单可以审核");
        }
        boolean pass = "pass".equalsIgnoreCase(request.getAuditResult());
        boolean reject = "reject".equalsIgnoreCase(request.getAuditResult());
        if (!pass && !reject) throw new BusinessValidationException("审核结果只能为 pass 或 reject");
        String now = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        Long userId = currentUserId();
        if (pass) {
            orderMapper.updateAuditPassed(orderId, userId, now, request.getAuditRemark());
            orderMapper.insertStatusLog(orderId, order.getDeptId(), order.getStatus(), STATUS_WAIT_SAMPLE, userId, "订单审核通过");
        } else {
            orderMapper.updateAuditRejected(orderId, userId, now, request.getAuditRemark());
            orderMapper.insertStatusLog(orderId, order.getDeptId(), order.getStatus(), STATUS_REAUDIT, userId, "订单审核不通过，待复议");
        }
        return requireOrder(orderId);
    }

    /** 从勾选的订单明细批量生成样衣，并将订单推进到样衣生成中。 */
    @Override
    @Transactional
    public List<SmSample> createSamples(Long orderId, OrderCreateSamplesRequest request) {
        OdOrder order = requireOrder(orderId);
        if (!Objects.equals(order.getStatus(), STATUS_WAIT_SAMPLE)) {
            throw new BusinessValidationException("只有已审核订单可以生成样衣");
        }
        List<Long> ids = request == null ? null : request.getSelectedItemIds();
        if (ids == null || ids.isEmpty()) throw new BusinessValidationException("请至少选择一条订单明细");
        List<OdOrderItem> selected = itemMapper.selectList(new QueryWrapper<OdOrderItem>()
                .eq("order_id", orderId).in("order_item_id", ids).eq("is_del", 0));
        if (selected.size() != ids.size()) throw new BusinessValidationException("部分订单明细不存在或不属于当前订单");
        for (OdOrderItem item : selected) {
            SmSample sample = new SmSample();
            sample.setDeptId(order.getDeptId());
            sample.setSampleNo(codeGeneratorService.generate("SAMPLE").getPreviewCode());
            sample.setOrderId(orderId);
            sample.setStyleId(item.getStyleId());
            sample.setSampleType(1);
            sample.setRoundNo(1);
            sample.setQty(1);
            sample.setConfirmResult(0);
            sample.setStatus(1);
            sampleMapper.insert(sample);
        }
        Long userId = currentUserId();
        orderMapper.updateStatus(orderId, STATUS_SAMPLE_GENERATING);
        orderMapper.insertStatusLog(orderId, order.getDeptId(), STATUS_WAIT_SAMPLE, STATUS_SAMPLE_GENERATING, userId, "已生成样衣 " + selected.size() + " 件");
        return sampleMapper.selectList(new QueryWrapper<SmSample>().eq("order_id", orderId).eq("is_del", 0).orderByAsc("created_at"));
    }

    /** 为待生产订单创建生产计划及对应工序任务。 */
    @Override
    @Transactional
    public PlProdPlan createProductionPlan(Long orderId, OrderCreateProductionPlanRequest request) {
        OdOrder order = requireOrder(orderId);
        if (!Objects.equals(order.getStatus(), STATUS_WAIT_PRODUCING)) {
            throw new BusinessValidationException("只有待生产订单可以创建生产计划");
        }
        if (prodPlanMapper.countByOrderId(orderId, null) > 0) throw new BusinessValidationException("该订单已经存在生产计划");
        SysProcess process = processMapper.selectById(request.getProcessId());
        if (process == null) throw new BusinessValidationException("工序不存在");
        int stage = process.getProcessType() == null ? 0 : process.getProcessType();
        if (stage < 1 || stage > 5) throw new BusinessValidationException("生产计划只能选择裁剪、缝制、剪线、整烫或包装工序");
        BigDecimal planQty = request.getPlanQty() == null ? order.getQtyTotal() : request.getPlanQty();
        if (planQty == null || planQty.signum() < 0) throw new BusinessValidationException("计划数量不能小于0");
        String today = LocalDate.now().toString();
        String start = StringUtils.hasText(order.getStartDate()) ? order.getStartDate() : today;
        String end = order.getDeliveryDatePlan();
        Long userId = currentUserId();
        String now = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        PlProdPlan plan = new PlProdPlan();
        plan.setDeptId(order.getDeptId());
        plan.setProdPlanNo(codeGeneratorService.generate("PROD_PLAN").getPreviewCode());
        plan.setOrderId(orderId);
        plan.setProdQty(planQty);
        plan.setStartDate(start);
        plan.setEndDate(end);
        plan.setStatus(2);
        plan.setPlannerId(userId);
        plan.setReleaseBy(userId);
        plan.setReleaseTime(now);
        plan.setRemark("由订单 " + order.getOrderNo() + " 创建");
        prodPlanMapper.insertBusiness(plan, userId);
        PlTask task = new PlTask();
        task.setProdPlanId(plan.getId());
        task.setTaskNo(codeGeneratorService.generate("PROD_TASK").getPreviewCode());
        task.setSeqNo(1);
        task.setProcessStage(stage);
        task.setDeptId(order.getDeptId());
        task.setStatus(1);
        task.setQtyPlan(planQty);
        task.setQtyDone(BigDecimal.ZERO);
        task.setStartDatePlan(start);
        task.setEndDatePlan(end);
        task.setQcNeed(1);
        task.setQcStatus(0);
        task.setQcType(2);
        task.setQcQtyCheck(BigDecimal.ZERO);
        task.setQcQtyPass(BigDecimal.ZERO);
        task.setQcQtyReject(BigDecimal.ZERO);
        taskMapper.insertBusiness(task, userId);
        orderMapper.updateStatus(orderId, STATUS_PRODUCING);
        orderMapper.insertStatusLog(orderId, order.getDeptId(), STATUS_WAIT_PRODUCING, STATUS_PRODUCING, userId, "生产计划已创建：" + plan.getProdPlanNo());
        return plan;
    }

    /** 事务保存订单头及明细，自动重算汇总、毛利和行号。 */
    @Override
    @Transactional
    public OdOrder saveOrder(OdOrderSaveRequest request, Long orderId) {
        log.info("[OD_ORDER_FLOW][SAVE][IN] orderId={} request={}", orderId, logJson.toJson(request));
        OdOrder existing = orderId == null ? null : requireOrder(orderId);
        if (existing != null) {
            ensureEditable(existing);
        }
        List<OdOrderItem> requestedItems = request.getItems() == null ? List.of() : request.getItems();
        if (requestedItems.isEmpty()) {
            throw new BusinessValidationException("订单至少需要一条明细");
        }

        Map<Long, OdOrderItem> existingItems = new HashMap<>();
        if (existing != null) {
            itemMapper.selectList(new QueryWrapper<OdOrderItem>()
                    .eq("order_id", orderId).eq("is_del", 0)).forEach(item -> existingItems.put(item.getId(), item));
            itemMapper.offsetItemNos(orderId);
        }
        Set<Long> incomingIds = new HashSet<>();
        for (OdOrderItem item : requestedItems) {
            if (item.getId() != null) incomingIds.add(item.getId());
        }
        for (OdOrderItem oldItem : existingItems.values()) {
            if (!incomingIds.contains(oldItem.getId())) {
                if (BusinessValueUtils.zero(oldItem.getQtyShipped()).signum() > 0) {
                    throw new BusinessValidationException("已发货的订单明细不能删除，行号：" + oldItem.getItemNo());
                }
                itemMapper.logicalDelete(oldItem.getId());
            }
        }

        OdOrder order = existing == null ? new OdOrder() : existing;
        copyEditableFields(request, order);
        normalizeHeader(order);
        if (existing == null) {
            if (!StringUtils.hasText(order.getOrderNo())) {
                order.setOrderNo(codeGeneratorService.generate("ORDER").getPreviewCode());
            }
            order.setStatus(STATUS_INITIAL);
            order.setReviewResult(0);
            order.setIsProducible(0);
            order.setDepositAmount(BigDecimal.ZERO);
            order.setCostAct(BigDecimal.ZERO);
        }

        BigDecimal qtyTotal = BigDecimal.ZERO;
        BigDecimal amountTotal = BigDecimal.ZERO;
        int seq = 1;
        for (OdOrderItem item : requestedItems) {
            OdOrderItem oldItem = item.getId() == null ? null : existingItems.get(item.getId());
            if (item.getId() != null && oldItem == null) {
                throw new BusinessValidationException("订单明细不属于当前订单");
            }
            item.setItemNo(seq++);
            item.setOrderId(order.getId());
            item.setDeptId(item.getDeptId() == null ? order.getDeptId() : item.getDeptId());
            item.setTaxRate(item.getTaxRate() == null ? order.getTaxRate() : item.getTaxRate());
            item.setQtyShipped(oldItem == null ? BigDecimal.ZERO : BusinessValueUtils.zero(oldItem.getQtyShipped()));
            normalizeItem(item);
            qtyTotal = qtyTotal.add(item.getQty());
            amountTotal = amountTotal.add(item.getAmount());
        }
        order.setQtyTotal(qtyTotal);
        order.setAmountTotal(BusinessValueUtils.money(amountTotal));
        order.setAmountTax(BusinessValueUtils.taxIncluded(order.getAmountTotal(), order.getTaxRate()));
        validateHeader(order, existing == null);
        if (mapperCount(order.getOrderNo(), order.getId()) > 0) {
            throw new BusinessValidationException("订单号已存在：" + order.getOrderNo());
        }

        if (existing == null) {
            orderMapper.insertBusiness(order, currentUserId());
        }
        for (OdOrderItem item : requestedItems) {
            item.setOrderId(order.getId());
            if (item.getId() == null) {
                itemMapper.insertBusiness(item, currentUserId());
            } else {
                itemMapper.updateBusiness(item);
            }
        }
        CostBreakdown calculatedCost = calculateCost(order.getId());
        order.setCostPlan(calculatedCost.total());
        order.setMaterialCost(calculatedCost.material());
        order.setLaborCost(calculatedCost.labor());
        orderMapper.updateBusiness(order);
        orderMapper.recalculateTotals(order.getId());
        OdOrder saved = requireOrder(order.getId());
        fillMetrics(saved);
        log.info("[OD_ORDER_FLOW][SAVE][OUT] orderId={} orderNo={} itemCount={} amountTotal={}",
                saved.getId(), saved.getOrderNo(), requestedItems.size(), saved.getAmountTotal());
        return saved;
    }

    /** 提交可做性评审，按后端毛利重新判定并落评审/关闭留痕。 */
    @Override
    @Transactional
    public OdOrder review(Long orderId, OdOrderReviewRequest request) {
        OdOrder order = requireOrder(orderId);
        if (!Objects.equals(order.getStatus(), STATUS_INITIAL)) {
            throw new BusinessValidationException("只有初始状态订单可以提交评审");
        }
        validateOrderItems(orderId);
        CostBreakdown calculatedCost = calculateCost(orderId);
        order.setCostPlan(calculatedCost.total());
        order.setMaterialCost(calculatedCost.material());
        order.setLaborCost(calculatedCost.labor());
        orderMapper.updateBusiness(order);
        OdOrderMetrics metrics = calculateMetrics(order);
        if (!Integer.valueOf(1).equals(request.getReviewResult()) && !Integer.valueOf(2).equals(request.getReviewResult())) {
            throw new BusinessValidationException("评审结论只能选择可做或不可做");
        }
        String now = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        Long userId = currentUserId();
        if (Integer.valueOf(1).equals(request.getReviewResult())) {
            if (!metrics.isViable()) {
                throw new BusinessValidationException("当前毛利不满足可做条件：" + viabilityMessage(metrics));
            }
            orderMapper.updateReviewPassed(orderId, userId, now);
            orderMapper.insertStatusLog(orderId, order.getDeptId(), STATUS_INITIAL, STATUS_WAIT_SAMPLE, userId, "可做性评审通过");
        } else {
            CloseReason reason = normalizeCloseReason(request.getCloseReasonType(), request.getCloseReason(), metrics, true);
            orderMapper.updateReviewRejected(orderId, userId, now, reason.type, reason.text);
            orderMapper.insertStatusLog(orderId, order.getDeptId(), STATUS_INITIAL, STATUS_CLOSED, userId, "可做性评审不通过：" + reason.text);
        }
        return requireOrder(orderId);
    }

    /** 暂停订单并记录状态历史。 */
    @Override
    @Transactional
    public OdOrder pause(Long orderId) {
        OdOrder order = requireOrder(orderId);
        if (!List.of(STATUS_WAIT_SAMPLE, STATUS_PRODUCING, STATUS_PART_DELIVERED, STATUS_SAMPLE_GENERATING, STATUS_WAIT_PRODUCING).contains(order.getStatus())) {
            throw new BusinessValidationException("当前状态不允许暂停");
        }
        Long userId = currentUserId();
        orderMapper.updateStatus(orderId, STATUS_PAUSED);
        orderMapper.insertStatusLog(orderId, order.getDeptId(), order.getStatus(), STATUS_PAUSED, userId, "人工暂停订单");
        return requireOrder(orderId);
    }

    /** 恢复订单到暂停前状态。 */
    @Override
    @Transactional
    public OdOrder resume(Long orderId) {
        OdOrder order = requireOrder(orderId);
        if (!Objects.equals(order.getStatus(), STATUS_PAUSED)) {
            throw new BusinessValidationException("只有暂停状态订单可以恢复");
        }
        Integer previous = orderMapper.selectPreviousStatus(orderId);
        int target = previous == null || previous == STATUS_PAUSED ? STATUS_WAIT_SAMPLE : previous;
        Long userId = currentUserId();
        orderMapper.updateStatus(orderId, target);
        orderMapper.insertStatusLog(orderId, order.getDeptId(), STATUS_PAUSED, target, userId, "恢复订单");
        return requireOrder(orderId);
    }

    /** 按客户原因关闭未投产订单。 */
    @Override
    @Transactional
    public OdOrder close(Long orderId, OdOrderCloseRequest request) {
        OdOrder order = requireOrder(orderId);
        if (Objects.equals(order.getStatus(), STATUS_DONE) || Objects.equals(order.getStatus(), STATUS_CLOSED)) {
            throw new BusinessValidationException("已完成或已关闭订单不能关闭");
        }
        CloseReason reason = normalizeCloseReason(request.getCloseReasonType(), request.getCloseReason(), calculateMetrics(order), false);
        Long userId = currentUserId();
        String now = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        orderMapper.updateClose(orderId, reason.type, reason.text, userId, now);
        orderMapper.insertStatusLog(orderId, order.getDeptId(), order.getStatus(), STATUS_CLOSED, userId, "关闭订单：" + reason.text);
        return requireOrder(orderId);
    }

    /** 校验订单评审通过且样衣已放行，阻止未放行订单排产。 */
    @Override
    public void assertCanCreatePlan(Long orderId) {
        OdOrder order = requireOrder(orderId);
        if (Objects.equals(order.getStatus(), STATUS_CLOSED) || !Integer.valueOf(1).equals(order.getReviewResult())) {
            throw new BusinessValidationException("订单未评审通过或已关闭，不能排产");
        }
        if (!Integer.valueOf(1).equals(order.getIsProducible())) {
            throw new BusinessValidationException("样衣尚未确认，订单未放行，不能排产");
        }
        if (!Objects.equals(order.getStatus(), STATUS_WAIT_PRODUCING)) {
            throw new BusinessValidationException("只有待生产状态订单可以创建生产计划");
        }
    }

    /** 校验订单可编辑状态。 */
    private void ensureEditable(OdOrder order) {
        if (!isOrderEditable(order)) {
            throw new BusinessValidationException("订单已开始生产或已关闭，不允许修改");
        }
    }

    /** 判断订单是否处于生产开始前，可编辑初始、待样衣/待排产及生产前暂停状态。 */
    private boolean isOrderEditable(OdOrder order) {
        if (List.of(STATUS_INITIAL, STATUS_WAIT_SAMPLE, STATUS_REAUDIT, STATUS_SAMPLE_GENERATING, STATUS_WAIT_PRODUCING).contains(order.getStatus())) {
            return true;
        }
        if (Objects.equals(order.getStatus(), STATUS_PAUSED)) {
            Integer previous = orderMapper.selectPreviousStatus(order.getId());
            return previous == null || List.of(STATUS_INITIAL, STATUS_WAIT_SAMPLE, STATUS_REAUDIT, STATUS_SAMPLE_GENERATING, STATUS_WAIT_PRODUCING).contains(previous);
        }
        return false;
    }

    /** 复制允许页面编辑的订单头字段。 */
    private void copyEditableFields(OdOrderSaveRequest source, OdOrder target) {
        target.setUnitId(source.getUnitId());
        target.setOrderType(source.getOrderType());
        target.setContractNo(source.getContractNo());
        target.setDeptId(source.getDeptId());
        target.setSalesmanId(source.getSalesmanId());
        target.setOrderDate(source.getOrderDate());
        target.setStartDate(source.getStartDate());
        target.setDeliveryDatePlan(source.getDeliveryDatePlan());
        target.setPayDatePlan(source.getPayDatePlan());
        target.setCurrency(source.getCurrency());
        target.setTaxRate(source.getTaxRate());
        target.setRemark(source.getRemark());
    }

    /** 补齐订单头默认值和创建人部门。 */
    private void normalizeHeader(OdOrder order) {
        if (order.getOrderType() == null) order.setOrderType(1);
        if (!StringUtils.hasText(order.getCurrency())) order.setCurrency("CNY");
        if (order.getDeptId() == null) {
            LoginUserContext.LoginUser user = LoginUserContext.get();
            if (user != null) {
                CtStaff staff = staffMapper.selectById(user.id());
                if (staff != null) order.setDeptId(staff.getDeptId());
            }
        }
        if (order.getSalesmanId() == null) order.setSalesmanId(currentUserId());
        order.setTaxRate(BusinessValueUtils.taxRate(order.getTaxRate()));
        if (order.getCostPlan() == null) order.setCostPlan(BigDecimal.ZERO);
        if (order.getDepositAmount() == null) order.setDepositAmount(BigDecimal.ZERO);
        if (order.getCostAct() == null) order.setCostAct(BigDecimal.ZERO);
        if (order.getStatus() == null) order.setStatus(STATUS_INITIAL);
        if (order.getDelayDays() == null) order.setDelayDays(0);
    }

    /** 校验订单头必填项、税率和日期顺序。 */
    private void validateHeader(OdOrder order, boolean creating) {
        if (!StringUtils.hasText(order.getOrderNo())) throw new BusinessValidationException("订单号不能为空");
        if (order.getUnitId() == null) throw new BusinessValidationException("请选择客户单位");
        if (!StringUtils.hasText(order.getOrderDate())) throw new BusinessValidationException("请选择下单日期");
        if (!StringUtils.hasText(order.getDeliveryDatePlan())) throw new BusinessValidationException("请选择预计交期");
        if (order.getTaxRate().compareTo(BigDecimal.ZERO) < 0 || order.getTaxRate().compareTo(BigDecimal.ONE) > 0) {
            throw new BusinessValidationException("税率必须在0到1之间");
        }
        if (StringUtils.hasText(order.getStartDate()) && order.getStartDate().compareTo(order.getOrderDate()) < 0) {
            throw new BusinessValidationException("开始日期不能早于下单日期");
        }
        if (StringUtils.hasText(order.getStartDate()) && order.getDeliveryDatePlan().compareTo(order.getStartDate()) < 0) {
            throw new BusinessValidationException("预计交期不能早于开始日期");
        }
        if (order.getDeliveryDatePlan().compareTo(order.getOrderDate()) < 0) {
            throw new BusinessValidationException("预计交期不能早于下单日期");
        }
        if (StringUtils.hasText(order.getDeliveryDateAct()) && order.getDeliveryDateAct().compareTo(order.getDeliveryDatePlan()) < 0) {
            throw new BusinessValidationException("实际交期不能早于预计交期");
        }
    }

    /** 规范化明细金额、税率和数量。 */
    private void normalizeItem(OdOrderItem item) {
        if (item.getStyleId() == null) throw new BusinessValidationException("请选择款式");
        if (!StringUtils.hasText(item.getStyleCode())) throw new BusinessValidationException("款号不能为空");
        item.setQty(BusinessValueUtils.zero(item.getQty()));
        item.setUnitPrice(BusinessValueUtils.zero(item.getUnitPrice()));
        if (item.getQty().signum() <= 0) throw new BusinessValidationException("明细数量必须大于0");
        if (item.getUnitPrice().signum() < 0) throw new BusinessValidationException("明细单价不能小于0");
        item.setAmount(BusinessValueUtils.multiply(item.getQty(), item.getUnitPrice()));
        item.setTaxRate(BusinessValueUtils.taxRate(item.getTaxRate()));
    }

    /** 校验订单明细数量和已发货保护。 */
    private void validateOrderItems(Long orderId) {
        Long count = itemMapper.selectCount(new QueryWrapper<OdOrderItem>().eq("order_id", orderId).eq("is_del", 0));
        if (count == null || count == 0) throw new BusinessValidationException("订单至少需要一条明细");
    }

    /** 计算订单毛利、毛利率和可做性。 */
    private OdOrderMetrics calculateMetrics(OdOrder order) {
        BigDecimal amount = BusinessValueUtils.money(order.getAmountTotal());
        BigDecimal cost = BusinessValueUtils.money(order.getCostPlan());
        BigDecimal gross = amount.subtract(cost);
        BigDecimal rate = amount.signum() == 0 ? BigDecimal.ZERO : gross.divide(amount, 4, RoundingMode.HALF_UP);
        BigDecimal minMargin = loadMinGrossMargin();
        String compareMode = loadConfig("COST_COMPARE_MODE", "EX_TAX");
        BigDecimal compareBase = "IN_TAX".equalsIgnoreCase(compareMode)
                ? BusinessValueUtils.money(order.getAmountTax()) : amount;
        boolean viable = compareBase.signum() > 0 && cost.compareTo(compareBase) < 0 && rate.compareTo(minMargin) >= 0;
        String warning = gross.signum() < 0 ? "LOSS" : gross.signum() == 0 ? "BREAK_EVEN" : "PROFIT";
        return new OdOrderMetrics(gross, rate, compareBase, minMargin, viable, warning);
    }

    /** 将实时毛利写回订单响应对象。 */
    private void fillMetrics(OdOrder order) {
        OdOrderMetrics metrics = calculateMetrics(order);
        order.setGrossProfit(metrics.getGrossProfit());
        order.setGrossMarginRate(metrics.getGrossMarginRate());
        order.setEditable(isOrderEditable(order));
    }

    /** 计算订单正式保存后的成本构成。 */
    private CostBreakdown calculateCost(Long orderId) {
        BigDecimal material = BusinessValueUtils.money(itemMapper.selectMaterialCostByOrderId(orderId));
        BigDecimal labor = BusinessValueUtils.money(itemMapper.selectLaborCostByOrderId(orderId));
        return new CostBreakdown(material, labor, BusinessValueUtils.money(material.add(labor)));
    }

    /** 计算页面尚未保存明细的成本预览。 */
    private CostBreakdown calculatePreviewCost(List<OdOrderItem> items) {
        BigDecimal material = BigDecimal.ZERO;
        BigDecimal labor = BigDecimal.ZERO;
        if (items != null) {
            for (OdOrderItem item : items) {
                if (item.getStyleId() == null) continue;
                BigDecimal qty = BusinessValueUtils.zero(item.getQty());
                material = material.add(qty.multiply(BusinessValueUtils.money(itemMapper.selectMaterialCostByStyleId(item.getStyleId()))));
                labor = labor.add(qty.multiply(BusinessValueUtils.money(itemMapper.selectLaborCostByStyleId(item.getStyleId()))));
            }
        }
        material = BusinessValueUtils.money(material);
        labor = BusinessValueUtils.money(labor);
        return new CostBreakdown(material, labor, BusinessValueUtils.money(material.add(labor)));
    }

    /** 成本构成值对象。 */
    private record CostBreakdown(BigDecimal material, BigDecimal labor, BigDecimal total) {}

    /** 读取最低毛利率配置。 */
    private BigDecimal loadMinGrossMargin() {
        String value = loadConfig("MIN_GROSS_MARGIN", "0");
        try {
            return new BigDecimal(value);
        } catch (Exception ignored) {
            return BigDecimal.ZERO;
        }
    }

    /** 读取订单规则配置值。 */
    private String loadConfig(String code, String defaultValue) {
        SysCommon common = commonMapper.selectOne(new QueryWrapper<SysCommon>()
                .eq("dict_type", "ORDER_RULE").eq("dict_code", code).eq("is_del", 0).last("LIMIT 1"));
        return common == null || !StringUtils.hasText(common.getExtValue()) ? defaultValue : common.getExtValue();
    }

    /** 规范化关闭原因，可做性不通过时自动带出指标说明。 */
    private CloseReason normalizeCloseReason(Integer type, String reason, OdOrderMetrics metrics, boolean reviewRejected) {
        int closeType = type == null ? (reviewRejected ? 1 : 4) : type;
        if (closeType < 1 || closeType > 4) throw new BusinessValidationException("请选择有效的关闭原因类型");
        String text = reason == null ? "" : reason.trim();
        if (!StringUtils.hasText(text) && closeType == 1) {
            text = "预计成本 " + metrics.getCostCompareBase().subtract(metrics.getGrossProfit()).stripTrailingZeros().toPlainString()
                    + "，订单金额 " + metrics.getCostCompareBase().stripTrailingZeros().toPlainString()
                    + "，毛利率 " + metrics.getGrossMarginRate().multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "%";
        }
        if (!StringUtils.hasText(text)) throw new BusinessValidationException("请填写关闭原因");
        if (text.length() > 200) text = text.substring(0, 200);
        return new CloseReason(closeType, text);
    }

    /** 生成可做性校验提示。 */
    private String viabilityMessage(OdOrderMetrics metrics) {
        return "成本比较基数=" + metrics.getCostCompareBase() + "，毛利额=" + metrics.getGrossProfit()
                + "，毛利率=" + metrics.getGrossMarginRate().multiply(new BigDecimal("100")) + "%";
    }

    /** 构建订单流程进度。 */
    private List<OdOrderProgressNode> buildProgress(OdOrder order) {
        List<OdOrderProgressNode> nodes = new ArrayList<>();
        nodes.add(new OdOrderProgressNode("订单创建", "done"));
        int status = order.getStatus() == null ? STATUS_INITIAL : order.getStatus();
        if (status == STATUS_CLOSED) {
            nodes.add(new OdOrderProgressNode("可做性评审", "closed"));
            nodes.add(new OdOrderProgressNode("样衣确认 / 放行", "pending"));
            nodes.add(new OdOrderProgressNode("生产与交付", "pending"));
            return nodes;
        }
        nodes.add(new OdOrderProgressNode("订单审核", status == STATUS_REAUDIT ? "closed" : Integer.valueOf(1).equals(order.getReviewResult()) ? "done" : "current"));
        nodes.add(new OdOrderProgressNode("样衣确认 / 放行", Integer.valueOf(1).equals(order.getIsProducible()) ? "done" : (List.of(STATUS_SAMPLE_GENERATING, STATUS_WAIT_PRODUCING).contains(status) ? "current" : "pending")));
        nodes.add(new OdOrderProgressNode("生产与交付", status == STATUS_DONE ? "done" : (status >= STATUS_PRODUCING && status <= STATUS_PART_DELIVERED ? "current" : "pending")));
        nodes.add(new OdOrderProgressNode("订单完成", status == STATUS_DONE ? "done" : "pending"));
        return nodes;
    }

    /** 查询并校验订单存在。 */
    private OdOrder requireOrder(Long orderId) {
        OdOrder order = orderMapper.selectActiveById(orderId);
        if (order == null) throw new BusinessValidationException("订单不存在");
        return order;
    }

    /** 统计订单号重复数。 */
    private int mapperCount(String orderNo, Long excludeId) {
        return orderMapper.countByOrderNo(orderNo, excludeId);
    }

    /** 获取当前登录用户 ID。 */
    private Long currentUserId() {
        LoginUserContext.LoginUser user = LoginUserContext.get();
        return user == null ? null : user.id();
    }

    /** 关闭原因值对象。 */
    private record CloseReason(int type, String text) {}
}
