import http from './http'

// 获取指定采购订单详情。
export const getPurchaseOrder = id => http.get('/pu/orders/' + id)
// 更新指定采购订单基础信息。
export const updatePurchaseOrder = (id, data) => http.put('/pu/orders/' + id, data)
// 查询指定采购订单的全部明细。
export const getPurchaseItems = poId => http.get('/pu/order-items/by-po/' + poId)
// 新增采购明细。
export const createPurchaseItem = data => http.post('/pu/order-items', data)
// 修改指定采购明细。
export const updatePurchaseItem = (id, data) => http.put('/pu/order-items/' + id, data)
// 删除指定采购明细。
export const deletePurchaseItem = id => http.delete('/pu/order-items/' + id)
// 查询指定采购订单的付款计划。
export const getPurchasePlans = poId => http.get('/fin/plans/by-purchase/' + poId)
// 新增采购付款计划。
export const createPurchasePlan = data => http.post('/fin/plans', data)
// 修改采购付款计划。
export const updatePurchasePlan = (id, data) => http.put('/fin/plans/' + id, data)
// 删除采购付款计划。
export const deletePurchasePlan = id => http.delete('/fin/plans/' + id)
// 查询指定采购订单关联的发票。
export const getPurchaseInvoices = poId => http.get('/fin/invoices/by-purchase/' + poId)
