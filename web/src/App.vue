<template>
  <div class="app-shell">
    <header class="app-topbar">
      <div class="topbar-brand">
        <img :src="logoIcon" alt="成泰" class="topbar-logo" />
        <div class="topbar-title">
          <strong>成泰服装管理系统</strong>
          <span>CHENGTAI GARMENT MANAGEMENT</span>
        </div>
      </div>
      <div class="topbar-user">
        <span class="user-role">系统管理员</span>
        <span class="topbar-divider"></span>
        <span class="system-state"><i></i> 系统在线</span>
      </div>
    </header>

    <div class="app-body">
      <aside class="app-sidebar">
        <nav class="side-navigation">
          <div v-for="group in menuGroups" :key="group.label" class="menu-group">
            <div class="menu-group-title">{{ group.label }}</div>
            <button
              v-for="item in group.items"
              :key="item.path"
              type="button"
              class="menu-item"
              :class="{ active: isActive(item.path) }"
              @click="navigate(item.path)"
            >
              <el-icon><component :is="item.icon" /></el-icon>
              <span>{{ item.label }}</span>
            </button>
          </div>
        </nav>
        <div class="sidebar-footer">
          <strong>成泰服装管理系统</strong>
          <span>基础资料与组织权限</span>
        </div>
      </aside>

      <main class="app-main">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { House, OfficeBuilding, UserFilled } from '@element-plus/icons-vue'
import logoIcon from './assets/chengtai-logo-icon.png'

const route = useRoute()
const router = useRouter()

const menuGroups = [
  {
    label: '总览',
    items: [{ label: '首页看板', path: '/', icon: House }]
  },
  {
    label: '基础资料',
    items: [{ label: '基础信息管理', path: '/basic', icon: OfficeBuilding }]
  },
  {
    label: '系统',
    items: [{ label: '员工与组织', path: '/organization', icon: UserFilled }]
  }
]

const isActive = target => route.path === target || (target !== '/' && route.path.startsWith(target))
const navigate = path => router.push(path)

const currentTitle = computed(() => {
  if (route.path === '/basic') return '基础信息管理'
  if (route.path === '/organization') return '员工与组织'
  return '首页看板'
})
</script>
