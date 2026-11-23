package com.chengtai.fashionmgmt.module.ms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chengtai.fashionmgmt.common.AbstractBaseService;
import com.chengtai.fashionmgmt.common.BusinessLogJson;
import com.chengtai.fashionmgmt.common.BusinessValidationException;
import com.chengtai.fashionmgmt.common.BusinessValueUtils;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import com.chengtai.fashionmgmt.module.ms.entity.MsOrder;
import com.chengtai.fashionmgmt.module.ms.entity.MsOrderItem;
import com.chengtai.fashionmgmt.module.ms.mapper.MsOrderItemMapper;
import com.chengtai.fashionmgmt.module.ms.mapper.MsOrderMapper;
import com.chengtai.fashionmgmt.module.ms.service.MsOrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/** 其他订单明细业务服务，保存、修改和删除均使用自定义 SQL。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MsOrderItemServiceImpl extends AbstractBaseService<MsOrderItemMapper, MsOrderItem> implements MsOrderItemService {
    private final MsOrderItemMapper mapper;
    private final MsOrderMapper orderMapper;
    private final BusinessLogJson logJson;

    /**
     * 新增其他订单明细：计算金额、校验行号并回写主单金额。
     */
    @Override
    @Transactional
    public boolean create(MsOrderItem request) {
        log.info("[MS_ORDER_ITEM][CREATE][IN] class={} method={} request={}",
                "MsOrderItemServiceImpl", "create", logJson.toJson(request));
        request.setId(null);
        normalize(request);
        validate(request, true);
        if (mapper.countBySeqNo(request.getMsOrderId(), request.getSeqNo(), null) > 0) {
            throw new BusinessValidationException("其他订单内行号已存在：" + request.getSeqNo());
        }
        int affected = mapper.insertBusiness(request, currentUserId());
        orderMapper.recalculateTotals(request.getMsOrderId());
        log.info("[MS_ORDER_ITEM][CREATE][OUT] class={} method={} affected={} itemId={} msOrderId={}",
                "MsOrderItemServiceImpl", "create", affected, request.getId(), request.getMsOrderId());
        return affected == 1;
    }

    /**
     * 修改其他订单明细：保护所属订单、重新计算金额并回写主单金额。
     */
    @Override
    @Transactional
    public boolean update(MsOrderItem request) {
        log.info("[MS_ORDER_ITEM][UPDATE][IN] class={} method={} itemId={} request={}",
                "MsOrderItemServiceImpl", "update", request.getId(), logJson.toJson(request));
        MsOrderItem existing = findById(request.getId());
        if (existing == null) {
            throw new BusinessValidationException("其他订单明细不存在");
        }
        Long originalOrderId = existing.getMsOrderId();
        BusinessValueUtils.copyNonNullProperties(request, existing);
        existing.setMsOrderId(originalOrderId);
        normalize(existing);
        validate(existing, false);
        if (mapper.countBySeqNo(existing.getMsOrderId(), existing.getSeqNo(), existing.getId()) > 0) {
            throw new BusinessValidationException("其他订单内行号已存在：" + existing.getSeqNo());
        }
        int affected = mapper.updateBusiness(existing);
        orderMapper.recalculateTotals(existing.getMsOrderId());
        log.info("[MS_ORDER_ITEM][UPDATE][OUT] class={} method={} affected={} itemId={} msOrderId={}",
                "MsOrderItemServiceImpl", "update", affected, existing.getId(), existing.getMsOrderId());
        return affected == 1;
    }

    /**
     * 按其他订单 ID 查询未删除的明细列表。
     */
    @Override
    public List<MsOrderItem> listByMsOrderId(Long msOrderId) {
        return mapper.selectList(new QueryWrapper<MsOrderItem>()
                .eq("ms_order_id", msOrderId)
                .eq("is_del", 0)
                .orderByAsc("seq_no"));
    }

    /**
     * 按当前分页和关键字查询未删除的其他订单明细。
     */
    @Override
    public IPage<MsOrderItem> page(long current, long size, String keyword, String keywordColumn) {
        QueryWrapper<MsOrderItem> query = new QueryWrapper<>();
        query.eq("is_del", 0);
        if (StringUtils.hasText(keyword)) {
            query.like(keywordColumn, keyword.trim());
        }
        query.orderByDesc("created_at");
        return page(new Page<>(current, size), query);
    }

    /**
     * 按主键查询未删除的其他订单明细。
     */
    @Override
    public MsOrderItem findById(Long id) {
        if (id == null) {
            return null;
        }
        return mapper.selectOne(new QueryWrapper<MsOrderItem>()
                .eq("ms_order_item_id", id)
                .eq("is_del", 0));
    }

    /**
     * 逻辑删除明细，并重新汇总所属其他订单金额。
     */
    @Override
    @Transactional
    public boolean delete(Long id) {
        MsOrderItem existing = findById(id);
        if (existing == null) {
            return false;
        }
        boolean success = mapper.logicalDelete(id) == 1;
        orderMapper.recalculateTotals(existing.getMsOrderId());
        log.info("[MS_ORDER_ITEM][DELETE][OUT] class={} method={} itemId={} msOrderId={} success={}",
                "MsOrderItemServiceImpl", "delete", id, existing.getMsOrderId(), success);
        return success;
    }

    /**
     * 补齐明细行号、单位和数量默认值，并按数量乘单价重新计算金额。
     */
    private void normalize(MsOrderItem entity) {
        if (entity.getMsOrderId() != null && (entity.getSeqNo() == null || entity.getSeqNo() <= 0)) {
            entity.setSeqNo(mapper.nextSeqNo(entity.getMsOrderId()));
        }
        if (!StringUtils.hasText(entity.getUnit())) entity.setUnit("项");
        entity.setQty(BusinessValueUtils.zero(entity.getQty()));
        entity.setPrice(BusinessValueUtils.zero(entity.getPrice()));
        entity.setAmount(BusinessValueUtils.multiply(entity.getQty(), entity.getPrice()));
        if (entity.getAssetFlag() == null) entity.setAssetFlag(0);
    }

    /**
     * 校验明细所属订单、项目名称、数量和非负金额规则。
     */
    private void validate(MsOrderItem entity, boolean creating) {
        if (entity.getMsOrderId() == null) {
            throw new BusinessValidationException("请选择其他订单");
        }
        MsOrder order = orderMapper.selectById(entity.getMsOrderId());
        if (order == null) {
            throw new BusinessValidationException("其他订单不存在");
        }
        if (!StringUtils.hasText(entity.getItemName())) {
            throw new BusinessValidationException("项目名称不能为空");
        }
        if (entity.getQty().signum() < 0 || entity.getPrice().signum() < 0) {
            throw new BusinessValidationException("数量和单价不能小于0");
        }
    }

    /**
     * 获取当前登录用户 ID，用于记录明细创建人。
     */
    private Long currentUserId() {
        LoginUserContext.LoginUser user = LoginUserContext.get();
        return user == null ? null : user.id();
    }
}
