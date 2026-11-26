<template>
  <router-view v-if="isPublicPage" />

  <div v-else class="app-shell">
    <header class="app-topbar">
      <div class="topbar-brand">
        <img :src="logoIcon" alt="成泰" class="topbar-logo" />
        <div class="topbar-title">
          <strong>成泰服装管理系统</strong>
          <span>CHENGTAI GARMENT MANAGEMENT</span>
        </div>
      </div>
      <div class="topbar-user">
        <span class="user-role">{{ userDisplay }}</span>
        <span class="topbar-divider"></span>
        <span class="system-state"><i></i> 系统在线</span>
        <button type="button" class="logout-button" @click="handleLogout">退出登录</button>
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
              :class="{ active: isActive(item) }"
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
import { computed, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Document, Goods, House, OfficeBuilding, SetUp, Tickets, UserFilled } from '@element-plus/icons-vue'
import logoIcon from './assets/chengtai-logo-icon.png'
import { logout as logoutApi } from './api/auth'
import { clearAuth, getStoredUser } from './utils/auth'

const route = useRoute()
const router = useRouter()
const currentUser = ref(getStoredUser())
const isPublicPage = computed(() => route.meta.public === true)
const userDisplay = computed(() => {
  const user = currentUser.value
  if (!user) return '已登录用户'
  const role = user.roleNames?.[0] || user.post || '员工'
  return user.staffName ? user.staffName + ' · ' + role : user.loginName
})

const menuGroups = [
  {
    label: '总览',
    items: [{ label: '首页看板', path: '/', icon: House }]
  },
  {
    label: '业务',
    items: [
      { label: '订单管理', path: '/orders', icon: Document },
      { label: '采购管理', path: '/purchases', icon: Goods },
      { label: '其他订单', path: '/misc', icon: Tickets }
    ]
  },
  {
    label: '生产管理',
    items: [
      { label: '样衣管理', path: '/samples', icon: Tickets },
      { label: '生产计划', path: '/production', icon: Document, exact: true },
      { label: '裁剪任务', path: '/production/cutting', icon: SetUp },
      { label: '缝制任务', path: '/production/sewing', icon: SetUp },
      { label: '剪线任务', path: '/production/trimming', icon: SetUp },
      { label: '整烫任务', path: '/production/ironing', icon: SetUp },
      { label: '包装任务', path: '/production/packing', icon: SetUp }
    ]
  },
  {
    label: '财务与仓储',
    items: [
      { label: '财务管理', path: '/finance', icon: Tickets },
      { label: '计件工资', path: '/wage', icon: Tickets },
      { label: '仓库管理', path: '/warehouse', icon: Goods },
      { label: '设备管理', path: '/equipment', icon: SetUp }
    ]
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

// 判断菜单路径是否与当前路由匹配，用于高亮导航。
const isActive = item => item.exact
  ? route.path === item.path
  : route.path === item.path || (item.path !== '/' && route.path.startsWith(item.path))
// 执行顶部导航跳转并关闭当前移动端菜单。
const navigate = path => router.push(path)

watch(() => route.fullPath, () => {
  currentUser.value = getStoredUser()
})

// 调用退出接口、清理登录缓存并返回登录页。
async function handleLogout() {
  try {
    await logoutApi()
  } catch {
    // JWT 无状态，即使服务端退出接口失败，本地也必须清除登录状态。
  } finally {
    clearAuth()
    currentUser.value = null
    ElMessage.success('已退出登录')
    await router.replace('/login')
  }
}
</script>
