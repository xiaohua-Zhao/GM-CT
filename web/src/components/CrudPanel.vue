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
          新增{{ entityName }}
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
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
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
            :type="field.prop.toLowerCase().includes('time') ? 'datetime' : 'date'"
            :value-format="field.prop.toLowerCase().includes('time') ? 'YYYY-MM-DD HH:mm:ss' : 'YYYY-MM-DD'"
            placeholder="请选择日期"
            style="width: 100%"
          />

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
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { createOne, deleteOne, getPage, updateOne } from '../api/crud'
import {
  idLookupMap, loadLookupData, lookupLabel, optionLabel as lookupOptionLabel
} from '../api/lookups'
import RolePermissionDialog from './RolePermissionDialog.vue'
import StaffRoleDialog from './StaffRoleDialog.vue'

const props = defineProps({
  config: { type: Object, required: true }
})

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

const entityNames = {
  depts: '部门', staffs: '员工', roles: '角色', pages: '页面',
  'staff-depts': '人员部门关系', 'staff-roles': '人员角色关系', 'dept-roles': '部门角色关系', permissions: '权限',
  units: '单位', suppliers: '供应商', banks: '银行账户', commons: '公共信息',
  styles: '款式', processes: '工序', materials: '物料',
  'style-processes': '款式工序', 'style-materials': '款式用料', attachments: '附件'
}
const entityName = computed(() => entityNames[props.config.key] || props.config.title.replace('管理', ''))
const operationWidth = computed(() => props.config.key === 'roles' || props.config.key === 'staffs' ? 190 : 135)

const fieldLabelMap = {
  deptId: '部门', parentId: '上级部门', leaderStaffId: '负责人', staffId: '人员',
  roleId: '角色', pageId: '页面', parentPageId: '上级页面', subjectId: '授权对象',
  styleId: '款式', materialId: '物料', processId: '工序', unitId: '客户单位',
  supplierId: '默认供应商', uploaderId: '上传人', salesmanId: '业务员',
  ownerId: '所属对象', bizId: '业务单据ID', deptPath: '部门路径', orderNo: '订单号'
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

const fieldLabel = field => fieldLabelMap[field.prop] || field.label.replace(/ID$/, '')
const isWideField = field => field.type === 'textarea' || ['remark', 'techDesc', 'requestParams'].includes(field.prop)
const isDateField = field => /Date$|Time$/.test(field.prop) && field.type !== 'number'
const inputPrecision = field => field.precision ?? (['taxRate', 'gramWeight', 'width', 'stdMinute', 'basePrice', 'quotePrice', 'sampleFee', 'stdPrice', 'safetyQty'].includes(field.prop) ? 2 : 0)
const inputStep = field => 10 ** -Math.min(inputPrecision(field), 4)

const display = value => value === null || value === undefined || value === '' ? '—' : value
const lookupRecords = key => lookups[key] || []
const optionLabelForLookup = (key, item) => lookupOptionLabel(key, item)

function optionLabel(column, value) {
  const option = column.options?.find(item => item.value === value)
  return option ? option.label : display(value)
}
function formatMoney(value) {
  return value === null || value === undefined ? '—' : '¥' + Number(value).toFixed(2)
}
function formatDateTime(value) {
  return value ? String(value).replace('T', ' ').slice(0, 19) : '—'
}
function formatFileSize(value) {
  if (value === null || value === undefined) return '—'
  const bytes = Number(value)
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  return (bytes / 1024 / 1024).toFixed(1) + ' MB'
}

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
function lookupItems(field) {
  const key = lookupKeyForField(field)
  return key ? lookupRecords(key) : []
}
function cellLookupLabel(column, row) {
  const key = lookupKeyForColumn(column, row)
  return key ? lookupLabel(key, lookupRecords(key), row[column.prop]) : display(row[column.prop])
}

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

async function load() {
  loading.value = true
  try {
    const response = await getPage(props.config.api, {
      current: current.value,
      size: size.value,
      keyword: keyword.value
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

function search() {
  current.value = 1
  load()
}
function resetSearch() {
  keyword.value = ''
  current.value = 1
  load()
}
function resetForm(row) {
  Object.keys(form).forEach(key => delete form[key])
  props.config.fields.forEach(field => {
    form[field.prop] = row?.[field.prop] ?? field.default ?? null
  })
}
function openCreate() {
  editingId.value = null
  resetForm()
  dialogVisible.value = true
}
function openEdit(row) {
  editingId.value = row.id
  resetForm(row)
  dialogVisible.value = true
}
function openRolePermission(row) {
  activeRole.value = row
  permissionVisible.value = true
}
function openStaffRoles(row) {
  activeStaff.value = row
  staffRoleVisible.value = true
}
function onFieldChange(field, value) {
  if (field.prop === 'subjectType') form.subjectId = null
  if (field.prop === 'ownerType') form.ownerId = null
}
async function afterRelationSaved() {
  await Promise.all([loadReferences(), load()])
}
async function save() {
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
