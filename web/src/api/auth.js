import http from './http'

// 调用后端登录接口，提交账号密码并返回 JWT 登录结果。
export function login(data) {
  return http.post('/auth/login', data)
}

// 调用后端接口获取当前 JWT 对应的登录用户信息。
export function getCurrentUser() {
  return http.get('/auth/me')
}

// 调用后端退出登录接口，结束当前会话。
export function logout() {
  return http.post('/auth/logout')
}
