# 数据库说明

数据库统一使用：

```text
ct_gm
```

基础 CRUD 使用正式设计表：

- 组织权限模块包：`com.chengtai.fashionmgmt.module.ct`
  - `t_ct_dept`
  - `t_ct_staff`
  - `t_ct_role`
  - `t_ct_page`
- 基础资料模块包：`com.chengtai.fashionmgmt.module.sys`
  - `t_sys_unit`
  - `t_sys_supplier`
  - `t_sys_bank`
  - `t_sys_common`
  - `t_sys_style`
  - `t_sys_process`
  - `t_sys_material`

所有表名均使用 `t_模块_表名` 前缀。正式建表结构以 `docs/数据库设计/成泰服装管理系统_DDL.sql` 为准。
