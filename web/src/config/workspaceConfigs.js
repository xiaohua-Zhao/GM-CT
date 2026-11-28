import { crudConfigs } from './crudConfigs'
import { operationsConfigs } from './operationsConfigs'

const yesNoOptions = [{ label: '是', value: 1 }, { label: '否', value: 0 }]
const orderTypeOptions = [{ label: '内销订单', value: 1 }, { label: '加工单', value: 2 }, { label: '返单', value: 3 }]
const orderStatusOptions = [
  { label: '初始', value: 1 }, { label: '待生产', value: 2 }, { label: '生产中', value: 3 },
  { label: '部分交付', value: 4 }, { label: '已完成', value: 5 }, { label: '延期', value: 6 },
  { label: '暂停', value: 7 }, { label: '取消', value: 8 }
]
const purchaseTypeOptions = [
  { label: '面料', value: 1 }, { label: '辅料', value: 2 }, { label: '包装', value: 3 }, { label: '外协', value: 4 }
]
const purchaseStatusOptions = [
  { label: '待审', value: 1 }, { label: '已审', value: 2 }, { label: '部分到货', value: 3 },
  { label: '已到货', value: 4 }, { label: '已入库', value: 5 }, { label: '取消', value: 6 }
]
const miscOrderTypeOptions = [
  { label: '设备采购', value: 1 }, { label: '固定资产', value: 2 }, { label: '装修工程', value: 3 },
  { label: '消防检查', value: 4 }, { label: '年检认证', value: 5 }, { label: '其他费用', value: 6 }
]
const miscRefTypeOptions = [
  { label: '不关联', value: 0 }, { label: '订单', value: 1 }, { label: '采购单', value: 2 }
]
const miscStatusOptions = [
  { label: '草稿', value: 1 }, { label: '审批中', value: 2 }, { label: '执行中', value: 3 },
  { label: '已完成', value: 4 }, { label: '取消', value: 5 }
]
const prodPlanStatusOptions = [
  { label: '待下达', value: 1 }, { label: '已下达', value: 2 }, { label: '生产中', value: 3 },
  { label: '已完工', value: 4 }, { label: '暂停', value: 5 }, { label: '取消', value: 6 }
]
const taskStatusOptions = [
  { label: '待下发', value: 1 }, { label: '已下发', value: 2 }, { label: '组长已领取', value: 3 },
  { label: '生产中', value: 4 }, { label: '已完成', value: 5 }, { label: '暂停', value: 6 },
  { label: '取消', value: 7 }
]
const processStageOptions = [
  { label: '裁剪', value: 1 }, { label: '缝制', value: 2 }, { label: '剪线', value: 3 },
  { label: '整烫', value: 4 }, { label: '包装', value: 5 }, { label: '质检', value: 6 }
]
const qcStatusOptions = [
  { label: '未检', value: 0 }, { label: '待检', value: 1 }, { label: '已检合格', value: 2 },
  { label: '已检不合格', value: 3 }, { label: '返修中', value: 4 }
]
const qcTypeOptions = [
  { label: '首件', value: 1 }, { label: '巡检', value: 2 }, { label: '终检', value: 3 },
  { label: '出货抽检', value: 4 }, { label: '客户验货', value: 5 }
]

const taskColumns = [
  { prop: 'taskNo', label: '工序任务号', width: 155 },
  { prop: 'prodPlanId', label: '生产计划', width: 155, type: 'lookup', lookup: 'prodPlans' },
  { prop: 'processStage', label: '工序', width: 85, type: 'option', options: processStageOptions },
  { prop: 'deptId', label: '执行组', width: 130, type: 'lookup', lookup: 'depts' },
  { prop: 'leaderId', label: '组长', width: 120, type: 'lookup', lookup: 'staffs' },
  { prop: 'status', label: '任务状态', width: 105, type: 'option', options: taskStatusOptions },
  { prop: 'qtyPlan', label: '计划数量', width: 95 },
  { prop: 'qtyDone', label: '完成数量', width: 95 },
  { prop: 'startDatePlan', label: '计划开始', width: 110 },
  { prop: 'endDatePlan', label: '计划结束', width: 110 },
  { prop: 'qcStatus', label: '质检状态', width: 100, type: 'option', options: qcStatusOptions },
  { prop: 'updatedAt', label: '更新时间', width: 160, type: 'datetime' }
]

const taskFields = [
  { prop: 'taskNo', label: '工序任务号', type: 'input' },
  { prop: 'prodPlanId', label: '生产计划', type: 'number', required: true },
  { prop: 'seqNo', label: '拆分序号', type: 'number', default: 1 },
  { prop: 'processStage', label: '工序阶段', type: 'select', options: processStageOptions, default: 1 },
  { prop: 'deptId', label: '执行组', type: 'number' },
  { prop: 'leaderId', label: '组长', type: 'number' },
  { prop: 'status', label: '任务状态', type: 'select', options: taskStatusOptions, default: 1 },
  { prop: 'qtyPlan', label: '计划数量', type: 'number', default: 0, precision: 2 },
  { prop: 'qtyDone', label: '完成数量', type: 'number', default: 0, precision: 2 },
  { prop: 'startDatePlan', label: '计划开始日期', type: 'date' },
  { prop: 'endDatePlan', label: '计划结束日期', type: 'date' },
  { prop: 'qcNeed', label: '需要质检', type: 'select', options: yesNoOptions, default: 1 },
  { prop: 'qcStatus', label: '质检状态', type: 'select', options: qcStatusOptions, default: 0 },
  { prop: 'qcType', label: '质检类型', type: 'select', options: qcTypeOptions, default: 2 },
  { prop: 'qcQtyCheck', label: '累计抽检量', type: 'number', default: 0, precision: 2 },
  { prop: 'qcQtyPass', label: '累计合格量', type: 'number', default: 0, precision: 2 },
  { prop: 'qcQtyReject', label: '累计不合格量', type: 'number', default: 0, precision: 2 },
  { prop: 'qcInspectorId', label: '责任质检员', type: 'number' },
  { prop: 'qcLastDate', label: '最近检验日期', type: 'date' },
  { prop: 'issuedBy', label: '任务下发人', type: 'number' },
  { prop: 'issuedAt', label: '任务下发时间', type: 'datetime' },
  { prop: 'claimedBy', label: '任务领取人', type: 'number' },
  { prop: 'claimedAt', label: '任务领取时间', type: 'datetime' },
  { prop: 'actualStartAt', label: '实际开始时间', type: 'datetime' },
  { prop: 'actualFinishAt', label: '实际完成时间', type: 'datetime' },
  { prop: 'remark', label: '备注', type: 'textarea' }
]

