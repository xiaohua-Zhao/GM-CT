import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import WorkspaceView from '../views/WorkspaceView.vue'
import LoginView from '../views/LoginView.vue'
import OrderCreateView from '../views/OrderCreateView.vue'
import OrderDetailView from '../views/OrderDetailView.vue'
import PurchaseCreateView from '../views/PurchaseCreateView.vue'
import PurchaseDetailView from '../views/PurchaseDetailView.vue'
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
  { path: '/orders', name: 'orders', component: WorkspaceView, props: { workspace: 'orders' }, meta: { title: '订单管理' } },
  { path: '/orders/new', name: 'order-create', component: OrderCreateView, meta: { title: '新增订单' } },
  { path: '/orders/:id', name: 'order-detail', component: OrderDetailView, meta: { title: '订单详情' } },
  { path: '/purchases', name: 'purchases', component: WorkspaceView, props: { workspace: 'purchases' }, meta: { title: '采购管理' } },
  { path: '/purchases/new', name: 'purchase-create', component: PurchaseCreateView, meta: { title: '新增采购订单' } },
  { path: '/purchases/:id', name: 'purchase-detail', component: PurchaseDetailView, meta: { title: '采购订单详情' } },
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
