<template>
  <el-dialog
    :model-value="modelValue"
    :title="role ? '页面授权 · ' + role.roleName : '页面授权'"
    width="760px"
    destroy-on-close
    @update:model-value="emit('update:modelValue', $event)"
  >
    <p class="form-hint">勾选该角色可以访问的页面，保存后立即同步到角色权限记录。</p>
    <div class="permission-toolbar">
      <span>已选择 {{ selectedPageIds.length }} / {{ pages.length }} 个页面</span>
      <div>
        <el-button size="small" @click="selectAll">全部选择</el-button>
        <el-button size="small" @click="clearAll">清空</el-button>
      </div>
    </div>
    <el-checkbox-group v-model="selectedPageIds" class="permission-grid">
      <el-checkbox v-for="page in pages" :key="page.id" :value="page.id" class="permission-item">
        <span class="permission-name">{{ page.pageName }}</span>
        <small>{{ page.moduleCode || '系统' }}模块</small>
      </el-checkbox>
    </el-checkbox-group>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="emit('update:modelValue', false)">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存授权</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { createOne, deleteOne } from '../api/crud'

const props = defineProps({
  modelValue: Boolean,
  role: { type: Object, default: null },
  pages: { type: Array, default: () => [] },
  permissions: { type: Array, default: () => [] }
})
const emit = defineEmits(['update:modelValue', 'saved'])
const selectedPageIds = ref([])
const saving = ref(false)

// 读取当前角色已经勾选的页面权限。
function currentPermissions() {
  if (!props.role) return []
  return props.permissions.filter(item =>
    Number(item.subjectType) === 1 &&
    Number(item.subjectId) === Number(props.role.id) &&
    item.actionCode === 'view' &&
    Number(item.operState) === 1
  )
}

watch(() => props.modelValue, visible => {
  if (!visible) return
  selectedPageIds.value = [...new Set(currentPermissions().map(item => Number(item.pageId)))]
})

// 勾选全部可配置页面权限。
function selectAll() {
  selectedPageIds.value = props.pages.map(page => page.id)
}
// 清空当前角色已勾选的页面权限。
function clearAll() {
  selectedPageIds.value = []
}

// 保存当前角色的页面权限配置并通知父组件刷新。
async function save() {
  if (!props.role) return
  saving.value = true
  try {
    const selected = new Set(selectedPageIds.value.map(Number))
    const current = currentPermissions()
    const removals = current.filter(item => !selected.has(Number(item.pageId)))
    const existingPageIds = new Set(current.map(item => Number(item.pageId)))
    const additions = [...selected].filter(pageId => !existingPageIds.has(pageId))

    await Promise.all(removals.map(item => deleteOne('/ct/permissions', item.id)))
    await Promise.all(additions.map(pageId => createOne('/ct/permissions', {
      subjectType: 1,
      subjectId: props.role.id,
      pageId,
      actionCode: 'view',
      operState: 1,
      grantSource: 1,
      remark: '角色页面授权'
    })))

    ElMessage.success('角色页面权限已保存')
    emit('update:modelValue', false)
    emit('saved')
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || '保存角色权限失败')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.permission-toolbar { display: flex; align-items: center; justify-content: space-between; gap: 12px; margin-bottom: 13px; color: #667085; font-size: 12px; }
.permission-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 10px; max-height: 52vh; overflow-y: auto; padding: 2px; }
.permission-item { width: 100%; height: auto; margin: 0; padding: 10px 12px; border: 1px solid #e4e7ed; border-radius: 7px; align-items: flex-start; }
.permission-item :deep(.el-checkbox__label) { display: flex; flex-direction: column; gap: 4px; min-width: 0; }
.permission-name { color: #334155; font-size: 13px; }
.permission-item small { overflow: hidden; color: #98a2b3; font-size: 11px; text-overflow: ellipsis; white-space: nowrap; }
@media (max-width: 700px) { .permission-grid { grid-template-columns: 1fr; } }
</style>
