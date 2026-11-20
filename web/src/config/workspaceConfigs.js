import { crudConfigs } from './crudConfigs'

const configMap = Object.fromEntries(crudConfigs.map(config => [config.key, config]))
const statusColumn = { prop: 'status', label: '状态', width: 90, type: 'status' }
const updatedColumn = { prop: 'updatedAt', label: '更新时间', width: 160, type: 'datetime' }

const columnOverrides = {
  depts: [
    { prop: 'deptCode', label: '部门编码', width: 105 },
    { prop: 'deptName', label: '部门名称', minWidth: 150 },
    { prop: 'shortName', label: '简称', width: 100 },
    { prop: 'parentId', label: '上级部门', width: 130, type: 'lookup', lookup: 'depts' },
    { prop: 'leaderStaffId', label: '负责人', width: 130, type: 'lookup', lookup: 'staffs' },
    { prop: 'memberCount', label: '成员数', width: 85, type: 'compute', compute: 'deptMemberCount' },
    statusColumn,
    updatedColumn
  ],
  staffs: [
    { prop: 'staffCode', label: '工号', width: 105 },
    { prop: 'staffName', label: '姓名', width: 105 },
    { prop: 'loginName', label: '登录名', width: 115 },
    { prop: 'gender', label: '性别', width: 75, type: 'option', options: [{ label: '男', value: 1 }, { label: '女', value: 2 }] },
    { prop: 'phone', label: '电话', width: 130 },
    { prop: 'deptId', label: '所属部门', width: 130, type: 'lookup', lookup: 'depts' },
    { prop: 'post', label: '岗位', width: 120 },
    { prop: 'roleNames', label: '关联角色', minWidth: 160, type: 'compute', compute: 'staffRoleNames' },
    statusColumn
  ],
  roles: [
    { prop: 'roleCode', label: '角色编码', width: 130 },
    { prop: 'roleName', label: '角色名称', width: 135 },
    { prop: 'roleType', label: '角色类型', width: 105, type: 'option', options: [{ label: '系统内置', value: 1 }, { label: '业务角色', value: 2 }] },
    { prop: 'dataScope', label: '数据范围', width: 120, type: 'option', options: [{ label: '全部数据', value: 1 }, { label: '本部门及下级', value: 2 }, { label: '仅本人', value: 3 }, { label: '指定部门', value: 4 }] },
    { prop: 'inheritDept', label: '部门继承', width: 90, type: 'yesNo' },
    { prop: 'pageCount', label: '授权页面', width: 90, type: 'compute', compute: 'rolePageCount' },
    { prop: 'staffCount', label: '关联员工', width: 90, type: 'compute', compute: 'roleStaffCount' },
    statusColumn
  ],
  pages: [
    { prop: 'pageCode', label: '页面编码', width: 140 },
    { prop: 'pageName', label: '页面名称', minWidth: 150 },
    { prop: 'moduleCode', label: '所属模块', width: 110 },
    { prop: 'routePath', label: '访问路径', minWidth: 170 },
    { prop: 'pageType', label: '页面类型', width: 90 },
    { prop: 'isMenu', label: '显示菜单', width: 90, type: 'yesNo' },
    { prop: 'sortNo', label: '排序', width: 75 },
    statusColumn
  ],
  'staff-depts': [
    { prop: 'staffId', label: '人员', width: 150, type: 'lookup', lookup: 'staffs' },
    { prop: 'deptId', label: '部门', width: 150, type: 'lookup', lookup: 'depts' },
    { prop: 'isPrimary', label: '主部门', width: 90, type: 'yesNo' },
    { prop: 'joinDate', label: '加入日期', width: 120 },
    statusColumn,
    updatedColumn
  ],
  'staff-roles': [
    { prop: 'staffId', label: '人员', width: 160, type: 'lookup', lookup: 'staffs' },
    { prop: 'roleId', label: '角色', width: 150, type: 'lookup', lookup: 'roles' },
    { prop: 'grantType', label: '授予方式', width: 120, type: 'option', options: [{ label: '手工授予', value: 1 }, { label: '随部门继承', value: 2 }] },
    statusColumn,
    updatedColumn
  ],
  'dept-roles': [
    { prop: 'deptId', label: '部门', width: 160, type: 'lookup', lookup: 'depts' },
    { prop: 'roleId', label: '默认角色', width: 160, type: 'lookup', lookup: 'roles' },
    { prop: 'effectiveDate', label: '生效日期', width: 120 },
    statusColumn,
    updatedColumn
  ],
  permissions: [
    { prop: 'subjectType', label: '主体类型', width: 100, type: 'option', options: [{ label: '角色', value: 1 }, { label: '部门', value: 2 }, { label: '人员', value: 3 }] },
    { prop: 'subjectId', label: '授权对象', minWidth: 150, type: 'lookup' },
    { prop: 'pageId', label: '授权页面', minWidth: 150, type: 'lookup', lookup: 'pages' },
    { prop: 'actionCode', label: '操作权限', width: 100, type: 'option', options: [{ label: '查看', value: 'view' }, { label: '新增', value: 'add' }, { label: '编辑', value: 'edit' }, { label: '删除', value: 'delete' }, { label: '审核', value: 'approve' }, { label: '导出', value: 'export' }, { label: '打印', value: 'print' }] },
    { prop: 'operState', label: '状态', width: 90, type: 'option', options: [{ label: '授予', value: 1 }, { label: '收回', value: 0 }, { label: '待审核', value: 2 }] },
    { prop: 'grantSource', label: '授权来源', width: 105, type: 'option', options: [{ label: '手工配置', value: 1 }, { label: '部门继承', value: 2 }, { label: '默认开通', value: 3 }] },
    { prop: 'effTime', label: '生效时间', width: 160, type: 'datetime' },
    { prop: 'expireTime', label: '失效时间', width: 160, type: 'datetime' }
  ],
  units: [
    { prop: 'unitCode', label: '单位编码', width: 105 },
    { prop: 'unitName', label: '单位名称', minWidth: 170 },
    { prop: 'shortName', label: '简称', width: 105 },
    { prop: 'unitType', label: '类型', width: 90 },
    { prop: 'contact', label: '联系人', width: 105 },
    { prop: 'phone', label: '电话', width: 130 },
    statusColumn,
    updatedColumn
  ],
  suppliers: [
    { prop: 'supCode', label: '供应商编码', width: 115 },
    { prop: 'supName', label: '供应商名称', minWidth: 180 },
    { prop: 'category', label: '合作品类', width: 110 },
    { prop: 'contact', label: '联系人', width: 105 },
    { prop: 'phone', label: '电话', width: 130 },
    { prop: 'creditDays', label: '账期(天)', width: 85 },
    statusColumn,
    updatedColumn
  ],
  banks: [
    { prop: 'accountName', label: '账户名称', minWidth: 160 },
    { prop: 'bankName', label: '开户行', minWidth: 160 },
    { prop: 'accountNo', label: '银行账号', width: 180 },
    { prop: 'ownerType', label: '所属类型', width: 100, type: 'option', options: [{ label: '单位', value: 1 }, { label: '供应商', value: 2 }, { label: '本厂主体', value: 3 }, { label: '员工', value: 4 }] },
    { prop: 'ownerId', label: '所属对象', minWidth: 150, type: 'lookup' },
    { prop: 'isDefault', label: '默认账户', width: 90, type: 'yesNo' },
    statusColumn
  ],
  commons: [
    { prop: 'dictType', label: '字典类型', width: 120 },
    { prop: 'dictCode', label: '字典编码', width: 120 },
    { prop: 'dictName', label: '名称', minWidth: 150 },
    { prop: 'extValue', label: '扩展值', minWidth: 140 },
    { prop: 'sortNo', label: '排序', width: 75 },
    statusColumn,
    updatedColumn
  ],
  styles: [
    { prop: 'styleCode', label: '款号', width: 105 },
    { prop: 'styleName', label: '款式名称', minWidth: 150 },
    { prop: 'category', label: '品类', width: 100 },
    { prop: 'season', label: '季节', width: 90 },
    { prop: 'quotePrice', label: '报价', width: 100, type: 'money' },
    { prop: 'sampleFee', label: '样衣费', width: 100, type: 'money' },
    statusColumn,
    updatedColumn
  ],
  processes: [
    { prop: 'processCode', label: '工序编码', width: 110 },
    { prop: 'processName', label: '工序名称', minWidth: 150 },
    { prop: 'processType', label: '工序类型', width: 100, type: 'option', options: [{ label: '裁剪', value: 1 }, { label: '缝制', value: 2 }, { label: '剪线', value: 3 }, { label: '整烫', value: 4 }, { label: '包装', value: 5 }, { label: '质检', value: 6 }] },
    { prop: 'stdMinute', label: '标准工时', width: 100 },
    { prop: 'basePrice', label: '基础工价', width: 100, type: 'money' },
    { prop: 'isKey', label: '关键工序', width: 90, type: 'yesNo' },
    statusColumn
  ],
  materials: [
    { prop: 'matCode', label: '物料编码', width: 110 },
    { prop: 'matName', label: '物料名称', minWidth: 170 },
    { prop: 'category', label: '类别', width: 90, type: 'option', options: [{ label: '面料', value: 1 }, { label: '辅料', value: 2 }, { label: '包装', value: 3 }, { label: '其他', value: 4 }] },
    { prop: 'spec', label: '规格', width: 130 },
    { prop: 'unit', label: '单位', width: 70 },
    { prop: 'safetyQty', label: '安全库存', width: 100 },
    { prop: 'stdPrice', label: '标准单价', width: 100, type: 'money' },
    statusColumn
  ],
  'style-processes': [
    { prop: 'styleId', label: '款式', minWidth: 150, type: 'lookup', lookup: 'styles' },
    { prop: 'processId', label: '工序', minWidth: 130, type: 'lookup', lookup: 'processes' },
    { prop: 'seqNo', label: '顺序', width: 75 },
    { prop: 'stdMinute', label: '标准工时', width: 100 },
    { prop: 'piecePrice', label: '计件单价', width: 100, type: 'money' },
    { prop: 'difficultyRate', label: '难度系数', width: 95 },
    { prop: 'isOutsource', label: '可外协', width: 85, type: 'yesNo' },
    statusColumn
  ],
  'style-materials': [
    { prop: 'styleId', label: '款式', minWidth: 150, type: 'lookup', lookup: 'styles' },
    { prop: 'materialId', label: '物料', minWidth: 150, type: 'lookup', lookup: 'materials' },
    { prop: 'part', label: '使用部位', width: 105 },
    { prop: 'unit', label: '单位', width: 70 },
    { prop: 'unitConsume', label: '单耗', width: 90 },
    { prop: 'lossRate', label: '损耗率', width: 90 },
    { prop: 'isMain', label: '主料', width: 80, type: 'yesNo' }
  ],
  attachments: [
    { prop: 'fileName', label: '文件名', minWidth: 180 },
    { prop: 'bizType', label: '业务类型', width: 105, type: 'option', options: [{ label: '单位', value: 'unit' }, { label: '供应商', value: 'supplier' }, { label: '款式', value: 'style' }, { label: '物料', value: 'material' }, { label: '合同', value: 'contract' }] },
    { prop: 'category', label: '附件分类', width: 110 },
    { prop: 'fileSize', label: '文件大小', width: 100, type: 'fileSize' },
    { prop: 'versionNo', label: '版本', width: 70 },
    { prop: 'isPublic', label: '客户可见', width: 90, type: 'yesNo' },
    { prop: 'uploadTime', label: '上传时间', width: 160, type: 'datetime' },
    statusColumn
  ]
}

