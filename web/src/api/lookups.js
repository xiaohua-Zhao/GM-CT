import { getPage } from './crud'

export const lookupDefinitions = {
  depts: { api: '/ct/depts', label: row => row.deptName || row.shortName || row.deptCode },
  staffs: { api: '/ct/staffs', label: row => row.staffName + (row.staffCode ? '（' + row.staffCode + '）' : '') },
  roles: { api: '/ct/roles', label: row => row.roleName || row.roleCode },
  pages: { api: '/ct/pages', label: row => row.pageName || row.pageCode },
  units: { api: '/sys/units', label: row => row.unitName || row.unitCode },
  suppliers: { api: '/sys/suppliers', label: row => row.supName || row.supCode },
  banks: { api: '/sys/banks', label: row => row.accountName + (row.accountNo ? ' · ' + row.accountNo.slice(-4) : '') },
  styles: { api: '/sys/styles', label: row => [row.styleCode, row.styleName].filter(Boolean).join(' ') },
  processes: { api: '/sys/processes', label: row => row.processName || row.processCode },
  materials: { api: '/sys/materials', label: row => [row.matCode, row.matName].filter(Boolean).join(' ') },
  commons: { api: '/sys/commons', label: row => row.dictName || row.dictCode },
  staffDepts: { api: '/ct/staff-depts', label: row => '人员部门关系 #' + row.id },
  staffRoles: { api: '/ct/staff-roles', label: row => '人员角色关系 #' + row.id },
  deptRoles: { api: '/ct/dept-roles', label: row => '部门角色关系 #' + row.id },
  permissions: { api: '/ct/permissions', label: row => '权限 #' + row.id },
  orders: { api: '/od/orders', label: row => row.orderNo },
  orderItems: { api: '/od/order-items', label: row => (row.styleCode || '明细') + ' / 行' + row.itemNo },
  purchaseOrders: { api: '/pu/orders', label: row => row.poNo },
  purchaseItems: { api: '/pu/order-items', label: row => '采购明细 #' + row.id },
  miscOrders: { api: '/ms/orders', label: row => [row.miscNo, row.title].filter(Boolean).join(' · ') },
  miscOrderItems: { api: '/ms/order-items', label: row => [row.itemName, row.spec].filter(Boolean).join(' · ') },
  prodPlans: { api: '/pl/plans', label: row => row.prodPlanNo },
  prodTasks: { api: '/pl/tasks', label: row => [row.taskNo, row.processStage].filter(Boolean).join(' · ') },
  settlements: { api: '/fin/settles', label: row => row.settleNo },
  financeFlows: { api: '/fin/flows', label: row => row.flowNo },
  financePlans: { api: '/fin/plans', label: row => row.planNo },
  financeInvoices: { api: '/fin/invoices', label: row => row.invoiceNo },
  wageReports: { api: '/wg/reports', label: row => row.reportNo },
  wageBills: { api: '/wg/bills', label: row => row.billNo },
  wageBatches: { api: '/wg/payment-batches', label: row => row.batchNo },
  wagePaymentItems: { api: '/wg/payment-items', label: row => row.billId + ' / ' + row.staffId },
  warehouses: { api: '/st/warehouses', label: row => [row.whCode, row.whName].filter(Boolean).join(' · ') },
  stocks: { api: '/st/stocks', label: row => [row.itemCode, row.itemName].filter(Boolean).join(' · ') },
  stockFlows: { api: '/st/flows', label: row => row.flowNo },
  devices: { api: '/dv/devices', label: row => [row.deviceNo, row.deviceName].filter(Boolean).join(' · ') },
  deviceLogs: { api: '/dv/device-logs', label: row => row.logNo }
}

export const idLookupMap = {
  deptId: 'depts',
  parentId: 'depts',
  salesDeptId: 'depts',
  leaderStaffId: 'staffs',
  staffId: 'staffs',
  uploaderId: 'staffs',
  salesmanId: 'staffs',
  roleId: 'roles',
  pageId: 'pages',
  parentPageId: 'pages',
  styleId: 'styles',
  materialId: 'materials',
  processId: 'processes',
  unitId: 'units',
  supplierId: 'suppliers',
  buyerId: 'staffs',
  orderId: 'orders',
  poId: 'purchaseOrders',
  msOrderId: 'miscOrders',
  prodPlanId: 'prodPlans',
  applicantId: 'staffs',
  plannerId: 'staffs',
  releaseBy: 'staffs',
  issuedBy: 'staffs',
  claimedBy: 'staffs',
  qcInspectorId: 'staffs',
  useDeptId: 'depts',
  confirmBy: 'staffs',
  operatorId: 'staffs',
  auditorId: 'staffs',
  managerId: 'staffs',
  keeperId: 'staffs',
  handlerId: 'staffs',
  laborSubmittedBy: 'staffs',
  financeConfirmedBy: 'staffs',
  approvedBy: 'staffs',
  paidBy: 'staffs',
  leaderConfirmedBy: 'staffs',
  laborReviewedBy: 'staffs',
  submittedToFinanceBy: 'staffs',
  financeReviewedBy: 'staffs',
  warehouseId: 'warehouses',
  taskId: 'prodTasks',
  finSettleId: 'settlements',
  finPlanId: 'financePlans',
  finFlowId: 'financeFlows',
  accountId: 'banks',
  paymentBatchId: 'wageBatches',
  billId: 'wageBills',
  deviceId: 'devices',
  sourceFlowId: 'financeFlows'
}

// 加载指定业务字典的关联下拉选项，最多获取 1000 条。
export async function loadLookupData(key) {
  const definition = lookupDefinitions[key]
  if (!definition) return []
  const response = await getPage(definition.api, { current: 1, size: 1000 })
  const payload = response.data
  if (payload?.code !== 0) throw new Error(payload?.message || '加载关联数据失败')
  return payload?.data?.records || []
}

// 按记录 ID 查找并格式化关联数据展示文本。
export function lookupLabel(key, records, value) {
  if (value === null || value === undefined || value === '') return '—'
  if (Number(value) === 0) return '未指定'
  const record = (records || []).find(item => String(item.id) === String(value))
  if (!record) return '#' + value
  const definition = lookupDefinitions[key]
  return definition ? definition.label(record) : String(value)
}

// 把关联记录转换为下拉框显示的文本。
export function optionLabel(key, record) {
  const definition = lookupDefinitions[key]
  return definition ? definition.label(record) : String(record.id)
}
