const TOKEN_KEY = 'ct_access_token'
const USER_KEY = 'ct_login_user'

export function saveAuth(loginData, remember = true) {
  clearAuth()
  const storage = remember ? window.localStorage : window.sessionStorage
  storage.setItem(TOKEN_KEY, loginData.accessToken)
  storage.setItem(USER_KEY, JSON.stringify(loginData.user || {}))
}

export function getToken() {
  return window.localStorage.getItem(TOKEN_KEY) || window.sessionStorage.getItem(TOKEN_KEY)
}

export function getStoredUser() {
  const raw = window.localStorage.getItem(USER_KEY) || window.sessionStorage.getItem(USER_KEY)
  if (!raw) return null
  try {
    return JSON.parse(raw)
  } catch {
    return null
  }
}

export function clearAuth() {
  for (const storage of [window.localStorage, window.sessionStorage]) {
    storage.removeItem(TOKEN_KEY)
    storage.removeItem(USER_KEY)
  }
}

export function isAuthenticated() {
  return Boolean(getToken())
}