const fieldOptionOverrides = {
  roleType: {
    type: 'select',
    options: [{ label: '系统内置', value: 1 }, { label: '业务角色', value: 2 }]
  },
  accountType: {
    type: 'select',
    options: [{ label: '银行账户', value: 1 }, { label: '现金账户', value: 2 }, { label: '微信/支付宝', value: 3 }]
  },
  ownerType: {
    type: 'select',
    options: [{ label: '单位', value: 1 }, { label: '供应商', value: 2 }, { label: '本厂主体', value: 3 }, { label: '员工', value: 4 }]
  }
}

const titleOverrides = {
  staffs: '员工列表',
  commons: '公共信息管理'
}

const descriptionOverrides = {
  depts: '维护组织架构、上级部门和部门负责人',
  staffs: '维护员工档案、所属部门和岗位信息',
  roles: '维护角色、数据范围及相关员工授权',
  pages: '维护系统页面名称、访问路径和菜单状态',
  'staff-depts': '维护员工所属部门及主部门关系',
  'staff-roles': '维护员工多角色授予关系',
  'dept-roles': '维护部门默认角色和生效日期',
  permissions: '按角色、部门或人员配置页面操作权限',
  units: '维护客户、合作单位和本厂主体信息',
  suppliers: '维护供应商合作品类、联系人和账期',
  banks: '维护单位及供应商的对公银行账户',
  commons: '维护面料、辅料、颜色和尺码等公共字典',
  styles: '维护服装款式、报价和样衣费用',
  processes: '维护裁剪、缝制、整烫和包装等标准工序',
  materials: '维护面料、辅料和包装材料基础信息',
  'style-processes': '维护款式的工序顺序、工时和计件单价',
  'style-materials': '维护款式各部位的材料单耗及损耗率',
  attachments: '集中管理业务附件和版本信息'
}

