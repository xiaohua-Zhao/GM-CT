import { getPage } from './crud'

export const lookupDefinitions = {
  depts: { api: '/ct/depts', label: row => row.deptName || row.shortName || row.deptCode },
  staffs: { api: '/ct/staffs', label: row => row.staffName + (row.staffCode ? '（' + row.staffCode + '）' : '') },
  roles: { api: '/ct/roles', label: row => row.roleName || row.roleCode },
  pages: { api: '/ct/pages', label: row => row.pageName || row.pageCode },
  units: { api: '/sys/units', label: row => row.unitName || row.unitCode },
  suppliers: { api: '/sys/suppliers', label: row => row.supName || row.supCode },
  banks: { api: '/sys/banks', label: row => row.accountName + (row.accountNo ? ' · ' + row.accountNo.slice(-4) : '') },
  styles: { api: '/sys/styles', label: row => [row.styleCode, row.styleName].filter(Boolean).join(' ') },
  processes: { api: '/sys/processes', label: row => row.processName || row.processCode },
  materials: { api: '/sys/materials', label: row => [row.matCode, row.matName].filter(Boolean).join(' ') },
  commons: { api: '/sys/commons', label: row => row.dictName || row.dictCode },
  staffDepts: { api: '/ct/staff-depts', label: row => '人员部门关系 #' + row.id },
  staffRoles: { api: '/ct/staff-roles', label: row => '人员角色关系 #' + row.id },
  deptRoles: { api: '/ct/dept-roles', label: row => '部门角色关系 #' + row.id },
  permissions: { api: '/ct/permissions', label: row => '权限 #' + row.id }
}

export const idLookupMap = {
  deptId: 'depts',
  parentId: 'depts',
  salesDeptId: 'depts',
  leaderStaffId: 'staffs',
  staffId: 'staffs',
  uploaderId: 'staffs',
  salesmanId: 'staffs',
  roleId: 'roles',
  pageId: 'pages',
  parentPageId: 'pages',
  styleId: 'styles',
  materialId: 'materials',
  processId: 'processes',
  unitId: 'units',
  supplierId: 'suppliers'
}

export async function loadLookupData(key) {
  const definition = lookupDefinitions[key]
  if (!definition) return []
  const response = await getPage(definition.api, { current: 1, size: 1000 })
  const payload = response.data
  if (payload?.code !== 0) throw new Error(payload?.message || '加载关联数据失败')
  return payload?.data?.records || []
}

export function lookupLabel(key, records, value) {
  if (value === null || value === undefined || value === '') return '—'
  if (Number(value) === 0) return '未指定'
  const record = (records || []).find(item => String(item.id) === String(value))
  if (!record) return '#' + value
  const definition = lookupDefinitions[key]
  return definition ? definition.label(record) : String(value)
}

export function optionLabel(key, record) {
  const definition = lookupDefinitions[key]
  return definition ? definition.label(record) : String(record.id)
}
