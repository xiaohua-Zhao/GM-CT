import http from './http'
import { createOne, deleteOne, getPage, updateOne } from './crud'

// 分页查询生产计划。
export const getPlanPage = params => getPage('/pl/plans', params)
// 查询可创建生产计划的订单。
export const getEligibleOrders = params => http.get('/pl/plans/eligible-orders', { params })
// 新建生产计划。
export const createPlan = data => createOne('/pl/plans', data)
// 修改生产计划。
export const updatePlan = (id, data) => updateOne('/pl/plans', id, data)
// 删除生产计划。
export const deletePlan = id => deleteOne('/pl/plans', id)
