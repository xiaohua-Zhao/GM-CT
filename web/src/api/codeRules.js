import http from './http'

export function getCodeRulesPage(params) {
  return http.get('/sys/code-rules/page', { params })
}

export function previewCode(bizType) {
  return http.post('/sys/code-rules/preview', { bizType })
}

export function generateCode(bizType) {
  return http.post('/sys/code-rules/generate', { bizType })
}

export function resetCodeSequence(ruleId, data) {
  return http.post('/sys/code-rules/' + ruleId + '/reset', data)
}
