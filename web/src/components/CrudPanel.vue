<template>
  <section class="content-panel crud-panel">
    <div class="panel-heading">
      <div>
        <h3>{{ config.title }}</h3>
        <p>{{ config.description }}</p>
      </div>
      <div class="panel-heading-actions">
        <el-button type="primary" @click="openCreate">
          <el-icon><Plus /></el-icon>
          {{ createButtonLabel }}
        </el-button>
      </div>
    </div>

    <div class="table-toolbar">
      <el-input
        v-model="keyword"
        class="search-input"
        clearable
        :placeholder="'搜索' + config.title"
        @keyup.enter="search"
      />
      <el-button type="primary" plain @click="search">查询</el-button>
      <el-button @click="resetSearch">重置</el-button>
      <div class="table-toolbar-actions">
        <span class="record-count">共 {{ total }} 条</span>
      </div>
    </div>

    <el-table v-loading="loading" :data="rows" row-key="id" border empty-text="暂无数据，点击右上角新增">
      <el-table-column
        v-for="column in config.columns"
        :key="column.prop"
        :prop="column.prop"
        :label="column.label"
        :width="column.width"
        :min-width="column.minWidth"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <el-tag v-if="column.type === 'status'" :type="row[column.prop] === 1 ? 'success' : 'info'" effect="light">
            {{ row[column.prop] === 1 ? '启用' : '停用' }}
          </el-tag>
          <el-tag v-else-if="column.type === 'yesNo'" :type="row[column.prop] === 1 ? 'success' : 'info'" effect="light">
            {{ row[column.prop] === 1 ? '是' : '否' }}
          </el-tag>
          <el-tag v-else-if="column.type === 'option'" type="info" effect="plain">
            {{ optionLabel(column, row[column.prop]) }}
          </el-tag>
          <span v-else-if="column.type === 'money'">{{ formatMoney(row[column.prop]) }}</span>
          <span v-else-if="column.type === 'fileSize'">{{ formatFileSize(row[column.prop]) }}</span>
          <span v-else-if="column.type === 'datetime'">{{ formatDateTime(row[column.prop]) }}</span>
          <span v-else-if="column.type === 'compute'" :class="{ 'count-strong': column.compute === 'deptMemberCount' || column.compute === 'rolePageCount' || column.compute === 'roleStaffCount' }">
            {{ computedCell(column, row) }}
          </span>
          <span v-else-if="lookupKeyForColumn(column, row)">
            {{ cellLookupLabel(column, row) }}
          </span>
          <span v-else>{{ display(row[column.prop]) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right" :width="operationWidth" align="center">
        <template #default="{ row }">
          <el-button v-if="config.detailPath" link type="primary" @click="openDetail(row)">查看详情</el-button>
          <el-button v-if="!config.hideEdit" link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button v-if="config.key === 'roles'" link type="warning" @click="openRolePermission(row)">权限</el-button>
          <el-button v-if="config.key === 'staffs'" link type="warning" @click="openStaffRoles(row)">角色</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="current"
      v-model:page-size="size"
      class="pager"
      background
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 50, 100]"
      :total="total"
      @current-change="load"
      @size-change="search"
    />
  </section>

  <el-dialog
    v-model="dialogVisible"
    :title="(editingId ? '编辑' : '新增') + entityName"
    width="760px"
    destroy-on-close
  >
    <p class="form-hint">带 * 的字段为必填项，关联对象请直接从下拉列表选择。</p>
    <el-form :model="form" label-position="top">
      <div class="form-grid">
        <el-form-item
          v-for="field in config.fields"
          :key="field.prop"
          :label="fieldLabel(field)"
          :required="field.required"
          :class="{ 'form-item-wide': isWideField(field) }"
        >
          <el-select
            v-if="lookupKeyForField(field)"
            v-model="form[field.prop]"
            filterable
            clearable
            :loading="lookupLoading"
            style="width: 100%"
            @change="onFieldChange(field, $event)"
          >
            <el-option
              v-for="item in lookupItems(field)"
              :key="item.id"
              :label="optionLabelForLookup(lookupKeyForField(field), item)"
              :value="item.id"
            />
          </el-select>

          <el-input-number
            v-else-if="field.type === 'number'"
            v-model="form[field.prop]"
            :min="0"
            :precision="inputPrecision(field)"
            :step="inputStep(field)"
            style="width: 100%"
          />

          <el-select v-else-if="field.type === 'select'" v-model="form[field.prop]" style="width: 100%" clearable>
            <el-option v-for="option in field.options" :key="option.value" :label="option.label" :value="option.value" />
          </el-select>

          <el-date-picker
            v-else-if="isDateField(field)"
            v-model="form[field.prop]"
            :type="isDateTimeField(field) ? 'datetime' : 'date'"
            :value-format="isDateTimeField(field) ? 'YYYY-MM-DD HH:mm:ss' : 'YYYY-MM-DD'"
            placeholder="请选择日期"
            style="width: 100%"
          />

          <el-input
            v-else-if="isAutoCodeField(field)"
            v-model="form[field.prop]"
            readonly
            class="auto-code-input"
            :placeholder="'系统自动生成' + fieldLabel(field)"
          >
            <template v-if="!editingId" #append>
              <el-button :loading="codeGenerating" @click="fillAutoCode(field)">自动生成</el-button>
            </template>
          </el-input>

          <el-input
            v-else-if="field.type === 'textarea'"
            v-model="form[field.prop]"
            type="textarea"
            :rows="3"
            :placeholder="'请输入' + fieldLabel(field)"
          />

          <el-input
            v-else
            v-model="form[field.prop]"
            clearable
            :placeholder="'请输入' + fieldLabel(field)"
          />
        </el-form-item>
      </div>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </div>
    </template>
  </el-dialog>

  <RolePermissionDialog
    v-model="permissionVisible"
    :role="activeRole"
    :pages="lookupRecords('pages')"
    :permissions="lookupRecords('permissions')"
    @saved="afterRelationSaved"
  />
  <StaffRoleDialog
    v-model="staffRoleVisible"
    :staff="activeStaff"
    :roles="lookupRecords('roles')"
    :staff-roles="lookupRecords('staffRoles')"
    @saved="afterRelationSaved"
  />
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { createOne, deleteOne, getPage, updateOne } from '../api/crud'
import { generateCode } from '../api/codeRules'
import {
  idLookupMap, loadLookupData, lookupLabel, optionLabel as lookupOptionLabel
} from '../api/lookups'
import RolePermissionDialog from './RolePermissionDialog.vue'
import StaffRoleDialog from './StaffRoleDialog.vue'

const props = defineProps({
  config: { type: Object, required: true }
})
const router = useRouter()

const rows = ref([])
const total = ref(0)
const current = ref(1)
const size = ref(10)
const keyword = ref('')
const loading = ref(false)
const saving = ref(false)
const lookupLoading = ref(false)
const dialogVisible = ref(false)
const permissionVisible = ref(false)
const staffRoleVisible = ref(false)
const editingId = ref(null)
const activeRole = ref(null)
const activeStaff = ref(null)
const form = reactive({})
const lookups = reactive({})
const codeGenerating = ref(false)

const entityNames = {
  depts: '部门', staffs: '员工', roles: '角色', pages: '页面',
  'staff-depts': '人员部门关系', 'staff-roles': '人员角色关系', 'dept-roles': '部门角色关系', permissions: '权限',
  units: '单位', suppliers: '供应商', banks: '银行账户', commons: '公共信息',
  styles: '款式', processes: '工序', materials: '物料',
  'style-processes': '款式工序', 'style-materials': '款式用料', attachments: '附件',
  orders: '订单', 'order-items': '订单明细', 'purchase-orders': '采购订单', 'purchase-items': '采购明细',
  'misc-orders': '其他订单', 'misc-order-items': '其他订单明细',
  'prod-plans': '生产计划', 'prod-tasks': '工序任务', 'cutting-tasks': '裁剪任务', 'sewing-tasks': '缝制任务', 'trimming-tasks': '剪线任务', 'ironing-tasks': '整烫任务',
  'packing-tasks': '包装任务', 'qc-tasks': '质检任务'
}
const entityName = computed(() => entityNames[props.config.key] || props.config.title.replace('管理', ''))
const createButtonLabel = computed(() => props.config.createLabel || '新增' + entityName.value)
const operationWidth = computed(() => props.config.detailPath ? 165 : (props.config.key === 'roles' || props.config.key === 'staffs' ? 190 : 135))

const fieldLabelMap = {
  deptId: '部门', parentId: '上级部门', leaderStaffId: '负责人', staffId: '人员',
  roleId: '角色', pageId: '页面', parentPageId: '上级页面', subjectId: '授权对象',
  styleId: '款式', materialId: '物料', processId: '工序', unitId: '客户单位',
  supplierId: '默认供应商', uploaderId: '上传人', salesmanId: '业务员',
  ownerId: '所属对象', bizId: '业务单据ID', deptPath: '部门路径', orderNo: '订单号',
  orderId: '订单', poId: '采购订单', buyerId: '采购员', prodPlanId: '生产计划',
  styleCode: '款号快照', msOrderId: '其他订单', applicantId: '申请人', useDeptId: '使用部门',
  plannerId: '计划人', releaseBy: '下达人', issuedBy: '下发人', claimedBy: '领取人',
  qcInspectorId: '质检员'
}

const lookupKeys = computed(() => {
  const keys = new Set(props.config.lookupKeys || [])
  const fields = [...(props.config.fields || []), ...(props.config.columns || [])]
  fields.forEach(field => {
    const key = idLookupMap[field.prop]
    if (key) keys.add(key)
  })
  if (props.config.key === 'roles') {
    ;['pages', 'permissions', 'staffRoles'].forEach(key => keys.add(key))
  }
  if (props.config.key === 'staffs') {
    ;['depts', 'roles', 'staffRoles'].forEach(key => keys.add(key))
  }
  if (props.config.key === 'depts') {
    ;['staffs', 'staffDepts'].forEach(key => keys.add(key))
  }
  if (props.config.key === 'permissions') {
    ;['roles', 'depts', 'staffs', 'pages'].forEach(key => keys.add(key))
  }
  if (props.config.key === 'banks') {
    ;['units', 'suppliers'].forEach(key => keys.add(key))
  }
  return [...keys]
})

// 返回表单字段的显示标签，未配置时回退为字段属性名。
const fieldLabel = field => fieldLabelMap[field.prop] || field.label.replace(/ID$/, '')
// 判断字段是否属于需要自动生成编码的只读字段。
const isAutoCodeField = field => props.config.autoCodeField === field.prop
// 判断表单字段是否应占据整行宽度。
const isWideField = field => field.type === 'textarea' || ['remark', 'techDesc', 'requestParams'].includes(field.prop)
// 判断字段是否为日期类型，以选择日期控件。
const isDateField = field => field.type === 'date' || field.type === 'datetime'
  || (/Date$|Time$/.test(field.prop) && field.type !== 'number')
// 判断日期字段是否需要精确到时分秒。
const isDateTimeField = field => field.type === 'datetime' || field.prop.toLowerCase().includes('time')
// 根据字段业务类型返回数字输入框精度。
const inputPrecision = field => field.precision ?? (['taxRate', 'gramWeight', 'width', 'stdMinute', 'basePrice', 'quotePrice', 'sampleFee', 'stdPrice', 'safetyQty'].includes(field.prop) ? 2 : 0)
// 根据字段业务类型返回数字输入框步长。
const inputStep = field => 10 ** -Math.min(inputPrecision(field), 4)

// 把空值转换为页面统一占位符后显示。
const display = value => value === null || value === undefined || value === '' ? '—' : value
// 读取指定关联字典当前已缓存的记录。
const lookupRecords = key => lookups[key] || []
// 按字段关联字典格式化选项显示文本。
const optionLabelForLookup = (key, item) => lookupOptionLabel(key, item)

// 把字段枚举值转换为中文选项标签。
function optionLabel(column, value) {
  const option = column.options?.find(item => item.value === value)
  return option ? option.label : display(value)
}
// 按人民币格式显示金额，空值显示占位符。
function formatMoney(value) {
  return value === null || value === undefined ? '—' : '¥' + Number(value).toFixed(2)
}
// 把后端日期时间格式化为页面展示格式。
function formatDateTime(value) {
  return value ? String(value).replace('T', ' ').slice(0, 19) : '—'
}
// 将附件字节数格式化为 B、KB 或 MB。
function formatFileSize(value) {
  if (value === null || value === undefined) return '—'
  const bytes = Number(value)
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  return (bytes / 1024 / 1024).toFixed(1) + ' MB'
}

// 推断表格列对应的关联字典键。
function lookupKeyForColumn(column, row) {
  if (column.lookup) return column.lookup
  if (column.prop === 'subjectId') {
    return { 1: 'roles', 2: 'depts', 3: 'staffs' }[row.subjectType]
  }
  if (column.prop === 'ownerId') {
    return { 1: 'units', 2: 'suppliers', 3: 'units', 4: 'staffs' }[row.ownerType]
  }
  return idLookupMap[column.prop] || null
}
// 推断表单字段对应的关联字典键。
function lookupKeyForField(field) {
  if (field.lookup) return field.lookup
  if (field.prop === 'subjectId') {
    return { 1: 'roles', 2: 'depts', 3: 'staffs' }[form.subjectType] || null
  }
  if (field.prop === 'ownerId') {
    return { 1: 'units', 2: 'suppliers', 3: 'units', 4: 'staffs' }[form.ownerType] || null
  }
  if (field.prop === 'bizId') return null
  return idLookupMap[field.prop] || null
}
// 筛选字段可选中的关联记录列表。
function lookupItems(field) {
  const key = lookupKeyForField(field)
  return key ? lookupRecords(key) : []
}
// 把表格中的关联 ID 转成可读名称。
function cellLookupLabel(column, row) {
  const key = lookupKeyForColumn(column, row)
  return key ? lookupLabel(key, lookupRecords(key), row[column.prop]) : display(row[column.prop])
}

// 计算部门人数、角色权限数等派生列显示值。
function computedCell(column, row) {
  if (column.compute === 'deptMemberCount') {
    const relationStaff = lookupRecords('staffDepts')
      .filter(item => Number(item.deptId) === Number(row.id) && Number(item.status) === 1)
      .map(item => Number(item.staffId))
    const directStaff = lookupRecords('staffs')
      .filter(item => Number(item.deptId) === Number(row.id))
      .map(item => Number(item.id))
    return new Set([...relationStaff, ...directStaff]).size
  }
  if (column.compute === 'rolePageCount') {
    return new Set(lookupRecords('permissions')
      .filter(item => Number(item.subjectType) === 1 && Number(item.subjectId) === Number(row.id) && item.actionCode === 'view' && Number(item.operState) === 1)
      .map(item => Number(item.pageId))).size
  }
  if (column.compute === 'roleStaffCount') {
    return new Set(lookupRecords('staffRoles')
      .filter(item => Number(item.roleId) === Number(row.id) && Number(item.status) === 1)
      .map(item => Number(item.staffId))).size
  }
  if (column.compute === 'staffRoleNames') {
    const roleIds = lookupRecords('staffRoles')
      .filter(item => Number(item.staffId) === Number(row.id) && Number(item.status) === 1)
      .map(item => Number(item.roleId))
    const names = lookupRecords('roles')
      .filter(role => roleIds.includes(Number(role.id)))
      .map(role => role.roleName)
    return names.length ? names.join('、') : '—'
  }
  return '—'
}

// 并行加载当前表单和表格需要的关联下拉数据。
async function loadReferences() {
  if (!lookupKeys.value.length) return
  lookupLoading.value = true
  const results = await Promise.allSettled(
    lookupKeys.value.map(async key => ({ key, records: await loadLookupData(key) }))
  )
  results.forEach(result => {
    if (result.status === 'fulfilled') {
      lookups[result.value.key] = result.value.records
    }
  })
  lookupLoading.value = false
}

// 按当前分页和关键字加载模块列表数据。
async function load() {
  loading.value = true
  try {
    const response = await getPage(props.config.api, {
      current: current.value,
      size: size.value,
      keyword: keyword.value,
      ...(props.config.fixedFilters || {})
    })
    const payload = response.data
    if (payload?.code !== 0) throw new Error(payload?.message || '查询失败')
    rows.value = payload?.data?.records || []
    total.value = payload?.data?.total || 0
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || error.message || '查询失败')
  } finally {
    loading.value = false
  }
}

