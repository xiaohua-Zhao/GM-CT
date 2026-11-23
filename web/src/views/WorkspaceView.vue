<template>
  <section class="workspace-page">
    <div class="page-heading">
      <div>
        <h1 class="page-title">{{ definition.title }}</h1>
        <p class="page-desc">{{ definition.description }}</p>
      </div>
      <el-tag type="info" effect="plain">{{ definition.tabs.length }} 个子模块</el-tag>
    </div>

    <div class="workspace-tabs">
      <button
        v-for="tab in definition.tabs"
        :key="tab.key"
        type="button"
        class="workspace-tab"
        :class="{ active: activeKey === tab.key }"
        @click="setTab(tab.key)"
      >
        <el-icon><component :is="iconMap[tab.icon]" /></el-icon>
        <span>{{ tab.label }}</span>
      </button>
    </div>

    <CodeRulePanel v-if="activeKey === 'code-rules'" />
    <CrudPanel v-else-if="activeConfig" :key="activeKey" :config="activeConfig" />
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  Box, Collection, Connection, CreditCard, Document, Files, Goods, Grid, Key,
  List, OfficeBuilding, Postcard, SetUp, Tickets, User, UserFilled
} from '@element-plus/icons-vue'
import CrudPanel from '../components/CrudPanel.vue'
import CodeRulePanel from '../components/CodeRulePanel.vue'
import { getWorkspaceConfig, workspaceDefinitions } from '../config/workspaceConfigs'

const props = defineProps({
  workspace: { type: String, required: true }
})

const route = useRoute()
const router = useRouter()
const definition = computed(() => workspaceDefinitions[props.workspace])
const activeKey = computed(() => {
  const requested = String(route.query.tab || '')
  return definition.value.tabs.some(tab => tab.key === requested)
    ? requested
    : definition.value.tabs[0].key
})
const activeConfig = computed(() => getWorkspaceConfig(props.workspace, activeKey.value))
const iconMap = {
  Box, Collection, Connection, CreditCard, Document, Files, Goods, Grid, Key,
  List, OfficeBuilding, Postcard, SetUp, Tickets, User, UserFilled
}

// 切换工作区内当前显示的业务模块页签。
function setTab(key) {
  router.replace({ path: route.path, query: { tab: key } })
}
</script>
