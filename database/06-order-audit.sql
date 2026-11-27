-- 订单审核备注字段。
ALTER TABLE ct_gm.t_od_order
    ADD COLUMN audit_remark VARCHAR(200) NULL COMMENT '审核备注' AFTER close_time;
