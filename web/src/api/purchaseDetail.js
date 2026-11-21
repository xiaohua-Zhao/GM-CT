import http from './http'

export const getPurchaseOrder = id => http.get('/pu/orders/' + id)
export const updatePurchaseOrder = (id, data) => http.put('/pu/orders/' + id, data)
export const getPurchaseItems = poId => http.get('/pu/order-items/by-po/' + poId)
export const createPurchaseItem = data => http.post('/pu/order-items', data)
export const updatePurchaseItem = (id, data) => http.put('/pu/order-items/' + id, data)
export const deletePurchaseItem = id => http.delete('/pu/order-items/' + id)
export const getPurchasePlans = poId => http.get('/fin/plans/by-purchase/' + poId)
export const createPurchasePlan = data => http.post('/fin/plans', data)
export const updatePurchasePlan = (id, data) => http.put('/fin/plans/' + id, data)
export const deletePurchasePlan = id => http.delete('/fin/plans/' + id)
export const getPurchaseInvoices = poId => http.get('/fin/invoices/by-purchase/' + poId)
