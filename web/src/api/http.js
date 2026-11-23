import axios from 'axios'
import { clearAuth, getToken } from '../utils/auth'

const REQUEST_ID_KEY = 'ct_last_request_id'

// 生成前端请求追踪 ID，用于关联后端访问日志。
function createRequestId() {
  if (window.crypto?.randomUUID) return window.crypto.randomUUID().replace(/-/g, '')
  return 'req-' + Date.now() + '-' + Math.random().toString(16).slice(2, 10)
}

const http = axios.create({
  baseURL: '/api',
  timeout: 15000
})

http.interceptors.request.use(config => {
  const token = getToken()
  const requestId = createRequestId()
  config.headers = config.headers || {}
  config.headers['X-Request-Id'] = requestId
  window.sessionStorage.setItem(REQUEST_ID_KEY, requestId)
  if (token) config.headers.Authorization = 'Bearer ' + token
  return config
})

http.interceptors.response.use(
  response => {
    const requestId = response.headers?.['x-request-id']
    if (requestId) window.sessionStorage.setItem(REQUEST_ID_KEY, requestId)
    return response
  },
  error => {
    const requestId = error.response?.headers?.['x-request-id']
    if (requestId) window.sessionStorage.setItem(REQUEST_ID_KEY, requestId)
    error.requestId = requestId || window.sessionStorage.getItem(REQUEST_ID_KEY)
    if (error.response?.status === 401 && window.location.pathname !== '/login') {
      clearAuth()
      const redirect = window.location.pathname + window.location.search
      window.location.replace('/login?redirect=' + encodeURIComponent(redirect))
    }
    return Promise.reject(error)
  }
)

export default http
