package com.chengtai.fashionmgmt.module.sm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import com.chengtai.fashionmgmt.module.sm.dto.SmSampleMadeRequest;
import com.chengtai.fashionmgmt.module.sm.dto.SmSampleAttachmentRequest;
import com.chengtai.fashionmgmt.module.sys.entity.SysAttach;
import com.chengtai.fashionmgmt.module.sys.entity.SysUnit;
import com.chengtai.fashionmgmt.module.sys.mapper.SysUnitMapper;
import com.chengtai.fashionmgmt.module.sys.mapper.SysAttachMapper;
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
    private final SysAttachMapper attachMapper;
    private final SysUnitMapper unitMapper;
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
        if (Objects.equals(existing.getStatus(), 4) || Objects.equals(existing.getStatus(), 6)) {
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

    /** 分页查询样衣并补充订单和客户信息。 */
    @Override
    public IPage<SmSample> page(long current, long size, String keyword, String keywordColumn) {
        QueryWrapper<SmSample> query = new QueryWrapper<>();
        query.eq("is_del", 0);
        if (StringUtils.hasText(keyword)) query.like(keywordColumn, keyword.trim());
        query.orderByDesc("created_at");
        IPage<SmSample> page = mapper.selectPage(new Page<>(current, size), query);
        page.getRecords().forEach(this::fillCustomerInfo);
        return page;
    }

    /** 查询样衣已上传的附件。 */
    public List<SysAttach> attachments(Long sampleId) {
        return attachMapper.selectList(new QueryWrapper<SysAttach>().eq("biz_type", "sample").eq("biz_id", sampleId).eq("is_del", 0).orderByDesc("version_no").orderByDesc("created_at"));
    }

    /** 删除样衣附件，采用逻辑删除并保留版本历史。 */
    @Transactional
    public boolean deleteAttachment(Long sampleId, Long attachId) {
        SmSample sample = getById(sampleId);
        if (sample == null) throw new BusinessValidationException("样衣不存在");
        int affected = attachMapper.update(null, new UpdateWrapper<SysAttach>()
                .eq("attach_id", attachId).eq("biz_type", "sample").eq("biz_id", sampleId).eq("is_del", 0)
                .set("is_del", 1).set("updated_at", LocalDateTime.now()));
        if (affected == 0) throw new BusinessValidationException("附件不存在或已删除");
        writeLog(sample, 5, "删除样衣附件 #" + attachId);
        return true;
    }

    /** 批量保存样衣附件。 */
    @Transactional
    public List<SysAttach> attachFiles(Long sampleId, List<SmSampleAttachmentRequest> requests) {
        SmSample sample = getById(sampleId);
        if (sample == null) throw new BusinessValidationException("样衣不存在");
        if (requests == null || requests.isEmpty()) throw new BusinessValidationException("请至少上传一个附件");
        List<SmSampleAttachmentRequest> validRequests = requests.stream()
                .filter(Objects::nonNull)
                .filter(request -> StringUtils.hasText(request.getFileUrl()))
                .toList();
        if (validRequests.isEmpty()) throw new BusinessValidationException("附件地址不能为空");
        // 版本号基于全部历史记录（含已删除附件）递增，确保删除附件后版本号不会回退或被复用。
        SysAttach latest = attachMapper.selectOne(new QueryWrapper<SysAttach>()
                .eq("biz_type", "sample").eq("biz_id", sampleId)
                .orderByDesc("version_no").last("LIMIT 1"));
        int versionNo = latest == null || latest.getVersionNo() == null ? 1 : latest.getVersionNo() + 1;
        for (SmSampleAttachmentRequest request : validRequests) {
            SysAttach attach = new SysAttach();
            attach.setDeptId(sample.getDeptId());
            attach.setBizType("sample");
            attach.setBizId(sampleId);
            attach.setCategory("样衣附件");
            attach.setFileName(StringUtils.hasText(request.getFileName()) ? request.getFileName() : "未命名附件");
            attach.setFileSize(request.getFileSize() == null ? 0L : request.getFileSize());
            attach.setMimeType(request.getMimeType());
            attach.setFileUrl(request.getFileUrl());
            attach.setUploaderId(currentUserId());
            attach.setUploadTime(LocalDateTime.now().format(DATE_TIME_FORMATTER));
            attach.setVersionNo(versionNo);
            attach.setStatus(1);
            attachMapper.insert(attach);
        }
        writeLog(sample, 5, "上传样衣附件 " + validRequests.size() + " 个", validRequests.get(0).getFileUrl());
        return attachments(sampleId);
    }

    /** 上传制作附件后标记样衣已制作，状态进入待确认。 */
    @Transactional
    public SmSample markMade(Long sampleId, SmSampleMadeRequest request) {
        SmSample sample = getById(sampleId);
        if (sample == null) throw new BusinessValidationException("样衣不存在");
        if (!Objects.equals(sample.getStatus(), 1) && !Objects.equals(sample.getStatus(), 5)) {
            throw new BusinessValidationException("只有待制作或待修正样衣可以标记已制作");
        }
        Long existing = attachMapper.selectCount(new QueryWrapper<SysAttach>().eq("biz_type", "sample").eq("biz_id", sampleId).eq("is_del", 0));
        if ((existing == null || existing == 0) && (request == null || !StringUtils.hasText(request.getFileUrl()))) {
            throw new BusinessValidationException("请先上传样衣附件");
        }
        if ((existing == null || existing == 0) && request != null && StringUtils.hasText(request.getFileUrl())) {
            SmSampleAttachmentRequest attachment = new SmSampleAttachmentRequest();
            attachment.setFileName(request.getFileName()); attachment.setFileUrl(request.getFileUrl());
            attachment.setFileSize(request.getFileSize()); attachment.setMimeType(request.getMimeType());
            attachFiles(sampleId, List.of(attachment));
        }
        sample.setStatus(2);
        mapper.updateById(sample);
        writeLog(sample, 5, request != null && StringUtils.hasText(request.getRemark()) ? request.getRemark() : "样衣已制作并上传附件", request == null ? null : request.getFileUrl());
        return getById(sampleId);
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

    /** 补充样衣关联订单与客户单位信息。 */
    private void fillCustomerInfo(SmSample sample) {
        OdOrder order = orderMapper.selectById(sample.getOrderId());
        if (order == null) return;
        sample.setOrderNo(order.getOrderNo());
        sample.setUnitId(order.getUnitId());
        SysUnit unit = order.getUnitId() == null ? null : unitMapper.selectById(order.getUnitId());
        if (unit != null) {
            sample.setCustomerName(unit.getUnitName());
            sample.setContactName(unit.getContact());
            sample.setContactPhone(unit.getPhone());
        }
    }

    /** 校验样衣关联订单已评审且订单明细包含所选款式。 */
    private void validate(SmSample sample) {
        if (sample.getOrderId() == null) throw new BusinessValidationException("请选择订单");
        if (sample.getStyleId() == null) throw new BusinessValidationException("请选择款式");
        OdOrder order = orderMapper.selectById(sample.getOrderId());
        if (order == null || !Objects.equals(order.getReviewResult(), 1)) {
            throw new BusinessValidationException("订单不存在或尚未评审通过");
        }
        if (!Objects.equals(order.getStatus(), 2) && !Objects.equals(order.getStatus(), 10)) throw new BusinessValidationException("订单当前状态不能维护样衣");
        Long count = orderItemMapper.selectCount(new QueryWrapper<OdOrderItem>()
                .eq("order_id", sample.getOrderId()).eq("style_id", sample.getStyleId()).eq("is_del", 0));
        if (count == null || count == 0) throw new BusinessValidationException("所选款式不属于该订单明细");
    }

    /** 写样衣跟单记录。 */
    private void writeLog(SmSample sample, int actionType, String content) {
        writeLog(sample, actionType, content, null);
    }

    /** 写样衣跟单记录并保留附件地址。 */
    private void writeLog(SmSample sample, int actionType, String content, String attachUrl) {
        SmSampleLog entry = new SmSampleLog();
        entry.setDeptId(sample.getDeptId());
        entry.setSampleId(sample.getId());
        entry.setLogTime(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        entry.setActionType(actionType);
        entry.setContent(content);
        entry.setAttachUrl(attachUrl);
        entry.setOperatorId(currentUserId());
        logMapper.insert(entry);
    }

    /** 获取当前登录用户 ID。 */
    private Long currentUserId() {
        LoginUserContext.LoginUser user = LoginUserContext.get();
        return user == null ? null : user.id();
    }
}
