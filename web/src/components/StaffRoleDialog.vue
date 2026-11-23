<template>
  <el-dialog
    :model-value="modelValue"
    :title="staff ? '关联角色 · ' + staff.staffName : '关联角色'"
    width="620px"
    destroy-on-close
    @update:model-value="emit('update:modelValue', $event)"
  >
    <p class="form-hint">一个员工可以同时关联多个角色，登录后权限自动合并。</p>
    <el-checkbox-group v-model="selectedRoleIds" class="role-grid">
      <el-checkbox v-for="role in roles" :key="role.id" :value="role.id" class="role-item">
        <span>{{ role.roleName }}</span>
        <small>{{ role.roleCode }} · {{ dataScopeText(role.dataScope) }}</small>
      </el-checkbox>
    </el-checkbox-group>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="emit('update:modelValue', false)">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存角色</el-button>
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
  staff: { type: Object, default: null },
  roles: { type: Array, default: () => [] },
  staffRoles: { type: Array, default: () => [] }
})
const emit = defineEmits(['update:modelValue', 'saved'])
const selectedRoleIds = ref([])
const saving = ref(false)

const scopeLabels = { 1: '全部数据', 2: '本部门及下级', 3: '仅本人', 4: '指定部门' }
// 把角色数据范围代码转换为中文说明。
const dataScopeText = value => scopeLabels[value] || '未设置'

// 读取当前员工已经关联的角色 ID 列表。
function currentRelations() {
  if (!props.staff) return []
  return props.staffRoles.filter(item =>
    Number(item.staffId) === Number(props.staff.id) && Number(item.status) === 1
  )
}

watch(() => props.modelValue, visible => {
  if (!visible) return
  selectedRoleIds.value = currentRelations().map(item => Number(item.roleId))
})

// 保存员工多角色关联并通知父组件刷新。
async function save() {
  if (!props.staff) return
  saving.value = true
  try {
    const selected = new Set(selectedRoleIds.value.map(Number))
    const current = currentRelations()
    const removals = current.filter(item => !selected.has(Number(item.roleId)))
    const existingRoleIds = new Set(current.map(item => Number(item.roleId)))
    const additions = [...selected].filter(roleId => !existingRoleIds.has(roleId))

    await Promise.all(removals.map(item => deleteOne('/ct/staff-roles', item.id)))
    await Promise.all(additions.map(roleId => createOne('/ct/staff-roles', {
      staffId: props.staff.id,
      roleId,
      grantType: 1,
      status: 1,
      remark: '员工角色关联'
    })))

    ElMessage.success('员工角色已保存')
    emit('update:modelValue', false)
    emit('saved')
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || '保存员工角色失败')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.role-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 10px; max-height: 52vh; overflow-y: auto; }
.role-item { width: 100%; height: auto; margin: 0; padding: 11px 12px; border: 1px solid #e4e7ed; border-radius: 7px; align-items: flex-start; }
.role-item :deep(.el-checkbox__label) { display: flex; flex-direction: column; gap: 4px; color: #334155; font-size: 13px; }
.role-item small { color: #98a2b3; font-size: 11px; }
@media (max-width: 620px) { .role-grid { grid-template-columns: 1fr; } }
</style>
