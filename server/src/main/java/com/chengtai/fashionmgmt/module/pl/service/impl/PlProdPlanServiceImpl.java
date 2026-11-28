package com.chengtai.fashionmgmt.module.pl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.BusinessValidationException;
import com.chengtai.fashionmgmt.common.BusinessValueUtils;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.pl.entity.PlProdPlan;
import com.chengtai.fashionmgmt.module.pl.entity.PlTask;
import com.chengtai.fashionmgmt.module.pl.mapper.PlProdPlanMapper;
import com.chengtai.fashionmgmt.module.pl.mapper.PlTaskMapper;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import com.chengtai.fashionmgmt.module.od.mapper.OdOrderMapper;
import com.chengtai.fashionmgmt.module.od.service.OdOrderFlowService;
import com.chengtai.fashionmgmt.module.pl.service.PlProdPlanService;
import com.chengtai.fashionmgmt.module.sys.service.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** 生产计划业务服务，保存、修改和删除均使用自定义 SQL。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlProdPlanServiceImpl extends AbstractBaseService<PlProdPlanMapper, PlProdPlan> implements PlProdPlanService {
    private final PlProdPlanMapper mapper;
    private final PlTaskMapper taskMapper;
    private final CodeGeneratorService codeGeneratorService;
    private final BusinessLogJson logJson;
    private final OdOrderFlowService orderFlowService;
    private final OdOrderMapper orderMapper;

    /** 订单状态：生产中。 */
    private static final int ORDER_STATUS_PRODUCING = 3;

    /**
     * 分页查询可创建生产计划的订单。
     * <p>
     * 业务筛选条件已全部下沉到 {@code PlProdPlanMapper.xml} 的 pageEligibleOrders：
     * 未删除、可做性评审通过、样衣已放行、待生产、未关闭，且尚未创建有效生产计划。
     *
     * @param current           当前页码，从 1 开始
     * @param size              每页条数
     * @param keyword           关键字，匹配订单号、合同号或客户单位名称，可为空
     * @param unitId            客户单位 ID，可为空
     * @param orderDateStart    下单日期起始（含），可为空
     * @param orderDateEnd      下单日期结束（含），可为空
     * @param deliveryDateStart 预计交期起始（含），可为空
     * @param deliveryDateEnd   预计交期结束（含），可为空
     * @return 可排产订单分页结果
     */
    @Override
    public IPage<OdOrder> pageEligibleOrders(long current, long size, String keyword, Long unitId,
                                             String orderDateStart, String orderDateEnd,
                                             String deliveryDateStart, String deliveryDateEnd) {
        // 关键字去掉首尾空格；空白关键字转成 null，由 XML 决定是否拼接模糊查询条件。
        String normalizedKeyword = StringUtils.hasText(keyword) ? keyword.trim() : null;
        // 分页对象交给 MyBatis-Plus 分页插件处理，SQL 统一维护在 PlProdPlanMapper.xml。
        Page<OdOrder> page = new Page<>(current, size);
        return mapper.pageEligibleOrders(page, normalizedKeyword, unitId,
                orderDateStart, orderDateEnd, deliveryDateStart, deliveryDateEnd);
    }

    /**
     * 新增生产计划：自动生成计划号、校验订单唯一性、写入计划，并按所选工序生成工序任务。
     */
    @Override
    @Transactional
    public boolean create(PlProdPlan request) {
        log.info("[PL_PROD_PLAN][CREATE][IN] class={} method={} request={}",
                "PlProdPlanServiceImpl", "create", logJson.toJson(request));
        request.setId(null);
        normalize(request);
        if (!StringUtils.hasText(request.getProdPlanNo())) {
            request.setProdPlanNo(codeGeneratorService.generate("PROD_PLAN").getPreviewCode());
        }
        validate(request, true);
        if (mapper.countByPlanNo(request.getProdPlanNo(), null) > 0) {
            throw new BusinessValidationException("生产计划单号已存在：" + request.getProdPlanNo());
        }
        if (mapper.countByOrderId(request.getOrderId(), null) > 0) {
            throw new BusinessValidationException("该订单已经存在有效生产计划");
        }
        // 先解析并校验工序，避免计划写入后再因工序非法回滚。
        List<Integer> processStages = resolveProcessStages(request.getProcessStages());
        // 生产计划表保存当前生产工序（单选），与后续生成的工序任务保持一致。
        request.setProdProcess(processStages.get(0));
        Long userId = currentUserId();
        int affected = mapper.insertBusiness(request, userId);
        if (affected == 1) {
            // 计划保存成功后，为每个选中的工序生成一条工序任务，供对应工序页面展示。
            createProcessTasks(request, processStages, userId);
            // 订单已有生产计划后，自动流转为生产中状态。
            markOrderProducing(request, userId);
        }
        log.info("[PL_PROD_PLAN][CREATE][OUT] class={} method={} affected={} id={} planNo={} stages={}",
                "PlProdPlanServiceImpl", "create", affected, request.getId(), request.getProdPlanNo(), processStages);
        return affected == 1;
    }

    /**
     * 为新建生产计划生成所选工序的工序任务。
     *
     * @param plan          已写入数据库的生产计划
     * @param processStages 已校验、去重并排序的工序阶段
     * @param userId        当前登录用户 ID，用作任务创建人
     */
    private void createProcessTasks(PlProdPlan plan, List<Integer> processStages, Long userId) {
        for (Integer stage : processStages) {
            PlTask task = new PlTask();
            // 归属当前生产计划
            task.setProdPlanId(plan.getId());
            // 每个工序任务单独生成任务号，编码规则为 PROD_TASK
            task.setTaskNo(codeGeneratorService.generate("PROD_TASK").getPreviewCode());
            // 同一计划下每个工序默认只有一条任务，拆分序号固定为 1
            task.setSeqNo(1);
            // 工序阶段：1裁剪 2缝制 3剪线 4整烫 5包装 6质检
            task.setProcessStage(stage);
            // 执行组默认取计划归属部门，后续可在工序页面调整
            task.setDeptId(plan.getDeptId());
            // 任务初始状态为待下发
            task.setStatus(1);
            // 计划数量、完成数量
            task.setQtyPlan(plan.getProdQty());
            task.setQtyDone(BigDecimal.ZERO);
            // 计划起止日期与生产计划保持一致
            task.setStartDatePlan(plan.getStartDate());
            task.setEndDatePlan(plan.getEndDate());
            // 质检默认参数：需要质检、未检、巡检
            task.setQcNeed(1);
            task.setQcStatus(0);
            task.setQcType(2);
            task.setQcQtyCheck(BigDecimal.ZERO);
            task.setQcQtyPass(BigDecimal.ZERO);
            task.setQcQtyReject(BigDecimal.ZERO);
            // 备注标记任务来源，便于工序页面追溯
            task.setRemark("由生产计划 " + plan.getProdPlanNo() + " 自动生成");
            taskMapper.insertBusiness(task, userId);
        }
    }

    /**
     * 规范化工序阶段：过滤空值、去重、升序，并校验取值范围；
     * 未选择工序时默认生成裁剪、缝制、剪线、整烫、包装、质检全部六个工序。
     */
    private List<Integer> resolveProcessStages(List<Integer> processStages) {
        List<Integer> result = new ArrayList<>();
        if (processStages != null) {
            for (Integer stage : processStages) {
                if (stage == null || result.contains(stage)) {
                    continue;
                }
                if (stage < 1 || stage > 6) {
                    throw new BusinessValidationException("生产工序必须为裁剪、缝制、剪线、整烫、包装或质检");
                }
                result.add(stage);
            }
        }
        if (result.isEmpty()) {
            return List.of(1, 2, 3, 4, 5, 6);
        }
        result.sort(Integer::compareTo);
        return result;
    }

    /**
     * 生产计划创建成功后，将关联订单自动流转为生产中状态。
     * <p>
     * 订单已有生产计划即视为进入生产阶段，同时写入订单状态流转日志，便于订单详情追溯。
     *
     * @param plan   已创建的生产计划
     * @param userId 当前登录用户 ID，用作状态变更操作人
     */
    private void markOrderProducing(PlProdPlan plan, Long userId) {
        OdOrder order = orderMapper.selectActiveById(plan.getOrderId());
        if (order == null) {
            return;
        }
        // 已经是生产中状态时不重复更新，避免产生重复的状态日志。
        if (java.util.Objects.equals(order.getStatus(), ORDER_STATUS_PRODUCING)) {
            return;
        }
        orderMapper.updateStatus(order.getId(), ORDER_STATUS_PRODUCING);
        orderMapper.insertStatusLog(order.getId(), order.getDeptId(), order.getStatus(),
                ORDER_STATUS_PRODUCING, userId, "生产计划已创建：" + plan.getProdPlanNo());
    }

    /**
     * 修改生产计划：保护计划单号和关联订单，重新校验日期和数量后更新；
     * 若前端传入了生产工序，则同步更新该计划对应的工序任务。
     */
    @Override
    @Transactional
    public boolean update(PlProdPlan request) {
        log.info("[PL_PROD_PLAN][UPDATE][IN] class={} method={} id={} request={}",
                "PlProdPlanServiceImpl", "update", request.getId(), logJson.toJson(request));
        PlProdPlan existing = findById(request.getId());
        if (existing == null) {
            throw new BusinessValidationException("生产计划不存在");
        }
        String originalPlanNo = existing.getProdPlanNo();
        Long originalOrderId = existing.getOrderId();
        BusinessValueUtils.copyNonNullProperties(request, existing);
        existing.setProdPlanNo(originalPlanNo);
        existing.setOrderId(originalOrderId);
        normalize(existing);
        validate(existing, false);
        // 先解析工序；未传工序时不调整已有工序任务。
        Integer processStage = resolveSingleProcessStage(request.getProcessStages());
        // 同步生产计划上的生产工序字段，编辑后列表/详情展示最新的工序。
        if (processStage != null) {
            existing.setProdProcess(processStage);
        }
        int affected = mapper.updateBusiness(existing);
        if (affected == 1 && processStage != null) {
            syncProcessTask(existing, processStage, currentUserId());
        }
        log.info("[PL_PROD_PLAN][UPDATE][OUT] class={} method={} affected={} id={} planNo={} stage={}",
                "PlProdPlanServiceImpl", "update", affected, existing.getId(), existing.getProdPlanNo(), processStage);
        return affected == 1;
    }

    /**
     * 解析单个工序阶段；未选择时返回 null，表示不调整工序任务。
     */
    private Integer resolveSingleProcessStage(List<Integer> processStages) {
        if (processStages == null || processStages.isEmpty()) {
            return null;
        }
        Integer stage = processStages.stream().filter(java.util.Objects::nonNull).findFirst().orElse(null);
        if (stage != null && (stage < 1 || stage > 6)) {
            throw new BusinessValidationException("生产工序必须为裁剪、缝制、剪线、整烫、包装或质检");
        }
        return stage;
    }

    /**
     * 同步生产计划的工序任务：已有任务时更新第一条任务的工序、数量和日期；
     * 没有任务时按当前工序补建一条，保证工序页面能查询到该计划。
     */
    private void syncProcessTask(PlProdPlan plan, Integer processStage, Long userId) {
        List<PlTask> tasks = taskMapper.selectList(new QueryWrapper<PlTask>()
                .eq("prod_plan_id", plan.getId())
                .eq("is_del", 0)
                .orderByAsc("seq_no")
                .orderByAsc("task_id"));
        if (tasks.isEmpty()) {
            createProcessTasks(plan, List.of(processStage), userId);
            return;
        }
        PlTask task = tasks.get(0);
        // 工序改变后任务会出现在新的工序页面
        task.setProcessStage(processStage);
        // 同步计划上的数量与日期，保持任务和计划一致
        task.setQtyPlan(plan.getProdQty());
        task.setStartDatePlan(plan.getStartDate());
        task.setEndDatePlan(plan.getEndDate());
        taskMapper.updateBusiness(task);
    }

    /**
     * 按当前分页和关键字查询未删除的生产计划。
     */
    @Override
    public IPage<PlProdPlan> page(long current, long size, String keyword, String keywordColumn) {
        QueryWrapper<PlProdPlan> query = new QueryWrapper<>();
        query.eq("is_del", 0);
        if (StringUtils.hasText(keyword)) {
            query.like(keywordColumn, keyword.trim());
        }
        query.orderByDesc("created_at");
        return page(new Page<>(current, size), query);
    }

    /**
     * 按主键查询未删除的生产计划。
     */
    @Override
    public PlProdPlan findById(Long id) {
        if (id == null) {
            return null;
        }
        return mapper.selectOne(new QueryWrapper<PlProdPlan>().eq("prod_plan_id", id).eq("is_del", 0));
    }

    /**
     * 逻辑删除生产计划；存在工序任务时禁止删除。
     */
    @Override
    @Transactional
    public boolean delete(Long id) {
        log.info("[PL_PROD_PLAN][DELETE][IN] class={} method={} id={}", "PlProdPlanServiceImpl", "delete", id);
        if (findById(id) == null) {
            return false;
        }
        if (taskMapper.countByPlanId(id) > 0) {
            throw new BusinessValidationException("该生产计划下已有工序任务，请先删除工序任务");
        }
        boolean success = mapper.logicalDelete(id) == 1;
        log.info("[PL_PROD_PLAN][DELETE][OUT] class={} method={} id={} success={}",
                "PlProdPlanServiceImpl", "delete", id, success);
        return success;
    }

    /**
     * 补齐生产计划默认状态和数量。
     */
    private void normalize(PlProdPlan entity) {
        entity.setProdQty(BusinessValueUtils.zero(entity.getProdQty()));
        if (entity.getStatus() == null) entity.setStatus(1);
    }

    /**
     * 校验生产计划的关联订单、开工完工日期、数量和计划单号。
     */
    private void validate(PlProdPlan entity, boolean creating) {
        if (creating && !StringUtils.hasText(entity.getProdPlanNo())) {
            throw new BusinessValidationException("生产计划单号不能为空");
        }
        if (entity.getOrderId() == null) {
            throw new BusinessValidationException("请选择订单");
        }
        // 仅新增计划时校验订单未排产；编辑计划时订单已进入生产中，不能再次套用待生产校验。
        if (creating) {
            orderFlowService.assertCanCreatePlan(entity.getOrderId());
        }
        if (!StringUtils.hasText(entity.getStartDate()) || !StringUtils.hasText(entity.getEndDate())) {
            throw new BusinessValidationException("请选择计划开工日和完工日");
        }
        if (entity.getEndDate().compareTo(entity.getStartDate()) < 0) {
            throw new BusinessValidationException("计划完工日不能早于开工日");
        }
        if (entity.getProdQty().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessValidationException("生产数量不能小于0");
        }
        if (entity.getProdQty().stripTrailingZeros().scale() > 0) {
            throw new BusinessValidationException("生产数量必须为整数");
        }
    }

    /**
     * 获取当前登录用户 ID，用于记录生产计划创建人。
     */
    private Long currentUserId() {
        LoginUserContext.LoginUser user = LoginUserContext.get();
        return user == null ? null : user.id();
    }
}
