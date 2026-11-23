const TOKEN_KEY = 'ct_access_token'
const USER_KEY = 'ct_login_user'

// 保存登录令牌和用户信息，并按“记住登录”选择存储位置。
export function saveAuth(loginData, remember = true) {
  clearAuth()
  const storage = remember ? window.localStorage : window.sessionStorage
  storage.setItem(TOKEN_KEY, loginData.accessToken)
  storage.setItem(USER_KEY, JSON.stringify(loginData.user || {}))
}

// 读取当前浏览器中保存的 JWT 令牌。
export function getToken() {
  return window.localStorage.getItem(TOKEN_KEY) || window.sessionStorage.getItem(TOKEN_KEY)
}

// 读取本地保存的登录用户信息，并兼容旧数据格式。
export function getStoredUser() {
  const raw = window.localStorage.getItem(USER_KEY) || window.sessionStorage.getItem(USER_KEY)
  if (!raw) return null
  try {
    return JSON.parse(raw)
  } catch {
    return null
  }
}

// 清理登录令牌和用户缓存，退出当前会话。
export function clearAuth() {
  for (const storage of [window.localStorage, window.sessionStorage]) {
    storage.removeItem(TOKEN_KEY)
    storage.removeItem(USER_KEY)
  }
}

// 判断当前浏览器是否已保存有效登录状态。
export function isAuthenticated() {
  return Boolean(getToken())
}
