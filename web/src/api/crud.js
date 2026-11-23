import http from './http'

// 统一模块接口前缀，避免配置中重复写入 Axios 已配置的 /api。
function normalizeApi(api) {
  const value = String(api || '').trim()
  return value.startsWith('/api/') ? value.slice(4) : value
}

// 按模块接口分页查询通用 CRUD 数据。
export function getPage(api, params) {
  return http.get(`${normalizeApi(api)}/page`, { params })
}

// 调用模块接口新增一条数据。
export function createOne(api, data) {
  return http.post(normalizeApi(api), data)
}

// 调用模块接口按主键修改一条数据。
export function updateOne(api, id, data) {
  return http.put(`${normalizeApi(api)}/${id}`, data)
}

// 调用模块接口按主键删除一条数据。
export function deleteOne(api, id) {
  return http.delete(`${normalizeApi(api)}/${id}`)
}
