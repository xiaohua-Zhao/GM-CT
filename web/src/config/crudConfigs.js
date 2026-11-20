const statusOptions = [{ label: '启用', value: 1 }, { label: '停用', value: 0 }]
const yesNoOptions = [{ label: '是', value: 1 }, { label: '否', value: 0 }]
const genderOptions = [{ label: '男', value: 1 }, { label: '女', value: 2 }]
const dataScopeOptions = [
  { label: '全部数据', value: 1 },
  { label: '本部门及下级', value: 2 },
  { label: '仅本人', value: 3 },
  { label: '指定部门', value: 4 }
]
const processTypeOptions = [
  { label: '裁剪', value: 1 }, { label: '缝制', value: 2 }, { label: '剪线', value: 3 },
  { label: '整烫', value: 4 }, { label: '包装', value: 5 }, { label: '质检', value: 6 }
]
const materialCategoryOptions = [
  { label: '面料', value: 1 }, { label: '辅料', value: 2 }, { label: '包装', value: 3 }, { label: '其他', value: 4 }
]

const commonColumns = [
  { prop: 'status', label: '状态', width: 90, type: 'status' },
  { prop: 'updatedAt', label: '更新时间', width: 170 }
]

export const crudConfigs = [
  {
    key: 'depts', path: '/ct/depts', title: '部门管理', group: '组织权限',
    api: '/ct/depts', keywordField: 'deptName', description: '操作 t_ct_dept，维护部门编码、层级和负责人',
    columns: [
      { prop: 'deptCode', label: '部门编码', width: 110 }, { prop: 'deptName', label: '部门名称', width: 160 },
      { prop: 'shortName', label: '简称', width: 100 }, { prop: 'parentId', label: '上级ID', width: 90 },
      { prop: 'leaderStaffId', label: '负责人ID', width: 100 }, { prop: 'sortNo', label: '排序', width: 80 }, ...commonColumns
    ],
    fields: [
      { prop: 'deptCode', label: '部门编码', type: 'input', required: true },
      { prop: 'deptName', label: '部门名称', type: 'input', required: true },
      { prop: 'shortName', label: '简称', type: 'input' },
      { prop: 'parentId', label: '上级部门ID', type: 'number' },
      { prop: 'deptPath', label: '部门路径', type: 'input', default: '/' },
      { prop: 'deptLevel', label: '层级', type: 'number', default: 1 },
      { prop: 'leaderStaffId', label: '负责人ID', type: 'number' },
      { prop: 'status', label: '状态', type: 'select', options: statusOptions, default: 1 },
      { prop: 'sortNo', label: '排序', type: 'number', default: 0 },
      { prop: 'effectiveDate', label: '生效日期', type: 'input' },
      { prop: 'expireDate', label: '失效日期', type: 'input' },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'staffs', path: '/ct/staffs', title: '人员管理', group: '组织权限',
    api: '/ct/staffs', keywordField: 'staffName', description: '操作 t_ct_staff，维护工号、岗位和部门',
    columns: [
      { prop: 'staffCode', label: '工号', width: 110 }, { prop: 'staffName', label: '姓名', width: 110 },
      { prop: 'loginName', label: '登录名', width: 120 }, { prop: 'gender', label: '性别', width: 80, type: 'option', options: genderOptions },
      { prop: 'phone', label: '电话', width: 140 }, { prop: 'deptId', label: '部门ID', width: 90 },
      { prop: 'post', label: '岗位', width: 120 }, ...commonColumns
    ],
    fields: [
      { prop: 'staffCode', label: '工号', type: 'input', required: true },
      { prop: 'staffName', label: '姓名', type: 'input', required: true },
      { prop: 'loginName', label: '登录名', type: 'input' },
      { prop: 'gender', label: '性别', type: 'select', options: genderOptions },
      { prop: 'phone', label: '电话', type: 'input' },
      { prop: 'deptId', label: '部门ID', type: 'number' },
      { prop: 'post', label: '岗位', type: 'input' },
      { prop: 'status', label: '状态', type: 'select', options: statusOptions, default: 1 },
      { prop: 'hireDate', label: '入职日期', type: 'input' },
      { prop: 'leaveDate', label: '离职日期', type: 'input' },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'roles', path: '/ct/roles', title: '角色管理', group: '组织权限',
    api: '/ct/roles', keywordField: 'roleName', description: '操作 t_ct_role，维护角色和数据范围',
    columns: [
      { prop: 'roleCode', label: '角色编码', width: 140 }, { prop: 'roleName', label: '角色名称', width: 140 },
      { prop: 'roleType', label: '类型', width: 80 }, { prop: 'dataScope', label: '数据范围', width: 110, type: 'option', options: dataScopeOptions },
      { prop: 'inheritDept', label: '部门继承', width: 90, type: 'yesNo' }, { prop: 'sortNo', label: '排序', width: 80 }, ...commonColumns
    ],
    fields: [
      { prop: 'roleCode', label: '角色编码', type: 'input', required: true },
      { prop: 'roleName', label: '角色名称', type: 'input', required: true },
      { prop: 'roleType', label: '角色类型', type: 'number', default: 2 },
      { prop: 'dataScope', label: '数据范围', type: 'select', options: dataScopeOptions, default: 2 },
      { prop: 'inheritDept', label: '是否部门继承', type: 'select', options: yesNoOptions, default: 1 },
      { prop: 'status', label: '状态', type: 'select', options: statusOptions, default: 1 },
      { prop: 'sortNo', label: '排序', type: 'number', default: 0 },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'pages', path: '/ct/pages', title: '页面管理', group: '组织权限',
    api: '/ct/pages', keywordField: 'pageName', description: '操作 t_ct_page，维护页面路由和菜单',
    columns: [
      { prop: 'pageCode', label: '页面编码', width: 160 }, { prop: 'pageName', label: '页面名称', width: 140 },
      { prop: 'moduleCode', label: '模块', width: 100 }, { prop: 'routePath', label: '路由', width: 180 },
      { prop: 'isMenu', label: '菜单', width: 80, type: 'yesNo' }, { prop: 'sortNo', label: '排序', width: 80 }, ...commonColumns
    ],
    fields: [
      { prop: 'pageCode', label: '页面编码', type: 'input', required: true },
      { prop: 'pageName', label: '页面名称', type: 'input', required: true },
      { prop: 'moduleCode', label: '模块编码', type: 'input', required: true },
      { prop: 'routePath', label: '路由', type: 'input' },
      { prop: 'pageType', label: '页面类型', type: 'number', default: 1 },
      { prop: 'parentPageId', label: '上级页面ID', type: 'number' },
      { prop: 'isMenu', label: '是否菜单', type: 'select', options: yesNoOptions, default: 1 },
      { prop: 'icon', label: '图标', type: 'input' },
      { prop: 'sortNo', label: '排序', type: 'number', default: 0 },
      { prop: 'status', label: '状态', type: 'select', options: statusOptions, default: 1 },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'units', path: '/sys/units', title: '单位管理', group: '基础资料',
    api: '/sys/units', keywordField: 'unitName', description: '操作 t_sys_unit，维护客户和往来单位',
    columns: [
      { prop: 'unitCode', label: '单位编码', width: 120 }, { prop: 'unitName', label: '单位名称', width: 180 },
      { prop: 'shortName', label: '简称', width: 100 }, { prop: 'unitType', label: '类型', width: 100 },
      { prop: 'contact', label: '联系人', width: 110 }, { prop: 'phone', label: '电话', width: 140 }, ...commonColumns
    ],
    fields: [
      { prop: 'unitCode', label: '单位编码', type: 'input', required: true },
      { prop: 'unitName', label: '单位名称', type: 'input', required: true },
      { prop: 'shortName', label: '简称', type: 'input' }, { prop: 'unitType', label: '单位类型', type: 'input' },
      { prop: 'grade', label: '等级', type: 'input' }, { prop: 'contact', label: '联系人', type: 'input' },
      { prop: 'phone', label: '电话', type: 'input' }, { prop: 'taxNo', label: '税号', type: 'input' },
      { prop: 'invoiceTitle', label: '开票抬头', type: 'input' }, { prop: 'settleType', label: '结算方式', type: 'input' },
      { prop: 'creditDays', label: '账期天数', type: 'number' }, { prop: 'deliveryAddr', label: '送货地址', type: 'input' },
      { prop: 'status', label: '状态', type: 'select', options: statusOptions, default: 1 },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'suppliers', path: '/sys/suppliers', title: '供应商管理', group: '基础资料',
    api: '/sys/suppliers', keywordField: 'supName', description: '操作 t_sys_supplier，维护供应商和合作信息',
    columns: [
      { prop: 'supCode', label: '供应商编码', width: 130 }, { prop: 'supName', label: '供应商名称', width: 180 },
      { prop: 'category', label: '合作品类', width: 120 }, { prop: 'contact', label: '联系人', width: 110 },
      { prop: 'phone', label: '电话', width: 140 }, { prop: 'score', label: '评分', width: 90 }, ...commonColumns
    ],
    fields: [
      { prop: 'supCode', label: '供应商编码', type: 'input', required: true },
      { prop: 'supName', label: '供应商名称', type: 'input', required: true },
      { prop: 'shortName', label: '简称', type: 'input' }, { prop: 'category', label: '合作品类', type: 'input' },
      { prop: 'grade', label: '等级', type: 'input' }, { prop: 'contact', label: '联系人', type: 'input' },
      { prop: 'phone', label: '电话', type: 'input' }, { prop: 'taxNo', label: '税号', type: 'input' },
      { prop: 'settleType', label: '结算方式', type: 'input' }, { prop: 'creditDays', label: '账期天数', type: 'number' },
      { prop: 'payMethod', label: '付款方式', type: 'input' }, { prop: 'score', label: '评分', type: 'input' },
      { prop: 'startDate', label: '开始日期', type: 'input' }, { prop: 'status', label: '状态', type: 'select', options: statusOptions, default: 1 },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'banks', path: '/sys/banks', title: '银行账户', group: '基础资料',
    api: '/sys/banks', keywordField: 'accountName', description: '操作 t_sys_bank，维护银行和现金账户',
    columns: [
      { prop: 'accountName', label: '账户名称', width: 170 }, { prop: 'bankName', label: '开户行', width: 180 },
      { prop: 'accountNo', label: '账号', width: 180 }, { prop: 'accountType', label: '账户类型', width: 100 },
      { prop: 'ownerType', label: '所属类型', width: 100 }, { prop: 'isDefault', label: '默认', width: 80, type: 'yesNo' }, ...commonColumns
    ],
    fields: [
      { prop: 'accountName', label: '账户名称', type: 'input', required: true },
      { prop: 'bankName', label: '开户行', type: 'input', required: true },
      { prop: 'branchName', label: '开户支行', type: 'input' },
      { prop: 'accountNo', label: '账号', type: 'input', required: true },
      { prop: 'accountType', label: '账户类型', type: 'number', default: 1 },
      { prop: 'ownerType', label: '所属类型', type: 'number', default: 3 },
      { prop: 'ownerId', label: '所属对象ID', type: 'number', default: 0 },
      { prop: 'currency', label: '币种', type: 'input', default: 'CNY' },
      { prop: 'isDefault', label: '是否默认', type: 'select', options: yesNoOptions, default: 0 },
      { prop: 'status', label: '状态', type: 'select', options: statusOptions, default: 1 },
      { prop: 'effDate', label: '启用日期', type: 'input' },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'commons', path: '/sys/commons', title: '公共信息', group: '基础资料',
    api: '/sys/commons', keywordField: 'dictName', description: '操作 t_sys_common，维护颜色、尺码和面料字典',
    columns: [
      { prop: 'dictType', label: '字典类型', width: 120 }, { prop: 'dictCode', label: '字典编码', width: 120 },
      { prop: 'dictName', label: '字典名称', width: 150 }, { prop: 'extValue', label: '扩展值', width: 150 },
      { prop: 'sortNo', label: '排序', width: 80 }, { prop: 'isSystem', label: '系统内置', width: 90, type: 'yesNo' }, ...commonColumns
    ],
    fields: [
      { prop: 'dictType', label: '字典类型', type: 'input', required: true },
      { prop: 'dictCode', label: '字典编码', type: 'input', required: true },
      { prop: 'dictName', label: '字典名称', type: 'input', required: true },
      { prop: 'parentId', label: '上级字典ID', type: 'number' },
      { prop: 'extValue', label: '扩展值', type: 'input' },
      { prop: 'sortNo', label: '排序', type: 'number', default: 0 },
      { prop: 'isSystem', label: '是否系统内置', type: 'select', options: yesNoOptions, default: 0 },
      { prop: 'status', label: '状态', type: 'select', options: statusOptions, default: 1 },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'styles', path: '/sys/styles', title: '款式管理', group: '基础资料',
    api: '/sys/styles', keywordField: 'styleName', description: '操作 t_sys_style，维护款号、品类和报价',
    columns: [
      { prop: 'styleCode', label: '款号', width: 120 }, { prop: 'styleName', label: '款式名称', width: 170 },
      { prop: 'category', label: '品类', width: 100 }, { prop: 'season', label: '季节', width: 90 },
      { prop: 'quotePrice', label: '报价', width: 100, type: 'money' }, { prop: 'sampleFee', label: '样衣费', width: 100, type: 'money' }, ...commonColumns
    ],
    fields: [
      { prop: 'styleCode', label: '款号', type: 'input', required: true },
      { prop: 'styleName', label: '款式名称', type: 'input', required: true },
      { prop: 'custStyleCode', label: '客户款号', type: 'input' }, { prop: 'unitId', label: '客户单位ID', type: 'number' },
      { prop: 'category', label: '品类', type: 'input' }, { prop: 'season', label: '季节', type: 'input' },
      { prop: 'year', label: '年份', type: 'input' }, { prop: 'gender', label: '适用性别', type: 'number' },
      { prop: 'techDesc', label: '工艺说明', type: 'textarea' }, { prop: 'quotePrice', label: '报价', type: 'number', default: 0 },
      { prop: 'sampleFee', label: '样衣费', type: 'number', default: 0 }, { prop: 'status', label: '状态', type: 'select', options: statusOptions, default: 1 },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'processes', path: '/sys/processes', title: '工序管理', group: '基础资料',
    api: '/sys/processes', keywordField: 'processName', description: '操作 t_sys_process，维护裁剪、缝制、整烫、包装等工序',
    columns: [
      { prop: 'processCode', label: '工序编码', width: 120 }, { prop: 'processName', label: '工序名称', width: 150 },
      { prop: 'processType', label: '工序类型', width: 100, type: 'option', options: processTypeOptions },
      { prop: 'stdMinute', label: '标准工时', width: 100 }, { prop: 'basePrice', label: '基础工价', width: 100, type: 'money' },
      { prop: 'isKey', label: '关键工序', width: 90, type: 'yesNo' }, ...commonColumns
    ],
    fields: [
      { prop: 'processCode', label: '工序编码', type: 'input', required: true },
      { prop: 'processName', label: '工序名称', type: 'input', required: true },
      { prop: 'processType', label: '工序类型', type: 'select', options: processTypeOptions, default: 2 },
      { prop: 'deptId', label: '执行部门ID', type: 'number' }, { prop: 'stdMinute', label: '标准工时', type: 'number', default: 0 },
      { prop: 'basePrice', label: '基础工价', type: 'number', default: 0 }, { prop: 'needEquipment', label: '需要设备', type: 'input' },
      { prop: 'isKey', label: '是否关键工序', type: 'select', options: yesNoOptions, default: 0 },
      { prop: 'sortNo', label: '排序', type: 'number', default: 0 }, { prop: 'status', label: '状态', type: 'select', options: statusOptions, default: 1 },
      { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  },
  {
    key: 'materials', path: '/sys/materials', title: '物料管理', group: '基础资料',
    api: '/sys/materials', keywordField: 'matName', description: '操作 t_sys_material，维护面料、辅料和包装材料',
    columns: [
      { prop: 'matCode', label: '物料编码', width: 120 }, { prop: 'matName', label: '物料名称', width: 170 },
      { prop: 'category', label: '类别', width: 100, type: 'option', options: materialCategoryOptions },
      { prop: 'matType', label: '细分类型', width: 110 }, { prop: 'spec', label: '规格', width: 130 },
      { prop: 'unit', label: '单位', width: 80 }, { prop: 'safetyQty', label: '安全库存', width: 100 }, ...commonColumns
    ],
    fields: [
      { prop: 'matCode', label: '物料编码', type: 'input', required: true },
      { prop: 'matName', label: '物料名称', type: 'input', required: true },
      { prop: 'category', label: '物料类别', type: 'select', options: materialCategoryOptions, default: 1 },
      { prop: 'matType', label: '细分类型', type: 'input' }, { prop: 'spec', label: '规格', type: 'input' },
      { prop: 'color', label: '颜色', type: 'input' }, { prop: 'gramWeight', label: '克重', type: 'number' },
      { prop: 'width', label: '幅宽', type: 'number' }, { prop: 'unit', label: '单位', type: 'input' },
      { prop: 'safetyQty', label: '安全库存', type: 'number', default: 0 }, { prop: 'stdPrice', label: '标准单价', type: 'number', default: 0 },
      { prop: 'taxRate', label: '税率', type: 'number', default: 0.13 }, { prop: 'supplierId', label: '默认供应商ID', type: 'number' },
      { prop: 'status', label: '状态', type: 'select', options: statusOptions, default: 1 }, { prop: 'remark', label: '备注', type: 'textarea' }
    ]
  }
]

export const menuGroups = [
  { label: '总览', items: [{ title: '首页', path: '/', key: 'home' }] },
  { label: '组织权限', items: crudConfigs.filter(item => item.group === '组织权限') },
  { label: '基础资料', items: crudConfigs.filter(item => item.group === '基础资料') }
]