const taskStageDefinitions = [
  { key: 'cutting-tasks', label: '前道裁剪', stage: 1, description: '维护裁剪工序任务、执行组和计划数量' },
  { key: 'sewing-tasks', label: '后道缝制', stage: 2, description: '维护缝制工序任务、组长和生产进度' },
  { key: 'trimming-tasks', label: '剪线工序', stage: 3, description: '维护剪线工序任务和完成数量' },
  { key: 'ironing-tasks', label: '整烫工序', stage: 4, description: '维护整烫工序任务和质检信息' },
  { key: 'packing-tasks', label: '包装工序', stage: 5, description: '维护包装工序任务和完成情况' },
  { key: 'qc-tasks', label: '质检工序', stage: 6, description: '维护质检工序任务、质检状态和抽检数量' }
]

const extraBusinessConfigs = [
  {
    key: 'misc-orders', path: '/misc-orders', title: '其他订单', api: '/ms/orders', keywordField: 'miscNo',
    autoCodeField: 'miscNo', codeBizType: 'MISC', createLabel: '新增其他订单',
    description: '维护设备采购、固定资产、装修、消防年检和日常费用',
    lookupKeys: ['units', 'suppliers', 'staffs', 'depts'],
    columns: [
      { prop: 'miscNo', label: '其他单号', width: 150 }, { prop: 'title', label: '事由', minWidth: 180 },
      { prop: 'orderType', label: '单据类型', width: 110, type: 'option', options: miscOrderTypeOptions },
      { prop: 'refNo', label: '关联单号', width: 140 },
      { prop: 'supplierId', label: '供应商', width: 145, type: 'lookup', lookup: 'suppliers' },
      { prop: 'applicantId', label: '申请人', width: 120, type: 'lookup', lookup: 'staffs' },
      { prop: 'deptId', label: '申请部门', width: 130, type: 'lookup', lookup: 'depts' },
      { prop: 'applyDate', label: '申请日期', width: 110 },
      { prop: 'amount', label: '金额', width: 110, type: 'money' },
      { prop: 'amountTax', label: '价税合计', width: 110, type: 'money' },
      { prop: 'status', label: '状态', width: 100, type: 'option', options: miscStatusOptions },
      { prop: 'updatedAt', label: '更新时间', width: 160, type: 'datetime' }
    ],
    fields: [
      { prop: 'miscNo', label: '其他单号', type: 'input' },
      { prop: 'title', label: '事由', type: 'input', required: true },
      { prop: 'orderType', label: '单据类型', type: 'select', options: miscOrderTypeOptions, default: 1 },
      { prop: 'refType', label: '关联类型', type: 'select', options: miscRefTypeOptions, default: 0 },
      { prop: 'refNo', label: '关联单号', type: 'input' },
      { prop: 'unitId', label: '往来单位', type: 'number' },
      { prop: 'supplierId', label: '供应商', type: 'number' },
      { prop: 'applicantId', label: '申请人', type: 'number' },
      { prop: 'deptId', label: '申请部门', type: 'number' },
      { prop: 'applyDate', label: '申请日期', type: 'date', required: true },
      { prop: 'contractNo', label: '合同或发票号', type: 'input' },
      { prop: 'amount', label: '不含税金额', type: 'number', default: 0, precision: 2 },
      { prop: 'taxRate', label: '税率', type: 'number', default: 0.13, precision: 4 },
      { prop: 'amountTax', label: '价税合计', type: 'number', default: 0, precision: 2 },
      { prop: 'payDatePlan', label: '预计付款日期', type: 'date' },
      { prop: 'expectDate', label: '预计完成日期', type: 'date' },
      { prop: 'finishDate', label: '实际完成日期', type: 'date' },
      { prop: 'nextDueDate', label: '下次到期日期', type: 'date' },
      { prop: 'certNo', label: '证书或验收单号', type: 'input' },
      { prop: 'status', label: '单据状态', type: 'select', options: miscStatusOptions, default: 1 },
      { prop: 'attachCount', label: '附件数量', type: 'number', default: 0 },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'misc-order-items', path: '/misc-order-items', title: '其他订单明细', api: '/ms/order-items',
    keywordField: 'itemName', lookupKeys: ['miscOrders', 'depts'],
    description: '维护其他订单项目、规格、数量、单价和固定资产标识',
    columns: [
      { prop: 'msOrderId', label: '其他订单', width: 170, type: 'lookup', lookup: 'miscOrders' },
      { prop: 'seqNo', label: '行号', width: 75 }, { prop: 'itemName', label: '项目名称', minWidth: 170 },
      { prop: 'spec', label: '规格型号', width: 140 }, { prop: 'unit', label: '单位', width: 75 },
      { prop: 'qty', label: '数量', width: 90 }, { prop: 'price', label: '单价', width: 100, type: 'money' },
      { prop: 'amount', label: '金额', width: 110, type: 'money' },
      { prop: 'assetFlag', label: '计入固资', width: 90, type: 'yesNo' },
      { prop: 'useDeptId', label: '使用部门', width: 130, type: 'lookup', lookup: 'depts' }
    ],
    fields: [
      { prop: 'msOrderId', label: '其他订单', type: 'number', required: true },
      { prop: 'deptId', label: '归属部门', type: 'number' },
      { prop: 'seqNo', label: '行号', type: 'number' },
      { prop: 'itemName', label: '项目名称', type: 'input', required: true },
      { prop: 'spec', label: '规格型号', type: 'input' },
      { prop: 'unit', label: '单位', type: 'input', default: '项' },
      { prop: 'qty', label: '数量', type: 'number', default: 1, precision: 2 },
      { prop: 'price', label: '单价', type: 'number', default: 0, precision: 4 },
      { prop: 'amount', label: '金额', type: 'number', default: 0, precision: 2 },
      { prop: 'assetFlag', label: '是否计入固定资产', type: 'select', options: yesNoOptions, default: 0 },
      { prop: 'useDeptId', label: '使用部门', type: 'number' },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'prod-plans', path: '/prod-plans', title: '生产计划', api: '/pl/plans', keywordField: 'prodPlanNo',
    autoCodeField: 'prodPlanNo', codeBizType: 'PROD_PLAN', createLabel: '新增生产计划',
    description: '按订单创建生产计划，维护开工、完工日期和下达状态',
    lookupKeys: ['orders', 'depts', 'staffs'],
    columns: [
      { prop: 'prodPlanNo', label: '生产计划单号', width: 160 },
      { prop: 'orderId', label: '订单', width: 150, type: 'lookup', lookup: 'orders' },
      { prop: 'deptId', label: '归属部门', width: 130, type: 'lookup', lookup: 'depts' },
      { prop: 'prodQty', label: '生产数量', width: 95 },
      { prop: 'startDate', label: '计划开工', width: 110 }, { prop: 'endDate', label: '计划完工', width: 110 },
      { prop: 'status', label: '计划状态', width: 100, type: 'option', options: prodPlanStatusOptions },
      { prop: 'plannerId', label: '计划人', width: 120, type: 'lookup', lookup: 'staffs' },
      { prop: 'releaseBy', label: '下达人', width: 120, type: 'lookup', lookup: 'staffs' },
      { prop: 'releaseTime', label: '下达时间', width: 165, type: 'datetime' }
    ],
    fields: [
      { prop: 'prodPlanNo', label: '生产计划单号', type: 'input' },
      { prop: 'orderId', label: '订单', type: 'number', required: true },
      { prop: 'deptId', label: '归属部门', type: 'number' },
      { prop: 'prodQty', label: '生产数量', type: 'number', default: 0, precision: 2 },
      { prop: 'startDate', label: '计划开工日期', type: 'date', required: true },
      { prop: 'endDate', label: '计划完工日期', type: 'date', required: true },
      { prop: 'status', label: '计划状态', type: 'select', options: prodPlanStatusOptions, default: 1 },
      { prop: 'plannerId', label: '计划人或生产主管', type: 'number' },
      { prop: 'releaseBy', label: '下达人', type: 'number' },
      { prop: 'releaseTime', label: '下达时间', type: 'datetime' },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'prod-tasks', path: '/prod-tasks', title: '全部工序任务', api: '/pl/tasks', keywordField: 'taskNo',
    autoCodeField: 'taskNo', codeBizType: 'PROD_TASK', createLabel: '新增工序任务',
    description: '维护全部生产工序任务、组长、数量、质检和状态',
    lookupKeys: ['prodPlans', 'depts', 'staffs'], columns: taskColumns, fields: taskFields
  },
  ...taskStageDefinitions.map(def => ({
    key: def.key,
    path: '/' + def.key,
    title: def.label,
    api: '/pl/tasks',
    keywordField: 'taskNo',
    autoCodeField: 'taskNo',
    codeBizType: 'PROD_TASK',
    createLabel: '新增' + def.label.replace('工序', '') + '任务',
    description: def.description,
    lookupKeys: ['prodPlans', 'depts', 'staffs'],
    columns: taskColumns,
    fields: taskFields.filter(field => field.prop !== 'processStage'),
    fixedFilters: { stage: def.stage },
    fixedValues: { processStage: def.stage }
  }))
]

const businessConfigs = [
  {
    key: 'orders', path: '/orders', title: '订单', api: '/od/orders', keywordField: 'orderNo',
    createMode: 'page', createPath: '/orders/new', createLabel: '新增订单', autoCodeField: 'orderNo', codeBizType: 'ORDER',
    detailPath: '/orders/:id', hideEdit: true,
    description: '维护客户订单、交期、订单金额和生产放行状态',
    lookupKeys: ['units', 'staffs', 'depts'],
    columns: [
      { prop: 'orderNo', label: '订单号', width: 150 }, { prop: 'unitId', label: '客户单位', width: 150, type: 'lookup', lookup: 'units' },
      { prop: 'orderType', label: '订单类型', width: 95, type: 'option', options: orderTypeOptions },
      { prop: 'salesmanId', label: '业务员', width: 120, type: 'lookup', lookup: 'staffs' },
      { prop: 'orderDate', label: '下单日期', width: 110 }, { prop: 'deliveryDatePlan', label: '预计交期', width: 110 },
      { prop: 'qtyTotal', label: '总数量', width: 90 }, { prop: 'amountTotal', label: '订单金额', width: 110, type: 'money' },
      { prop: 'amountTax', label: '价税合计', width: 110, type: 'money' },
      { prop: 'isProducible', label: '可生产', width: 85, type: 'yesNo' },
      { prop: 'status', label: '状态', width: 100, type: 'option', options: orderStatusOptions },
      { prop: 'updatedAt', label: '更新时间', width: 160, type: 'datetime' }
    ],
    fields: [
      { prop: 'orderNo', label: '订单号', type: 'input', required: true },
      { prop: 'unitId', label: '客户单位', type: 'number', required: true },
      { prop: 'orderType', label: '订单类型', type: 'select', options: orderTypeOptions, default: 1 },
      { prop: 'deptId', label: '归属部门', type: 'number' },
      { prop: 'salesmanId', label: '业务员', type: 'number' },
      { prop: 'contractNo', label: '合同号', type: 'input' },
      { prop: 'orderDate', label: '下单日期', type: 'input', required: true },
      { prop: 'startDate', label: '开始日期', type: 'input' },
      { prop: 'deliveryDatePlan', label: '预计交付日期', type: 'input', required: true },
      { prop: 'deliveryDateAct', label: '实际交付日期', type: 'input' },
      { prop: 'currency', label: '币种', type: 'input', default: 'CNY' },
      { prop: 'qtyTotal', label: '订单总数量', type: 'number', default: 0, precision: 2 },
      { prop: 'amountTotal', label: '订单金额', type: 'number', default: 0, precision: 2 },
      { prop: 'taxRate', label: '税率', type: 'number', default: 0.13, precision: 4 },
      { prop: 'amountTax', label: '价税合计', type: 'number', default: 0, precision: 2 },
      { prop: 'costPlan', label: '预计成本', type: 'number', default: 0, precision: 2 },
      { prop: 'costAct', label: '实际成本', type: 'number', default: 0, precision: 2 },
      { prop: 'depositAmount', label: '已收定金', type: 'number', default: 0, precision: 2 },
      { prop: 'payDatePlan', label: '预计首款日期', type: 'input' },
      { prop: 'isProducible', label: '允许生产', type: 'select', options: yesNoOptions, default: 0 },
      { prop: 'releaseTime', label: '放行时间', type: 'input' },
      { prop: 'status', label: '订单状态', type: 'select', options: orderStatusOptions, default: 1 },
      { prop: 'delayDays', label: '逾期天数', type: 'number', default: 0 },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'order-items', path: '/order-items', title: '订单明细', api: '/od/order-items', keywordField: 'styleCode',
    description: '维护订单款式、颜色、尺码、数量和单价',
    lookupKeys: ['orders', 'styles', 'depts'],
    columns: [
      { prop: 'orderId', label: '订单', width: 150, type: 'lookup', lookup: 'orders' },
      { prop: 'itemNo', label: '行号', width: 70 }, { prop: 'styleId', label: '款式', width: 150, type: 'lookup', lookup: 'styles' },
      { prop: 'styleCode', label: '款号', width: 110 }, { prop: 'color', label: '颜色', width: 90 }, { prop: 'size', label: '尺码', width: 80 },
      { prop: 'qty', label: '数量', width: 90 }, { prop: 'unitPrice', label: '单价', width: 100, type: 'money' },
      { prop: 'amount', label: '金额', width: 110, type: 'money' }, { prop: 'qtyShipped', label: '已发', width: 85 },
      { prop: 'deliveryDatePlan', label: '行交期', width: 110 }
    ],
    fields: [
      { prop: 'orderId', label: '订单', type: 'number', required: true },
      { prop: 'itemNo', label: '行号', type: 'number', default: 1 },
      { prop: 'styleId', label: '款式', type: 'number', required: true },
      { prop: 'styleCode', label: '款号快照', type: 'input', required: true },
      { prop: 'deptId', label: '归属部门', type: 'number' },
      { prop: 'color', label: '颜色', type: 'input' }, { prop: 'size', label: '尺码', type: 'input' },
      { prop: 'qty', label: '数量', type: 'number', default: 0, precision: 2 },
      { prop: 'unitPrice', label: '单价', type: 'number', default: 0, precision: 4 },
      { prop: 'amount', label: '金额', type: 'number', default: 0, precision: 2 },
      { prop: 'taxRate', label: '税率', type: 'number', default: 0.13, precision: 4 },
      { prop: 'deliveryDatePlan', label: '预计交付日期', type: 'input' },
      { prop: 'qtyShipped', label: '已发数量', type: 'number', default: 0, precision: 2 },
      { prop: 'techDesc', label: '工艺要求', type: 'textarea' }, { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'purchase-orders', path: '/purchase-orders', title: '采购订单', api: '/pu/orders', keywordField: 'poNo', autoCodeField: 'poNo', codeBizType: 'PURCHASE',
    createMode: 'page', createPath: '/purchases/new', createLabel: '新增采购订单', detailPath: '/purchases/:id', hideEdit: true,
    description: '维护采购供应商、采购类型、金额和到货进度',
    lookupKeys: ['suppliers', 'staffs', 'depts', 'orders'],
    columns: [
      { prop: 'poNo', label: '采购单号', width: 150 }, { prop: 'supplierId', label: '供应商', width: 160, type: 'lookup', lookup: 'suppliers' },
      { prop: 'orderId', label: '关联订单', width: 145, type: 'lookup', lookup: 'orders' },
      { prop: 'poType', label: '采购类型', width: 95, type: 'option', options: purchaseTypeOptions },
      { prop: 'poDate', label: '采购日期', width: 110 }, { prop: 'expectArriveDate', label: '预计到货', width: 110 },
      { prop: 'qtyTotal', label: '采购数量', width: 95 }, { prop: 'amount', label: '采购金额', width: 110, type: 'money' },
      { prop: 'qtyReceived', label: '已到货', width: 90 },
      { prop: 'status', label: '状态', width: 100, type: 'option', options: purchaseStatusOptions },
      { prop: 'buyerId', label: '采购员', width: 120, type: 'lookup', lookup: 'staffs' },
      { prop: 'updatedAt', label: '更新时间', width: 160, type: 'datetime' }
    ],
    fields: [
      { prop: 'poNo', label: '采购单号', type: 'input', required: true },
      { prop: 'supplierId', label: '供应商', type: 'number', required: true },
      { prop: 'orderId', label: '关联订单', type: 'number' },
      { prop: 'prodPlanId', label: '关联生产计划', type: 'number' },
      { prop: 'deptId', label: '归属部门', type: 'number' },
      { prop: 'poType', label: '采购类型', type: 'select', options: purchaseTypeOptions, default: 1 },
      { prop: 'poDate', label: '采购日期', type: 'input', required: true },
      { prop: 'expectArriveDate', label: '预计到货日期', type: 'input' },
      { prop: 'qtyTotal', label: '采购总数量', type: 'number', default: 0, precision: 2 },
      { prop: 'amount', label: '采购金额', type: 'number', default: 0, precision: 2 },
      { prop: 'taxRate', label: '税率', type: 'number', default: 0.13, precision: 4 },
      { prop: 'amountTax', label: '价税合计', type: 'number', default: 0, precision: 2 },
      { prop: 'qtyReceived', label: '已到货数量', type: 'number', default: 0, precision: 2 },
      { prop: 'status', label: '采购状态', type: 'select', options: purchaseStatusOptions, default: 1 },
      { prop: 'stockInNo', label: '汇总入库单号', type: 'input' },
      { prop: 'buyerId', label: '采购员', type: 'number' },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'purchase-items', path: '/purchase-items', title: '采购明细', api: '/pu/order-items', keywordField: 'part',
    description: '维护采购物料、使用部位、数量、价格和到货日期',
    lookupKeys: ['purchaseOrders', 'materials', 'depts'],
    columns: [
      { prop: 'poId', label: '采购单', width: 150, type: 'lookup', lookup: 'purchaseOrders' },
      { prop: 'materialId', label: '物料', width: 170, type: 'lookup', lookup: 'materials' },
      { prop: 'part', label: '使用部位', width: 105 }, { prop: 'unit', label: '单位', width: 70 },
      { prop: 'qty', label: '采购数量', width: 95 }, { prop: 'price', label: '单价', width: 100, type: 'money' },
      { prop: 'amount', label: '金额', width: 110, type: 'money' }, { prop: 'qtyReceived', label: '已到货', width: 90 },
      { prop: 'arriveDate', label: '到货日期', width: 110 }
    ],
    fields: [
      { prop: 'poId', label: '采购订单', type: 'number', required: true },
      { prop: 'materialId', label: '物料', type: 'number', required: true },
      { prop: 'deptId', label: '归属部门', type: 'number' },
      { prop: 'part', label: '使用部位', type: 'input', default: '整件' },
      { prop: 'unit', label: '单位', type: 'input', default: '米' },
      { prop: 'qty', label: '采购数量', type: 'number', default: 0, precision: 2 },
      { prop: 'price', label: '采购单价', type: 'number', default: 0, precision: 4 },
      { prop: 'amount', label: '采购金额', type: 'number', default: 0, precision: 2 },
      { prop: 'qtyReceived', label: '已到货数量', type: 'number', default: 0, precision: 2 },
      { prop: 'arriveDate', label: '到货日期', type: 'input' },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  ...extraBusinessConfigs
]

const configMap = Object.fromEntries([...crudConfigs, ...businessConfigs, ...operationsConfigs].map(config => [config.key, config]))
const statusColumn = { prop: 'status', label: '状态', width: 90, type: 'status' }
const updatedColumn = { prop: 'updatedAt', label: '更新时间', width: 160, type: 'datetime' }

const columnOverrides = {
  depts: [
    { prop: 'deptCode', label: '部门编码', width: 105 },
    { prop: 'deptName', label: '部门名称', minWidth: 150 },
    { prop: 'shortName', label: '简称', width: 100 },
    { prop: 'parentId', label: '上级部门', width: 130, type: 'lookup', lookup: 'depts' },
    { prop: 'leaderStaffId', label: '负责人', width: 130, type: 'lookup', lookup: 'staffs' },
    { prop: 'memberCount', label: '成员数', width: 85, type: 'compute', compute: 'deptMemberCount' },
    statusColumn,
    updatedColumn
  ],
  staffs: [
    { prop: 'staffCode', label: '工号', width: 105 },
    { prop: 'staffName', label: '姓名', width: 105 },
    { prop: 'loginName', label: '登录名', width: 115 },
    { prop: 'gender', label: '性别', width: 75, type: 'option', options: [{ label: '男', value: 1 }, { label: '女', value: 2 }] },
    { prop: 'phone', label: '电话', width: 130 },
    { prop: 'deptId', label: '所属部门', width: 130, type: 'lookup', lookup: 'depts' },
    { prop: 'post', label: '岗位', width: 120 },
    { prop: 'roleNames', label: '关联角色', minWidth: 160, type: 'compute', compute: 'staffRoleNames' },
    statusColumn
  ],
  roles: [
    { prop: 'roleCode', label: '角色编码', width: 130 },
    { prop: 'roleName', label: '角色名称', width: 135 },
    { prop: 'roleType', label: '角色类型', width: 105, type: 'option', options: [{ label: '系统内置', value: 1 }, { label: '业务角色', value: 2 }] },
    { prop: 'dataScope', label: '数据范围', width: 120, type: 'option', options: [{ label: '全部数据', value: 1 }, { label: '本部门及下级', value: 2 }, { label: '仅本人', value: 3 }, { label: '指定部门', value: 4 }] },
    { prop: 'inheritDept', label: '部门继承', width: 90, type: 'yesNo' },
    { prop: 'pageCount', label: '授权页面', width: 90, type: 'compute', compute: 'rolePageCount' },
    { prop: 'staffCount', label: '关联员工', width: 90, type: 'compute', compute: 'roleStaffCount' },
    statusColumn
  ],
  pages: [
    { prop: 'pageCode', label: '页面编码', width: 140 },
    { prop: 'pageName', label: '页面名称', minWidth: 150 },
    { prop: 'moduleCode', label: '所属模块', width: 110 },
    { prop: 'routePath', label: '访问路径', minWidth: 170 },
    { prop: 'pageType', label: '页面类型', width: 90 },
    { prop: 'isMenu', label: '显示菜单', width: 90, type: 'yesNo' },
    { prop: 'sortNo', label: '排序', width: 75 },
    statusColumn
  ],
  'staff-depts': [
    { prop: 'staffId', label: '人员', width: 150, type: 'lookup', lookup: 'staffs' },
    { prop: 'deptId', label: '部门', width: 150, type: 'lookup', lookup: 'depts' },
    { prop: 'isPrimary', label: '主部门', width: 90, type: 'yesNo' },
    { prop: 'joinDate', label: '加入日期', width: 120 },
    statusColumn,
    updatedColumn
  ],
  'staff-roles': [
    { prop: 'staffId', label: '人员', width: 160, type: 'lookup', lookup: 'staffs' },
    { prop: 'roleId', label: '角色', width: 150, type: 'lookup', lookup: 'roles' },
    { prop: 'grantType', label: '授予方式', width: 120, type: 'option', options: [{ label: '手工授予', value: 1 }, { label: '随部门继承', value: 2 }] },
    statusColumn,
    updatedColumn
  ],
  'dept-roles': [
    { prop: 'deptId', label: '部门', width: 160, type: 'lookup', lookup: 'depts' },
    { prop: 'roleId', label: '默认角色', width: 160, type: 'lookup', lookup: 'roles' },
    { prop: 'effectiveDate', label: '生效日期', width: 120 },
    statusColumn,
    updatedColumn
  ],
  permissions: [
    { prop: 'subjectType', label: '主体类型', width: 100, type: 'option', options: [{ label: '角色', value: 1 }, { label: '部门', value: 2 }, { label: '人员', value: 3 }] },
    { prop: 'subjectId', label: '授权对象', minWidth: 150, type: 'lookup' },
    { prop: 'pageId', label: '授权页面', minWidth: 150, type: 'lookup', lookup: 'pages' },
    { prop: 'actionCode', label: '操作权限', width: 100, type: 'option', options: [{ label: '查看', value: 'view' }, { label: '新增', value: 'add' }, { label: '编辑', value: 'edit' }, { label: '删除', value: 'delete' }, { label: '审核', value: 'approve' }, { label: '导出', value: 'export' }, { label: '打印', value: 'print' }] },
    { prop: 'operState', label: '状态', width: 90, type: 'option', options: [{ label: '授予', value: 1 }, { label: '收回', value: 0 }, { label: '待审核', value: 2 }] },
    { prop: 'grantSource', label: '授权来源', width: 105, type: 'option', options: [{ label: '手工配置', value: 1 }, { label: '部门继承', value: 2 }, { label: '默认开通', value: 3 }] },
    { prop: 'effTime', label: '生效时间', width: 160, type: 'datetime' },
    { prop: 'expireTime', label: '失效时间', width: 160, type: 'datetime' }
  ],
  units: [
    { prop: 'unitCode', label: '单位编码', width: 105 },
    { prop: 'unitName', label: '单位名称', minWidth: 170 },
    { prop: 'shortName', label: '简称', width: 105 },
    { prop: 'unitType', label: '类型', width: 90 },
    { prop: 'contact', label: '联系人', width: 105 },
    { prop: 'phone', label: '电话', width: 130 },
    statusColumn,
    updatedColumn
  ],
  suppliers: [
    { prop: 'supCode', label: '供应商编码', width: 115 },
    { prop: 'supName', label: '供应商名称', minWidth: 180 },
    { prop: 'category', label: '合作品类', width: 110 },
    { prop: 'contact', label: '联系人', width: 105 },
    { prop: 'phone', label: '电话', width: 130 },
    { prop: 'creditDays', label: '账期(天)', width: 85 },
    statusColumn,
    updatedColumn
  ],
  banks: [
    { prop: 'accountName', label: '账户名称', minWidth: 160 },
    { prop: 'bankName', label: '开户行', minWidth: 160 },
    { prop: 'accountNo', label: '银行账号', width: 180 },
    { prop: 'ownerType', label: '所属类型', width: 100, type: 'option', options: [{ label: '单位', value: 1 }, { label: '供应商', value: 2 }, { label: '本厂主体', value: 3 }, { label: '员工', value: 4 }] },
    { prop: 'ownerId', label: '所属对象', minWidth: 150, type: 'lookup' },
    { prop: 'isDefault', label: '默认账户', width: 90, type: 'yesNo' },
    statusColumn
  ],
  commons: [
    { prop: 'dictType', label: '字典类型', width: 120 },
    { prop: 'dictCode', label: '字典编码', width: 120 },
    { prop: 'dictName', label: '名称', minWidth: 150 },
    { prop: 'extValue', label: '扩展值', minWidth: 140 },
    { prop: 'sortNo', label: '排序', width: 75 },
    statusColumn,
    updatedColumn
  ],
  styles: [
    { prop: 'styleCode', label: '款号', width: 105 },
    { prop: 'styleName', label: '款式名称', minWidth: 150 },
    { prop: 'category', label: '品类', width: 100 },
    { prop: 'season', label: '季节', width: 90 },
    { prop: 'quotePrice', label: '报价', width: 100, type: 'money' },
    { prop: 'sampleFee', label: '样衣费', width: 100, type: 'money' },
    statusColumn,
    updatedColumn
  ],
  processes: [
    { prop: 'processCode', label: '工序编码', width: 110 },
    { prop: 'processName', label: '工序名称', minWidth: 150 },
    { prop: 'processType', label: '工序类型', width: 100, type: 'option', options: [{ label: '裁剪', value: 1 }, { label: '缝制', value: 2 }, { label: '剪线', value: 3 }, { label: '整烫', value: 4 }, { label: '包装', value: 5 }, { label: '质检', value: 6 }] },
    { prop: 'stdMinute', label: '标准工时', width: 100 },
    { prop: 'basePrice', label: '基础工价', width: 100, type: 'money' },
    { prop: 'isKey', label: '关键工序', width: 90, type: 'yesNo' },
    statusColumn
  ],
  materials: [
    { prop: 'matCode', label: '物料编码', width: 110 },
    { prop: 'matName', label: '物料名称', minWidth: 170 },
    { prop: 'category', label: '类别', width: 90, type: 'option', options: [{ label: '面料', value: 1 }, { label: '辅料', value: 2 }, { label: '包装', value: 3 }, { label: '其他', value: 4 }] },
    { prop: 'spec', label: '规格', width: 130 },
    { prop: 'unit', label: '单位', width: 70 },
    { prop: 'safetyQty', label: '安全库存', width: 100 },
    { prop: 'stdPrice', label: '标准单价', width: 100, type: 'money' },
    statusColumn
  ],
  'style-processes': [
    { prop: 'styleId', label: '款式', minWidth: 150, type: 'lookup', lookup: 'styles' },
    { prop: 'processId', label: '工序', minWidth: 130, type: 'lookup', lookup: 'processes' },
    { prop: 'seqNo', label: '顺序', width: 75 },
    { prop: 'stdMinute', label: '标准工时', width: 100 },
    { prop: 'piecePrice', label: '计件单价', width: 100, type: 'money' },
    { prop: 'difficultyRate', label: '难度系数', width: 95 },
    { prop: 'isOutsource', label: '可外协', width: 85, type: 'yesNo' },
    statusColumn
  ],
  'style-materials': [
    { prop: 'styleId', label: '款式', minWidth: 150, type: 'lookup', lookup: 'styles' },
    { prop: 'materialId', label: '物料', minWidth: 150, type: 'lookup', lookup: 'materials' },
    { prop: 'part', label: '使用部位', width: 105 },
    { prop: 'unit', label: '单位', width: 70 },
    { prop: 'unitConsume', label: '单耗', width: 90 },
    { prop: 'lossRate', label: '损耗率', width: 90 },
    { prop: 'isMain', label: '主料', width: 80, type: 'yesNo' }
  ],
  attachments: [
    { prop: 'fileName', label: '文件名', minWidth: 180 },
    { prop: 'bizType', label: '业务类型', width: 105, type: 'option', options: [{ label: '单位', value: 'unit' }, { label: '供应商', value: 'supplier' }, { label: '款式', value: 'style' }, { label: '物料', value: 'material' }, { label: '合同', value: 'contract' }] },
    { prop: 'category', label: '附件分类', width: 110 },
    { prop: 'fileSize', label: '文件大小', width: 100, type: 'fileSize' },
    { prop: 'versionNo', label: '版本', width: 70 },
    { prop: 'isPublic', label: '客户可见', width: 90, type: 'yesNo' },
    { prop: 'uploadTime', label: '上传时间', width: 160, type: 'datetime' },
    statusColumn
  ]
}

const fieldOptionOverrides = {
  roleType: {
    type: 'select',
    options: [{ label: '系统内置', value: 1 }, { label: '业务角色', value: 2 }]
  },
  accountType: {
    type: 'select',
    options: [{ label: '银行账户', value: 1 }, { label: '现金账户', value: 2 }, { label: '微信/支付宝', value: 3 }]
  },
  ownerType: {
    type: 'select',
    options: [{ label: '单位', value: 1 }, { label: '供应商', value: 2 }, { label: '本厂主体', value: 3 }, { label: '员工', value: 4 }]
  }
}

const autoCodeConfigs = {
  depts: { autoCodeField: 'deptCode', codeBizType: 'DEPT' },
  staffs: { autoCodeField: 'staffCode', codeBizType: 'STAFF' },
  units: { autoCodeField: 'unitCode', codeBizType: 'UNIT' },
  suppliers: { autoCodeField: 'supCode', codeBizType: 'SUPPLIER' },
  styles: { autoCodeField: 'styleCode', codeBizType: 'STYLE' },
  processes: { autoCodeField: 'processCode', codeBizType: 'PROCESS' },
  materials: { autoCodeField: 'matCode', codeBizType: 'MATERIAL' }
}

const titleOverrides = {
  staffs: '员工列表',
  commons: '公共信息管理'
}

const descriptionOverrides = {
  depts: '维护组织架构、上级部门和部门负责人',
  staffs: '维护员工档案、所属部门和岗位信息',
  roles: '维护角色、数据范围及相关员工授权',
  pages: '维护系统页面名称、访问路径和菜单状态',
  'staff-depts': '维护员工所属部门及主部门关系',
  'staff-roles': '维护员工多角色授予关系',
  'dept-roles': '维护部门默认角色和生效日期',
  permissions: '按角色、部门或人员配置页面操作权限',
  units: '维护客户、合作单位和本厂主体信息',
  suppliers: '维护供应商合作品类、联系人和账期',
  banks: '维护单位及供应商的对公银行账户',
  commons: '维护面料、辅料、颜色和尺码等公共字典',
  styles: '维护服装款式、报价和样衣费用',
  processes: '维护裁剪、缝制、整烫和包装等标准工序',
  materials: '维护面料、辅料和包装材料基础信息',
  'style-processes': '维护款式的工序顺序、工时和计件单价',
  'style-materials': '维护款式各部位的材料单耗及损耗率',
  attachments: '集中管理业务附件和版本信息'
}

const lookupKeys = {
  depts: ['depts', 'staffs', 'staffDepts'],
  staffs: ['depts', 'roles', 'staffRoles'],
  roles: ['pages', 'permissions', 'staffRoles'],
  pages: ['pages'],
  'staff-depts': ['staffs', 'depts'],
  'staff-roles': ['staffs', 'roles'],
  'dept-roles': ['depts', 'roles'],
  permissions: ['roles', 'depts', 'staffs', 'pages'],
  units: ['staffs'],
  suppliers: ['depts'],
  banks: ['units', 'suppliers'],
  styles: ['units'],
  processes: ['depts'],
  materials: ['suppliers'],
  'style-processes': ['styles', 'processes'],
  'style-materials': ['styles', 'materials'],
  attachments: ['depts', 'staffs']
}

export const workspaceDefinitions = {
  organization: {
    title: '员工与组织权限',
    description: '统一维护部门、页面、角色、员工以及多对多授权关系',
    tabs: [
      { key: 'depts', label: '部门管理', icon: 'OfficeBuilding' },
      { key: 'pages', label: '页面管理', icon: 'Document' },
      { key: 'roles', label: '角色管理', icon: 'UserFilled' },
      { key: 'staffs', label: '员工列表', icon: 'User' },
      { key: 'staff-depts', label: '人员部门关系', icon: 'Connection' },
      { key: 'staff-roles', label: '人员角色关系', icon: 'Key' },
      { key: 'dept-roles', label: '部门角色关系', icon: 'SetUp' },
      { key: 'permissions', label: '权限管理', icon: 'Tickets' }
    ]
  },
  orders: {
    title: '订单管理',
    description: '统一维护订单、交期和订单状态',
    tabs: [
      { key: 'orders', label: '订单列表', icon: 'Document' }
    ]
  },
  purchases: {
    title: '采购管理',
    description: '统一维护采购订单、采购明细、付款计划和到货进度',
    tabs: [
      { key: 'purchase-orders', label: '采购订单', icon: 'Goods' }
    ]
  },
  production: {
    title: '生产管理',
    description: '统一维护订单对应的生产计划',
    tabs: [
      { key: 'prod-plans', label: '生产计划', icon: 'Document' }
    ]
  },
  misc: {
    title: '其他订单管理',
    description: '维护采购、设备、固定资产、装修、消防年检和日常费用',
    tabs: [
      { key: 'misc-orders', label: '其他订单', icon: 'Tickets' },
      { key: 'misc-order-items', label: '订单明细', icon: 'List' }
    ]
  },
  finance: {
    title: '财务管理',
    description: '统一维护结算、资金流水、收付款计划和发票',
    tabs: [
      { key: 'finance-settles', label: '结算管理', icon: 'Tickets' },
      { key: 'finance-flows', label: '资金流水', icon: 'CreditCard' },
      { key: 'finance-plans', label: '收付款计划', icon: 'Document' },
      { key: 'finance-invoices', label: '发票管理', icon: 'Files' }
    ]
  },
  wage: {
    title: '计件工资',
    description: '统一维护报工流水、工资单、发放批次和发放明细',
    tabs: [
      { key: 'wage-reports', label: '报工流水', icon: 'List' },
      { key: 'wage-bills', label: '工资单', icon: 'Document' },
      { key: 'wage-batches', label: '发放批次', icon: 'Tickets' },
      { key: 'wage-payment-items', label: '发放明细', icon: 'CreditCard' }
    ]
  },
  warehouse: {
    title: '仓库管理',
    description: '统一维护仓库、库存和出入库流水',
    tabs: [
      { key: 'warehouses', label: '仓库管理', icon: 'OfficeBuilding' },
      { key: 'stocks', label: '库存管理', icon: 'Box' },
      { key: 'stock-flows', label: '出入库流水', icon: 'List' }
    ]
  },
  equipment: {
    title: '设备管理',
    description: '统一维护设备台账和保养维修记录',
    tabs: [
      { key: 'devices', label: '设备台账', icon: 'SetUp' },
      { key: 'device-logs', label: '保养维修', icon: 'Tickets' }
    ]
  },
  basic: {
    title: '基础数据管理',
    description: '统一维护单位、供应商、银行账户、款式、工序和物料等基础资料',
    tabs: [
      { key: 'units', label: '单位管理', icon: 'OfficeBuilding' },
      { key: 'suppliers', label: '供应商管理', icon: 'Goods' },
      { key: 'banks', label: '银行账户', icon: 'CreditCard' },
      { key: 'commons', label: '公共信息', icon: 'Collection' },
      { key: 'styles', label: '款式管理', icon: 'Postcard' },
      { key: 'processes', label: '工序管理', icon: 'SetUp' },
      { key: 'materials', label: '物料管理', icon: 'Box' },
      { key: 'style-processes', label: '款式工序', icon: 'List' },
      { key: 'style-materials', label: '款式用料', icon: 'Grid' },
      { key: 'attachments', label: '附件管理', icon: 'Files' },
      { key: 'code-rules', label: '编码规则', icon: 'Tickets' }
    ]
  }
}

// 按工作区标识和业务模块键读取页面配置。
export function getWorkspaceConfig(workspace, key) {
  const base = configMap[key]
  if (!base) return null
  return {
    ...base,
    title: titleOverrides[key] || base.title,
    description: descriptionOverrides[key] || base.description,
    columns: columnOverrides[key] || base.columns,
    fields: (base.fields || []).map(field => fieldOptionOverrides[field.prop] ? { ...field, ...fieldOptionOverrides[field.prop] } : field),
    ...(autoCodeConfigs[key] || {}),
    lookupKeys: lookupKeys[key] || []
  }
}

// 根据业务模块键生成对应工作区访问路径。
export function workspacePathForKey(key) {
  if (workspaceDefinitions.orders.tabs.some(tab => tab.key === key)) return '/orders'
  if (workspaceDefinitions.purchases.tabs.some(tab => tab.key === key)) return '/purchases'
  if (key === 'prod-plans') return '/production'
  if (key === 'cutting-tasks') return '/production/cutting'
  if (key === 'sewing-tasks') return '/production/sewing'
  if (key === 'trimming-tasks') return '/production/trimming'
  if (key === 'ironing-tasks') return '/production/ironing'
  if (key === 'packing-tasks') return '/production/packing'
  if (key === 'qc-tasks') return '/production/qc'
  if (key === 'prod-tasks') return '/production/tasks'
  if (workspaceDefinitions.production.tabs.some(tab => tab.key === key)) return '/production'
  if (workspaceDefinitions.misc.tabs.some(tab => tab.key === key)) return '/misc'
  if (workspaceDefinitions.finance.tabs.some(tab => tab.key === key)) return '/finance'
  if (workspaceDefinitions.wage.tabs.some(tab => tab.key === key)) return '/wage'
  if (workspaceDefinitions.warehouse.tabs.some(tab => tab.key === key)) return '/warehouse'
  if (workspaceDefinitions.equipment.tabs.some(tab => tab.key === key)) return '/equipment'
  return workspaceDefinitions.basic.tabs.some(tab => tab.key === key) ? '/basic' : '/organization'
}
