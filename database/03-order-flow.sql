-- 货物订单评审、关闭留痕及可做性评审参数。
ALTER TABLE ct_gm.t_od_order
    ADD COLUMN review_result TINYINT(1) NOT NULL DEFAULT 0 COMMENT '评审结论 0未评审 1可做 2不可做' AFTER status,
    ADD COLUMN review_by BIGINT NULL COMMENT '评审人' AFTER review_result,
    ADD COLUMN review_time DATETIME NULL COMMENT '评审时间' AFTER review_by,
    ADD COLUMN close_reason_type TINYINT NULL COMMENT '关闭原因类型 1成本 2交期 3客户取消 4其他' AFTER review_time,
    ADD COLUMN close_reason VARCHAR(200) NULL COMMENT '关闭原因说明' AFTER close_reason_type,
    ADD COLUMN close_by BIGINT NULL COMMENT '关闭人' AFTER close_reason,
    ADD COLUMN close_time DATETIME NULL COMMENT '关闭时间' AFTER close_by;

ALTER TABLE ct_gm.t_od_order ADD INDEX idx_od_order_review_result (review_result);

INSERT INTO ct_gm.t_sys_common
    (dict_type, dict_code, dict_name, ext_value, sort_no, is_system, status, remark, created_at, updated_at, is_del)
VALUES
    ('ORDER_RULE', 'MIN_GROSS_MARGIN', '订单最低毛利率', '0', 1, 1, 1, '小数比率，0.05 表示 5%', NOW(), NOW(), 0),
    ('ORDER_RULE', 'COST_COMPARE_MODE', '订单成本比较口径', 'EX_TAX', 2, 1, 1, 'EX_TAX 不含税 / IN_TAX 含税', NOW(), NOW(), 0)
ON DUPLICATE KEY UPDATE
    dict_name=VALUES(dict_name),
    ext_value=VALUES(ext_value),
    status=VALUES(status),
    remark=VALUES(remark),
    updated_at=NOW(),
    is_del=0;