const lookupKeys = {
  depts: ['depts', 'staffs', 'staffDepts'],
  staffs: ['depts', 'roles', 'staffRoles'],
  roles: ['pages', 'permissions', 'staffRoles'],
  pages: ['pages'],
  'staff-depts': ['staffs', 'depts'],
  'staff-roles': ['staffs', 'roles'],
  'dept-roles': ['depts', 'roles'],
  permissions: ['roles', 'depts', 'staffs', 'pages'],
  units: ['staffs'],
  suppliers: ['depts'],
  banks: ['units', 'suppliers'],
  styles: ['units'],
  processes: ['depts'],
  materials: ['suppliers'],
  'style-processes': ['styles', 'processes'],
  'style-materials': ['styles', 'materials'],
  attachments: ['depts', 'staffs']
}

export const workspaceDefinitions = {
  organization: {
    title: '员工与组织权限',
    description: '统一维护部门、页面、角色、员工以及多对多授权关系',
    tabs: [
      { key: 'depts', label: '部门管理', icon: 'OfficeBuilding' },
      { key: 'pages', label: '页面管理', icon: 'Document' },
      { key: 'roles', label: '角色管理', icon: 'UserFilled' },
      { key: 'staffs', label: '员工列表', icon: 'User' },
      { key: 'staff-depts', label: '人员部门关系', icon: 'Connection' },
      { key: 'staff-roles', label: '人员角色关系', icon: 'Key' },
      { key: 'dept-roles', label: '部门角色关系', icon: 'SetUp' },
      { key: 'permissions', label: '权限管理', icon: 'Tickets' }
    ]
  },
  basic: {
    title: '基础数据管理',
    description: '统一维护单位、供应商、银行账户、款式、工序和物料等基础资料',
    tabs: [
      { key: 'units', label: '单位管理', icon: 'OfficeBuilding' },
      { key: 'suppliers', label: '供应商管理', icon: 'Goods' },
      { key: 'banks', label: '银行账户', icon: 'CreditCard' },
      { key: 'commons', label: '公共信息', icon: 'Collection' },
      { key: 'styles', label: '款式管理', icon: 'Postcard' },
      { key: 'processes', label: '工序管理', icon: 'SetUp' },
      { key: 'materials', label: '物料管理', icon: 'Box' },
      { key: 'style-processes', label: '款式工序', icon: 'List' },
      { key: 'style-materials', label: '款式用料', icon: 'Grid' },
      { key: 'attachments', label: '附件管理', icon: 'Files' }
    ]
  }
}

export function getWorkspaceConfig(workspace, key) {
  const base = configMap[key]
  if (!base) return null
  return {
    ...base,
    title: titleOverrides[key] || base.title,
    description: descriptionOverrides[key] || base.description,
    columns: columnOverrides[key] || base.columns,
    fields: (base.fields || []).map(field => fieldOptionOverrides[field.prop] ? { ...field, ...fieldOptionOverrides[field.prop] } : field),
    lookupKeys: lookupKeys[key] || []
  }
}

export function workspacePathForKey(key) {
  return workspaceDefinitions.basic.tabs.some(tab => tab.key === key) ? '/basic' : '/organization'
}
