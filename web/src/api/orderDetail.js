import http from './http'

// 分页查询货物订单列表。
export const getOrderList = params => http.get('/od/order/list', { params })
// 获取订单流程详情聚合数据。
export const getOrderFlow = id => http.get('/od/order/' + id)
// 按当前明细预览款式用料和用工成本。
export const previewOrderCost = items => http.post('/od/order/cost-preview', items)
// 新建订单及全部明细。
export const createOrderFlow = data => http.post('/od/order', data)
// 编辑订单及全部明细。
export const updateOrderFlow = (id, data) => http.put('/od/order/' + id, data)
// 提交可做性评审。
export const reviewOrderFlow = (id, data) => http.post('/od/order/' + id + '/review', data)
// 暂停订单。
export const pauseOrderFlow = id => http.post('/od/order/' + id + '/pause')
// 恢复暂停订单。
export const resumeOrderFlow = id => http.post('/od/order/' + id + '/resume')
// 关闭订单。
export const closeOrderFlow = (id, data) => http.post('/od/order/' + id + '/close', data)

// 兼容旧页面：获取指定订单详情。
export const getOrder = id => http.get('/od/orders/' + id)
// 兼容旧页面：更新指定订单基础信息。
export const updateOrder = (id, data) => http.put('/od/orders/' + id, data)
// 查询指定订单的全部明细。
export const getOrderItems = orderId => http.get('/od/order-items/by-order/' + orderId)
// 新增订单明细。
export const createOrderItem = data => http.post('/od/order-items', data)
// 修改指定订单明细。
export const updateOrderItem = (id, data) => http.put('/od/order-items/' + id, data)
// 删除指定订单明细。
export const deleteOrderItem = id => http.delete('/od/order-items/' + id)
// 查询指定订单的收款计划。
export const getPlanList = orderId => http.get('/fin/plans/by-order/' + orderId)
// 新增收款计划。
export const createPlan = data => http.post('/fin/plans', data)
// 修改收款计划。
export const updatePlan = (id, data) => http.put('/fin/plans/' + id, data)
// 删除收款计划。
export const deletePlan = id => http.delete('/fin/plans/' + id)
// 查询指定订单关联的发票。
export const getInvoices = orderId => http.get('/fin/invoices/by-order/' + orderId)
