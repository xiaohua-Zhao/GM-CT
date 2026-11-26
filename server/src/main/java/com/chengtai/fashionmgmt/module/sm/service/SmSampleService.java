package com.chengtai.fashionmgmt.module.sm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.BusinessValidationException;
import com.chengtai.fashionmgmt.common.BusinessValueUtils;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.od.entity.OdOrder;
import com.chengtai.fashionmgmt.module.od.entity.OdOrderItem;
import com.chengtai.fashionmgmt.module.od.mapper.OdOrderItemMapper;
import com.chengtai.fashionmgmt.module.od.mapper.OdOrderMapper;
import com.chengtai.fashionmgmt.module.sm.dto.SmSampleConfirmRequest;
import com.chengtai.fashionmgmt.module.sm.entity.SmSample;
import com.chengtai.fashionmgmt.module.sm.entity.SmSampleLog;
import com.chengtai.fashionmgmt.module.sm.mapper.SmSampleLogMapper;
import com.chengtai.fashionmgmt.module.sm.mapper.SmSampleMapper;
import com.chengtai.fashionmgmt.module.sys.service.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/** 样衣业务服务：样衣登记、寄出、客户确认和订单放行。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmSampleService extends AbstractBaseService<SmSampleMapper, SmSample> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final SmSampleMapper mapper;
    private final SmSampleLogMapper logMapper;
    private final OdOrderMapper orderMapper;
    private final OdOrderItemMapper orderItemMapper;
    private final CodeGeneratorService codeGeneratorService;
    private final BusinessLogJson logJson;

    /** 新增样衣并校验订单评审状态和款式关联。 */
    @Override
    @Transactional
    public boolean create(SmSample request) {
        log.info("[SM_SAMPLE][CREATE][IN] request={}", logJson.toJson(request));
        request.setId(null);
        normalize(request);
        validate(request);
        if (!StringUtils.hasText(request.getSampleNo())) {
            request.setSampleNo(codeGeneratorService.generate("SAMPLE").getPreviewCode());
        }
        if (count(new QueryWrapper<SmSample>().eq("sample_no", request.getSampleNo()).eq("is_del", 0)) > 0) {
            throw new BusinessValidationException("样衣编号已存在：" + request.getSampleNo());
        }
        boolean success = save(request);
        if (success) writeLog(request, 5, "创建样衣 " + request.getSampleNo());
        return success;
    }

    /** 修改样衣，客户确认后不允许再编辑。 */
    @Override
    @Transactional
    public boolean update(SmSample request) {
        SmSample existing = getById(request.getId());
        if (existing == null) throw new BusinessValidationException("样衣不存在");
        if (Objects.equals(existing.getStatus(), 4) || Objects.equals(existing.getStatus(), 5) || Objects.equals(existing.getStatus(), 6)) {
            throw new BusinessValidationException("已确认或作废样衣不能修改");
        }
        String sampleNo = existing.getSampleNo();
        Long orderId = existing.getOrderId();
        BusinessValueUtils.copyNonNullProperties(request, existing);
        existing.setSampleNo(sampleNo);
        existing.setOrderId(orderId);
        normalize(existing);
        validate(existing);
        return updateById(existing);
    }

    /** 查询允许创建样衣的订单：评审通过、未关闭。 */
    public List<OdOrder> eligibleOrders(String keyword) {
        QueryWrapper<OdOrder> query = new QueryWrapper<>();
        query.eq("is_del", 0).eq("review_result", 1).eq("status", 2).orderByDesc("created_at").last("LIMIT 200");
        if (StringUtils.hasText(keyword)) {
            String value = keyword.trim();
            query.and(wrapper -> wrapper.like("order_no", value)
                    .or().like("contract_no", value)
                    .or().apply("unit_id IN (SELECT unit_id FROM ct_gm.t_sys_unit WHERE unit_name LIKE CONCAT('%', {0}, '%') AND is_del=0)", value));
        }
        return orderMapper.selectList(query);
    }

    /** 将样衣寄出并进入客户确认中。 */
    @Transactional
    public SmSample send(Long sampleId) {
        SmSample sample = getById(sampleId);
        if (sample == null) throw new BusinessValidationException("样衣不存在");
        if (!Objects.equals(sample.getStatus(), 1) && !Objects.equals(sample.getStatus(), 2)) {
            throw new BusinessValidationException("当前样衣状态不能提交客户确认");
        }
        sample.setStatus(2);
        if (!StringUtils.hasText(sample.getSendDate())) sample.setSendDate(LocalDate.now().toString());
        mapper.updateById(sample);
        writeLog(sample, 1, "样衣已寄出，等待客户确认");
        return getById(sampleId);
    }

    /** 客户确认样衣；通过后自动将订单置为可生产并写放行时间。 */
    @Transactional
    public SmSample confirm(Long sampleId, SmSampleConfirmRequest request) {
        SmSample sample = getById(sampleId);
        if (sample == null) throw new BusinessValidationException("样衣不存在");
        if (!Objects.equals(sample.getStatus(), 2) && !Objects.equals(sample.getStatus(), 3)) {
            throw new BusinessValidationException("只有已寄出或待确认样衣可以确认");
        }
        if (!Integer.valueOf(1).equals(request.getConfirmResult()) && !Integer.valueOf(2).equals(request.getConfirmResult())) {
            throw new BusinessValidationException("确认结果只能为通过或不通过");
        }
        String confirmDate = StringUtils.hasText(request.getConfirmDate()) ? request.getConfirmDate() : LocalDate.now().toString();
        sample.setConfirmResult(request.getConfirmResult());
        sample.setConfirmDate(confirmDate);
        sample.setBackDate(confirmDate);
        sample.setConfirmBy(currentUserId());
        sample.setStatus(Integer.valueOf(1).equals(request.getConfirmResult()) ? 4 : 5);
        mapper.updateById(sample);
        if (Integer.valueOf(1).equals(request.getConfirmResult())) {
            orderMapper.releaseForProduction(sample.getOrderId(), LocalDateTime.now().format(DATE_TIME_FORMATTER));
            Long total = mapper.selectCount(new QueryWrapper<SmSample>().eq("order_id", sample.getOrderId()).eq("is_del", 0));
            Long confirmed = mapper.selectCount(new QueryWrapper<SmSample>().eq("order_id", sample.getOrderId()).eq("is_del", 0).eq("confirm_result", 1));
            if (total != null && total > 0 && total.equals(confirmed)) {
                orderMapper.updateStatus(sample.getOrderId(), 11);
            }
            writeLog(sample, 4, StringUtils.hasText(request.getRemark()) ? request.getRemark() : "客户确认通过，订单已放行");
        } else {
            writeLog(sample, 2, StringUtils.hasText(request.getRemark()) ? request.getRemark() : "客户确认不通过，需要返修或重新打样");
        }
        return getById(sampleId);
    }

    /** 查询样衣跟单记录。 */
    public List<SmSampleLog> logs(Long sampleId) {
        return logMapper.selectList(new QueryWrapper<SmSampleLog>().eq("sample_id", sampleId).eq("is_del", 0).orderByDesc("log_time"));
    }

    /** 补齐样衣默认值。 */
    private void normalize(SmSample sample) {
        if (sample.getSampleType() == null) sample.setSampleType(1);
        if (sample.getRoundNo() == null || sample.getRoundNo() < 1) sample.setRoundNo(1);
        if (sample.getQty() == null || sample.getQty() < 1) sample.setQty(1);
        if (sample.getConfirmResult() == null) sample.setConfirmResult(0);
        if (sample.getFee() == null) sample.setFee(BigDecimal.ZERO);
        if (sample.getStatus() == null) sample.setStatus(1);
    }

    /** 校验样衣关联订单已评审且订单明细包含所选款式。 */
    private void validate(SmSample sample) {
        if (sample.getOrderId() == null) throw new BusinessValidationException("请选择订单");
        if (sample.getStyleId() == null) throw new BusinessValidationException("请选择款式");
        OdOrder order = orderMapper.selectById(sample.getOrderId());
        if (order == null || !Objects.equals(order.getReviewResult(), 1)) {
            throw new BusinessValidationException("订单不存在或尚未评审通过");
        }
        if (!Objects.equals(order.getStatus(), 2)) throw new BusinessValidationException("只有已审核订单可以制作样衣");
        Long count = orderItemMapper.selectCount(new QueryWrapper<OdOrderItem>()
                .eq("order_id", sample.getOrderId()).eq("style_id", sample.getStyleId()).eq("is_del", 0));
        if (count == null || count == 0) throw new BusinessValidationException("所选款式不属于该订单明细");
    }

    /** 写样衣跟单记录。 */
    private void writeLog(SmSample sample, int actionType, String content) {
        SmSampleLog entry = new SmSampleLog();
        entry.setDeptId(sample.getDeptId());
        entry.setSampleId(sample.getId());
        entry.setLogTime(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        entry.setActionType(actionType);
        entry.setContent(content);
        entry.setOperatorId(currentUserId());
        logMapper.insert(entry);
    }

    /** 获取当前登录用户 ID。 */
    private Long currentUserId() {
        LoginUserContext.LoginUser user = LoginUserContext.get();
        return user == null ? null : user.id();
    }
}
