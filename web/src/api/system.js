import http from './http'

export function getHealth() {
  return http.get('/health')
}

export function uploadAttachment(file) {
  const formData = new FormData()
  formData.append('file', file)
  return http.post('/files/upload', formData)
}
