package com.chengtai.fashionmgmt.module.sys.code;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/** 编码目标表白名单，禁止直接使用数据库配置拼接任意SQL。 */
@Component
public class CodeTargetRegistry {
    private static final Map<String, Target> TARGETS = Map.ofEntries(
            Map.entry("STAFF", new Target("ct_gm.t_ct_staff", "staff_code")),
            Map.entry("DEPT", new Target("ct_gm.t_ct_dept", "dept_code")),
            Map.entry("UNIT", new Target("ct_gm.t_sys_unit", "unit_code")),
            Map.entry("SUPPLIER", new Target("ct_gm.t_sys_supplier", "sup_code")),
            Map.entry("STYLE", new Target("ct_gm.t_sys_style", "style_code")),
            Map.entry("MATERIAL", new Target("ct_gm.t_sys_material", "mat_code")),
            Map.entry("PROCESS", new Target("ct_gm.t_sys_process", "process_code")),
            Map.entry("ORDER", new Target("ct_gm.t_od_order", "order_no")),
            Map.entry("PURCHASE", new Target("ct_gm.t_pu_order", "po_no")),
            Map.entry("SAMPLE", new Target("ct_gm.t_sm_sample", "sample_no")),
            Map.entry("MISC", new Target("ct_gm.t_ms_order", "misc_no")),
            Map.entry("PROD_PLAN", new Target("ct_gm.t_pl_prod_plan", "prod_plan_no")),
            Map.entry("PROD_TASK", new Target("ct_gm.t_pl_task", "task_no")),
            Map.entry("FIN_SETTLE", new Target("ct_gm.t_fin_settle", "settle_no")),
            Map.entry("FIN_FLOW", new Target("ct_gm.t_fin_flow", "flow_no")),
            Map.entry("PIECE_REPORT", new Target("ct_gm.t_pr_report", "report_no")),
            Map.entry("WAGE_BILL", new Target("ct_gm.t_wg_bill", "bill_no")),
            Map.entry("WAGE_BATCH", new Target("ct_gm.t_wg_payment_batch", "batch_no")),
            Map.entry("WAREHOUSE", new Target("ct_gm.t_st_warehouse", "wh_code")),
            Map.entry("STOCK_FLOW", new Target("ct_gm.t_st_flow", "flow_no")),
            Map.entry("DEVICE", new Target("ct_gm.t_dv_device", "device_no")),
            Map.entry("DEVICE_LOG", new Target("ct_gm.t_dv_device_log", "log_no"))
    );

    private final JdbcTemplate jdbcTemplate;

    /**
     * 注入 JdbcTemplate，用于校验目标业务表中的编码是否已存在。
     */
    public CodeTargetRegistry(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 检查指定业务类型和编码是否已存在于目标业务表中。
     */
    public boolean exists(String bizType, String code) {
        Target target = TARGETS.get(bizType);
        if (target == null) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM " + target.tableName() + " WHERE " + target.columnName() + "=? AND is_del=0";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, code);
        return count != null && count > 0;
    }

    private record Target(String tableName, String columnName) {}
}
