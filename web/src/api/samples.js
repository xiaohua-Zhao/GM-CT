import http from './http'
import { createOne, deleteOne, getPage, updateOne } from './crud'

// 分页查询样衣记录。
export const getSamplePage = params => getPage('/sm/samples', params)
// 查询可制作样衣的订单。
export const getEligibleSampleOrders = keyword => http.get('/sm/samples/eligible-orders', { params: { keyword } })
// 新建样衣。
export const createSample = data => createOne('/sm/samples', data)
// 修改样衣。
export const updateSample = (id, data) => updateOne('/sm/samples', id, data)
// 删除样衣。
export const deleteSample = id => deleteOne('/sm/samples', id)
// 提交客户确认。
export const sendSample = id => http.post('/sm/samples/' + id + '/send')
// 客户确认样衣。
export const confirmSample = (id, data) => http.post('/sm/samples/' + id + '/confirm', data)
// 查询样衣跟单记录。
export const getSampleLogs = id => http.get('/sm/samples/' + id + '/logs')
