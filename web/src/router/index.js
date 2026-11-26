import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import WorkspaceView from '../views/WorkspaceView.vue'
import OrderListView from '../views/OrderListView.vue'
import LoginView from '../views/LoginView.vue'
import OrderCreateView from '../views/OrderCreateView.vue'
import OrderDetailView from '../views/OrderDetailView.vue'
import PurchaseCreateView from '../views/PurchaseCreateView.vue'
import PurchaseDetailView from '../views/PurchaseDetailView.vue'
import ModuleView from '../views/ModuleView.vue'
import ProductionPlanView from '../views/ProductionPlanView.vue'
import SampleView from '../views/SampleView.vue'
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
  { path: '/orders', name: 'orders', component: OrderListView, meta: { title: '订单管理' } },
  { path: '/orders/new', name: 'order-create', component: OrderCreateView, meta: { title: '新增订单' } },
  { path: '/orders/:id', name: 'order-detail', component: OrderDetailView, meta: { title: '订单详情' } },
  { path: '/orders/:id/edit', name: 'order-edit', component: OrderDetailView, props: { mode: 'edit' }, meta: { title: '编辑订单' } },
  { path: '/purchases', name: 'purchases', component: WorkspaceView, props: { workspace: 'purchases' }, meta: { title: '采购管理' } },
  { path: '/purchases/new', name: 'purchase-create', component: PurchaseCreateView, meta: { title: '新增采购订单' } },
  { path: '/purchases/:id', name: 'purchase-detail', component: PurchaseDetailView, meta: { title: '采购订单详情' } },
  { path: '/samples', name: 'samples', component: SampleView, meta: { title: '样衣管理' } },
  { path: '/production', name: 'production', component: ProductionPlanView, meta: { title: '生产计划' } },
  { path: '/production/cutting', name: 'production-cutting', component: ModuleView, props: { workspace: 'production', configKey: 'cutting-tasks' }, meta: { title: '裁剪任务' } },
  { path: '/production/sewing', name: 'production-sewing', component: ModuleView, props: { workspace: 'production', configKey: 'sewing-tasks' }, meta: { title: '缝制任务' } },
  { path: '/production/trimming', name: 'production-trimming', component: ModuleView, props: { workspace: 'production', configKey: 'trimming-tasks' }, meta: { title: '剪线任务' } },
  { path: '/production/ironing', name: 'production-ironing', component: ModuleView, props: { workspace: 'production', configKey: 'ironing-tasks' }, meta: { title: '整烫任务' } },
  { path: '/production/packing', name: 'production-packing', component: ModuleView, props: { workspace: 'production', configKey: 'packing-tasks' }, meta: { title: '包装任务' } },
  { path: '/production/tasks', name: 'production-tasks', component: ModuleView, props: { workspace: 'production', configKey: 'prod-tasks' }, meta: { title: '全部工序任务' } },
  { path: '/misc', name: 'misc', component: WorkspaceView, props: { workspace: 'misc' }, meta: { title: '其他订单管理' } },
  { path: '/finance', name: 'finance', component: WorkspaceView, props: { workspace: 'finance' }, meta: { title: '财务管理' } },
  { path: '/wage', name: 'wage', component: WorkspaceView, props: { workspace: 'wage' }, meta: { title: '计件工资' } },
  { path: '/warehouse', name: 'warehouse', component: WorkspaceView, props: { workspace: 'warehouse' }, meta: { title: '仓库管理' } },
  { path: '/equipment', name: 'equipment', component: WorkspaceView, props: { workspace: 'equipment' }, meta: { title: '设备管理' } },
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
