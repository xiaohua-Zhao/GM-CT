import http from './http'

// 调用系统健康检查接口，获取应用和数据库状态。
export function getHealth() {
  return http.get('/health')
}

// 上传附件并返回文件名称、访问地址和大小信息。
export function uploadAttachment(file) {
  const formData = new FormData()
  formData.append('file', file)
  return http.post('/files/upload', formData)
}
