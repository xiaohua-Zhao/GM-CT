<template>
  <section class="crud-page">
    <div class="crud-heading">
      <div>
        <p class="eyebrow">{{ config.group }}</p>
        <h1>{{ config.title }}</h1>
        <p class="subtitle">{{ config.description }}</p>
      </div>
      <el-button type="primary" @click="openCreate">
        <el-icon><Plus /></el-icon>
        新增
      </el-button>
    </div>

    <el-card shadow="never" class="panel">
      <div class="table-toolbar">
        <el-input
          v-model="keyword"
          clearable
          placeholder="输入关键字搜索"
          style="width: 240px"
          @keyup.enter="search"
        />
        <el-button type="primary" plain @click="search">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
        <span class="record-count">共 {{ total }} 条</span>
      </div>

      <el-table v-loading="loading" :data="rows" border stripe height="calc(100vh - 330px)">
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
            <el-tag v-if="column.type === 'status'" :type="row[column.prop] === 1 ? 'success' : 'info'">
              {{ row[column.prop] === 1 ? '启用' : '停用' }}
            </el-tag>
            <el-tag v-else-if="column.type === 'yesNo'" :type="row[column.prop] === 1 ? 'success' : 'info'">
              {{ row[column.prop] === 1 ? '是' : '否' }}
            </el-tag>
            <el-tag v-else-if="column.type === 'option'" type="info">{{ optionLabel(column, row[column.prop]) }}</el-tag>
            <span v-else-if="column.type === 'money'">{{ formatMoney(row[column.prop]) }}</span>
            <span v-else-if="column.prop === 'updatedAt'">{{ formatDateTime(row[column.prop]) }}</span>
            <span v-else>{{ display(row[column.prop]) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="150" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
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
    </el-card>

    <el-dialog v-model="dialogVisible" :title="editingId ? `编辑${config.title}` : `新增${config.title}`" width="560px" destroy-on-close>
      <el-form :model="form" label-width="100px">
        <el-form-item v-for="field in config.fields" :key="field.prop" :label="field.label" :required="field.required">
          <el-input-number
            v-if="field.type === 'number'"
            v-model="form[field.prop]"
            :min="0"
            :precision="field.prop === 'unitPrice' || field.prop === 'safetyStock' ? 2 : 0"
            style="width: 100%"
          />
          <el-select v-else-if="field.type === 'select'" v-model="form[field.prop]" style="width: 100%">
            <el-option v-for="option in field.options" :key="option.value" :label="option.label" :value="option.value" />
          </el-select>
          <el-input v-else-if="field.type === 'textarea'" v-model="form[field.prop]" type="textarea" :rows="3" />
          <el-input v-else v-model="form[field.prop]" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { createOne, deleteOne, getPage, updateOne } from '../api/crud'

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
const dialogVisible = ref(false)
const editingId = ref(null)
const form = reactive({})

function display(value) {
  return value === null || value === undefined || value === '' ? '—' : value
}
function optionLabel(column, value) {
  const option = column.options?.find(item => item.value === value)
  return option ? option.label : display(value)
}
function formatMoney(value) {
  return value === null || value === undefined ? '—' : `¥${Number(value).toFixed(2)}`
}
function formatDateTime(value) {
  return value ? String(value).replace('T', ' ').slice(0, 19) : '—'
}

async function load() {
  loading.value = true
  try {
    const response = await getPage(props.config.api, { current: current.value, size: size.value, keyword: keyword.value })
    const data = response.data?.data
    rows.value = data?.records || []
    total.value = data?.total || 0
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || '查询失败')
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
async function save() {
  const missing = props.config.fields.filter(field => field.required && (form[field.prop] === null || form[field.prop] === undefined || form[field.prop] === '')).map(field => field.label)
  if (missing.length) {
    ElMessage.warning(`请填写：${missing.join('、')}`)
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
    await load()
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || '保存失败')
  } finally {
    saving.value = false
  }
}
async function remove(row) {
  try {
    await ElMessageBox.confirm('确认删除这条数据吗？删除后不可恢复。', '删除确认', { type: 'warning' })
    await deleteOne(props.config.api, row.id)
    ElMessage.success('删除成功')
    await load()
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      ElMessage.error(error?.response?.data?.message || '删除失败')
    }
  }
}

onMounted(load)
</script>
