-- 其他订单、生产计划和工序任务 CRUD 所需的基础配置。
-- 编码规则统一使用 ct_gm 架构，避免多 Schema 环境下写入错误数据库。

INSERT INTO ct_gm.t_sys_code_rule
    (rule_code, rule_name, biz_type, target_table, target_column, prefix, date_format, `separator`,
     sequence_length, sequence_start, sequence_step, reset_cycle, allow_manual, enabled, remark,
     created_at, updated_at, is_del)
VALUES
    ('CODE_MISC', '其他单号', 'MISC', 't_ms_order', 'misc_no', 'MS', 'yyyyMMdd', '', 3, 1, 1, 'DAY', 1, 1, '其他订单自动编码', NOW(), NOW(), 0),
    ('CODE_PROD_PLAN', '生产计划单号', 'PROD_PLAN', 't_pl_prod_plan', 'prod_plan_no', 'PP', 'yyyyMMdd', '', 3, 1, 1, 'DAY', 1, 1, '生产计划自动编码', NOW(), NOW(), 0),
    ('CODE_PROD_TASK', '工序任务号', 'PROD_TASK', 't_pl_task', 'task_no', 'PT', 'yyyyMMdd', '', 3, 1, 1, 'DAY', 1, 1, '工序任务自动编码', NOW(), NOW(), 0)
ON DUPLICATE KEY UPDATE
    rule_name = VALUES(rule_name),
    target_table = VALUES(target_table),
    target_column = VALUES(target_column),
    prefix = VALUES(prefix),
    date_format = VALUES(date_format),
    `separator` = VALUES(`separator`),
    sequence_length = VALUES(sequence_length),
    sequence_start = VALUES(sequence_start),
    sequence_step = VALUES(sequence_step),
    reset_cycle = VALUES(reset_cycle),
    allow_manual = VALUES(allow_manual),
    enabled = VALUES(enabled),
    remark = VALUES(remark),
    updated_at = NOW(),
    is_del = 0;

INSERT INTO ct_gm.t_ct_page
    (page_code, page_name, module_code, route_path, page_type, is_menu, sort_no, status, remark,
     created_at, updated_at, is_del)
VALUES
    ('misc.order', '其他订单', 'misc', '/misc?tab=misc-orders', 1, 1, 210, 1, '其他订单、费用和日常支出', NOW(), NOW(), 0),
    ('misc.order-item', '其他订单明细', 'misc', '/misc?tab=misc-order-items', 2, 0, 211, 1, '其他订单明细维护', NOW(), NOW(), 0),
    ('prod.plan', '生产计划', 'production', '/production', 1, 1, 180, 1, '生产计划创建与下达', NOW(), NOW(), 0),
    ('prod.cutting', '前道裁剪', 'production', '/production/cutting', 1, 1, 181, 1, '前道裁剪工序任务', NOW(), NOW(), 0),
    ('prod.sewing', '后道缝制', 'production', '/production/sewing', 1, 1, 182, 1, '后道缝制工序任务', NOW(), NOW(), 0),
    ('prod.trimming', '剪线工序', 'production', '/production/trimming', 1, 1, 183, 1, '剪线工序任务', NOW(), NOW(), 0),
    ('prod.ironing', '整烫工序', 'production', '/production/ironing', 1, 1, 184, 1, '整烫工序任务', NOW(), NOW(), 0),
    ('prod.packing', '包装工序', 'production', '/production/packing', 1, 1, 185, 1, '包装工序任务', NOW(), NOW(), 0),
    ('prod.task', '全部工序任务', 'production', '/production/tasks', 1, 0, 186, 1, '全部生产工序任务', NOW(), NOW(), 0)
ON DUPLICATE KEY UPDATE
    page_name = VALUES(page_name),
    module_code = VALUES(module_code),
    route_path = VALUES(route_path),
    page_type = VALUES(page_type),
    is_menu = VALUES(is_menu),
    sort_no = VALUES(sort_no),
    status = VALUES(status),
    remark = VALUES(remark),
    updated_at = NOW(),
    is_del = 0;
