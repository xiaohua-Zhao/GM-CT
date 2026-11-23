package com.chengtai.fashionmgmt.module.wg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chengtai.fashionmgmt.common.LogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/** 工资发放明细实体，对应表 ct_gm.t_wg_payment_item。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ct_gm.t_wg_payment_item")
public class WgPaymentItem extends LogicEntity {
    @TableId(value = "payment_item_id", type = IdType.AUTO)
    private Long id;

    private Long deptId; // 归属部门
    private Long paymentBatchId; // 工资发放批次
    private Long billId; // 工资单
    private Long staffId; // 员工
    private BigDecimal amount; // 实发金额
    private Integer paymentMethod; // 发放方式
    private Long finFlowId; // 资金流水
    private Integer status; // 状态
    private String paidAt; // 发放时间
    private String remark; // 备注
}
