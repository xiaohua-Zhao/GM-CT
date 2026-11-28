-- 生产计划新增生产工序字段（1裁剪 2缝制 3剪线 4整烫 5包装 6质检）。
ALTER TABLE ct_gm.t_pl_prod_plan
    ADD COLUMN prod_process TINYINT NULL COMMENT '生产工序：1裁剪 2缝制 3剪线 4整烫 5包装 6质检' AFTER prod_qty;
