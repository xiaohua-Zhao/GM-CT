import http from './http'

// 分页查询编码规则列表。
export function getCodeRulesPage(params) {
  return http.get('/sys/code-rules/page', { params })
}

// 预览指定业务类型的下一个编码，不占用序列号。
export function previewCode(bizType) {
  return http.post('/sys/code-rules/preview', { bizType })
}

// 正式生成并占用一个编码序列号。
export function generateCode(bizType) {
  return http.post('/sys/code-rules/generate', { bizType })
}

// 按指定下一序号重置编码规则序列。
export function resetCodeSequence(ruleId, data) {
  return http.post('/sys/code-rules/' + ruleId + '/reset', data)
}
