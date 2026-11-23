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
            Map.entry("SAMPLE", new Target("ct_gm.t_sm_sample", "sample_no"))
    );

    private final JdbcTemplate jdbcTemplate;

    public CodeTargetRegistry(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
