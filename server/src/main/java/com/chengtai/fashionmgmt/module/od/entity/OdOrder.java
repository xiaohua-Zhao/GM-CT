package com.chengtai.fashionmgmt.module.od.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 订单实体，对应表 t_od_order。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_od_order")
public class OdOrder extends BaseEntity {
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门ID
    private String orderNo; // 订单号
    private Integer orderType; // 订单类型
    private Long unitId; // 客户单位ID
    private String contractNo; // 合同号
    private Long salesmanId; // 业务员ID
    private String orderDate; // 订单日期
    private String startDate; // 开始日期
    private String deliveryDatePlan; // 预计交付日期
    private String deliveryDateAct; // 实际交付日期
    private String currency; // 币种
    private BigDecimal qtyTotal; // 订单总数量
    private BigDecimal amountTotal; // 订单金额
    private BigDecimal taxRate; // 税率
    private BigDecimal amountTax; // 价税合计
    private BigDecimal costPlan; // 预计成本
    private BigDecimal costAct; // 实际成本
    private BigDecimal depositAmount; // 已收定金
    private String payDatePlan; // 预计首款日期
    private Integer isProducible; // 是否允许生产
    private String releaseTime; // 放行时间
    private Integer status; // 订单状态
    private Integer delayDays; // 逾期天数
    private String remark; // 备注
}
