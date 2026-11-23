<template>
  <CrudPanel v-if="config" :key="configKey" :config="config" />
</template>

<script setup>
import { computed } from 'vue'
import CrudPanel from '../components/CrudPanel.vue'
import { getWorkspaceConfig } from '../config/workspaceConfigs'

const props = defineProps({
  workspace: { type: String, required: true },
  configKey: { type: String, required: true }
})

// 根据工作区和配置键读取独立模块的 CRUD 页面配置。
const config = computed(() => getWorkspaceConfig(props.workspace, props.configKey))
</script>
