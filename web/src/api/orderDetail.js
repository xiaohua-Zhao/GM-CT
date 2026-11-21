import http from './http'

export const getOrder = id => http.get('/od/orders/' + id)
export const updateOrder = (id, data) => http.put('/od/orders/' + id, data)
export const getOrderItems = orderId => http.get('/od/order-items/by-order/' + orderId)
export const createOrderItem = data => http.post('/od/order-items', data)
export const updateOrderItem = (id, data) => http.put('/od/order-items/' + id, data)
export const deleteOrderItem = id => http.delete('/od/order-items/' + id)
export const getPlanList = orderId => http.get('/fin/plans/by-order/' + orderId)
export const createPlan = data => http.post('/fin/plans', data)
export const updatePlan = (id, data) => http.put('/fin/plans/' + id, data)
export const deletePlan = id => http.delete('/fin/plans/' + id)
export const getInvoices = orderId => http.get('/fin/invoices/by-order/' + orderId)
