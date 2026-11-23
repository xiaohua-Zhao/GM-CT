-- 财务、计件工资、仓库和设备 CRUD 的编码规则与页面登记。
INSERT INTO ct_gm.t_sys_code_rule
    (rule_code, rule_name, biz_type, target_table, target_column, prefix, date_format, `separator`, sequence_length, sequence_start, sequence_step, reset_cycle, allow_manual, enabled, remark, created_at, updated_at, is_del)
VALUES
    ('CODE_FIN_SETTLE','结算单号','FIN_SETTLE','t_fin_settle','settle_no','ST','yyyyMMdd','',3,1,1,'DAY',1,1,'结算单号自动编码',NOW(),NOW(),0),
    ('CODE_FIN_FLOW','财务流水号','FIN_FLOW','t_fin_flow','flow_no','FF','yyyyMMdd','',3,1,1,'DAY',1,1,'财务流水号自动编码',NOW(),NOW(),0),
    ('CODE_PIECE_REPORT','报工批次号','PIECE_REPORT','t_pr_report','report_no','RG','yyyyMMdd','',3,1,1,'DAY',1,1,'报工批次号自动编码',NOW(),NOW(),0),
    ('CODE_WAGE_BILL','工资单号','WAGE_BILL','t_wg_bill','bill_no','WG','yyyyMM','',3,1,1,'MONTH',1,1,'工资单号自动编码',NOW(),NOW(),0),
    ('CODE_WAGE_BATCH','工资发放批次号','WAGE_BATCH','t_wg_payment_batch','batch_no','WB','yyyyMM','',3,1,1,'MONTH',1,1,'工资发放批次号自动编码',NOW(),NOW(),0),
    ('CODE_WAREHOUSE','仓库编码','WAREHOUSE','t_st_warehouse','wh_code','WH',NULL,'',3,1,1,'NONE',1,1,'仓库编码自动编码',NOW(),NOW(),0),
    ('CODE_STOCK_FLOW','出入库单号','STOCK_FLOW','t_st_flow','flow_no','IO','yyyyMMdd','',4,1,1,'DAY',1,1,'出入库单号自动编码',NOW(),NOW(),0),
    ('CODE_DEVICE','设备编号','DEVICE','t_dv_device','device_no','DV',NULL,'',4,1,1,'NONE',1,1,'设备编号自动编码',NOW(),NOW(),0),
    ('CODE_DEVICE_LOG','设备记录单号','DEVICE_LOG','t_dv_device_log','log_no','DL','yyyyMMdd','',4,1,1,'DAY',1,1,'设备记录单号自动编码',NOW(),NOW(),0)
ON DUPLICATE KEY UPDATE rule_name=VALUES(rule_name), target_table=VALUES(target_table), target_column=VALUES(target_column), prefix=VALUES(prefix), date_format=VALUES(date_format), `separator`=VALUES(`separator`), sequence_length=VALUES(sequence_length), reset_cycle=VALUES(reset_cycle), enabled=VALUES(enabled), updated_at=NOW(), is_del=0;

INSERT INTO ct_gm.t_ct_page
    (page_code,page_name,module_code,route_path,page_type,is_menu,sort_no,status,remark,created_at,updated_at,is_del)
VALUES
    ('fin.settle','结算管理','finance','/finance?tab=finance-settles',1,1,220,1,'结算管理页面',NOW(),NOW(),0),
    ('fin.flow','资金流水','finance','/finance?tab=finance-flows',1,1,221,1,'资金流水页面',NOW(),NOW(),0),
    ('fin.plan','收付款计划','finance','/finance?tab=finance-plans',1,0,222,1,'收付款计划页面',NOW(),NOW(),0),
    ('fin.invoice','发票管理','finance','/finance?tab=finance-invoices',1,0,223,1,'发票管理页面',NOW(),NOW(),0),
    ('wage.report','报工流水','wage','/wage?tab=wage-reports',1,1,230,1,'报工流水页面',NOW(),NOW(),0),
    ('wage.bill','工资单','wage','/wage?tab=wage-bills',1,1,231,1,'工资单页面',NOW(),NOW(),0),
    ('wage.batch','工资发放批次','wage','/wage?tab=wage-batches',1,1,232,1,'工资发放批次页面',NOW(),NOW(),0),
    ('wage.payment','工资发放明细','wage','/wage?tab=wage-payment-items',1,0,233,1,'工资发放明细页面',NOW(),NOW(),0),
    ('st.warehouse','仓库管理','warehouse','/warehouse?tab=warehouses',1,1,240,1,'仓库管理页面',NOW(),NOW(),0),
    ('st.stock','库存管理','warehouse','/warehouse?tab=stocks',1,1,241,1,'库存管理页面',NOW(),NOW(),0),
    ('st.flow','出入库流水','warehouse','/warehouse?tab=stock-flows',1,1,242,1,'出入库流水页面',NOW(),NOW(),0),
    ('dv.device','设备台账','equipment','/equipment?tab=devices',1,1,250,1,'设备台账页面',NOW(),NOW(),0),
    ('dv.device-log','设备保养维修','equipment','/equipment?tab=device-logs',1,1,251,1,'设备保养维修页面',NOW(),NOW(),0)
ON DUPLICATE KEY UPDATE page_name=VALUES(page_name), module_code=VALUES(module_code), route_path=VALUES(route_path), page_type=VALUES(page_type), is_menu=VALUES(is_menu), sort_no=VALUES(sort_no), status=VALUES(status), updated_at=NOW(), is_del=0;
