import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import WorkspaceView from '../views/WorkspaceView.vue'
import LoginView from '../views/LoginView.vue'
import { crudConfigs } from '../config/crudConfigs'
import { workspacePathForKey } from '../config/workspaceConfigs'
import { isAuthenticated } from '../utils/auth'

const legacyRoutes = crudConfigs.map(config => ({
  path: config.path,
  redirect: () => ({ path: workspacePathForKey(config.key), query: { tab: config.key } })
}))

const routes = [
  { path: '/login', name: 'login', component: LoginView, meta: { public: true, title: '登录' } },
  { path: '/', name: 'home', component: HomeView, meta: { title: '首页看板' } },
  { path: '/basic', name: 'basic', component: WorkspaceView, props: { workspace: 'basic' }, meta: { title: '基础信息管理' } },
  { path: '/organization', name: 'organization', component: WorkspaceView, props: { workspace: 'organization' }, meta: { title: '员工与组织' } },
  { path: '/base', redirect: '/basic' },
  { path: '/sys', redirect: '/organization' },
  ...legacyRoutes,
  { path: '/:pathMatch(.*)*', redirect: '/' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(to => {
  const authenticated = isAuthenticated()
  if (to.meta.public) {
    if (to.path === '/login' && authenticated) return '/'
    return true
  }
  if (!authenticated) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  return true
})

router.afterEach(to => {
  document.title = to.meta.title ? to.meta.title + ' · 成泰服装管理系统' : '成泰服装管理系统'
})

export default router
