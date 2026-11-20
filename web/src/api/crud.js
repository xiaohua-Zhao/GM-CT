import http from './http'

export function getPage(api, params) {
  return http.get(`${api}/page`, { params })
}

export function createOne(api, data) {
  return http.post(api, data)
}

export function updateOne(api, id, data) {
  return http.put(`${api}/${id}`, data)
}

export function deleteOne(api, id) {
  return http.delete(`${api}/${id}`)
}