// 回到第一页并按当前关键字重新查询列表。
function search() {
  current.value = 1
  load()
}
// 清空搜索条件并刷新模块列表。
function resetSearch() {
  keyword.value = ''
  current.value = 1
  load()
}
// 重置新增/编辑表单，并在编辑时回填当前行数据。
function resetForm(row) {
  Object.keys(form).forEach(key => delete form[key])
  props.config.fields.forEach(field => {
    form[field.prop] = row?.[field.prop] ?? field.default ?? null
  })
  Object.assign(form, props.config.fixedValues || {})
}
// 调用后端编码服务生成字段编码并回填表单。
async function fillAutoCode(field) {
  if (!props.config.codeBizType || codeGenerating.value) return
  codeGenerating.value = true
  try {
    const response = await generateCode(props.config.codeBizType)
    const payload = response.data?.data
    if (!payload?.previewCode) throw new Error(response.data?.message || '生成编码失败')
    form[field.prop] = payload.previewCode
    ElMessage.success('已生成编码：' + payload.previewCode)
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || error?.message || '生成编码失败')
  } finally {
    codeGenerating.value = false
  }
}

// 按配置进入独立新增页或打开新增对话框。
function openCreate() {
  if (props.config.createMode === 'page' && props.config.createPath) {
    router.push(props.config.createPath)
    return
  }
  editingId.value = null
  resetForm()
  dialogVisible.value = true
}
// 进入当前记录的详情页面。
function openDetail(row) {
  if (!props.config.detailPath) return
  router.push(props.config.detailPath.replace(':id', row.id))
}
// 打开编辑弹窗并回填当前记录。
function openEdit(row) {
  editingId.value = row.id
  resetForm(row)
  dialogVisible.value = true
}
// 打开角色页面权限配置弹窗。
function openRolePermission(row) {
  activeRole.value = row
  permissionVisible.value = true
}
// 打开员工多角色关联配置弹窗。
function openStaffRoles(row) {
  activeStaff.value = row
  staffRoleVisible.value = true
}
// 处理字段联动变化，并自动刷新关联选项。
function onFieldChange(field, value) {
  if (field.prop === 'subjectType') form.subjectId = null
  if (field.prop === 'ownerType') form.ownerId = null
}
// 角色或员工关联保存后刷新当前列表展示。
async function afterRelationSaved() {
  await Promise.all([loadReferences(), load()])
}
// 校验表单后新增或修改记录，并刷新列表。
async function save() {
  if (props.config.codeBizType && props.config.autoCodeField) {
    const codeField = props.config.fields.find(field => field.prop === props.config.autoCodeField)
    if (codeField && !form[codeField.prop]) {
      try {
        const response = await generateCode(props.config.codeBizType)
        form[codeField.prop] = response.data?.data?.previewCode
      } catch (error) {
        ElMessage.error(error?.response?.data?.message || '自动生成编码失败')
        return
      }
    }
  }
  const missing = props.config.fields
    .filter(field => field.required && (form[field.prop] === null || form[field.prop] === undefined || form[field.prop] === ''))
    .map(field => fieldLabel(field))
  if (missing.length) {
    ElMessage.warning('请填写：' + missing.join('、'))
    return
  }
  saving.value = true
  try {
    if (editingId.value) {
      await updateOne(props.config.api, editingId.value, form)
    } else {
      await createOne(props.config.api, form)
    }
    ElMessage.success(editingId.value ? '修改成功' : '新增成功')
    dialogVisible.value = false
    await Promise.all([loadReferences(), load()])
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || error.message || '保存失败')
  } finally {
    saving.value = false
  }
}
// 二次确认后删除当前记录，并刷新列表。
async function remove(row) {
  try {
    await ElMessageBox.confirm('确认删除这条数据吗？删除后不可恢复。', '删除确认', { type: 'warning' })
    await deleteOne(props.config.api, row.id)
    ElMessage.success('删除成功')
    await Promise.all([loadReferences(), load()])
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      ElMessage.error(error?.response?.data?.message || '删除失败')
    }
  }
}

onMounted(async () => {
  await Promise.all([loadReferences(), load()])
})
</script>
