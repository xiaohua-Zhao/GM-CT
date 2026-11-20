import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import WorkspaceView from '../views/WorkspaceView.vue'
import { crudConfigs } from '../config/crudConfigs'
import { workspacePathForKey } from '../config/workspaceConfigs'

const legacyRoutes = crudConfigs.map(config => ({
  path: config.path,
  redirect: () => ({ path: workspacePathForKey(config.key), query: { tab: config.key } })
}))

const routes = [
  { path: '/', name: 'home', component: HomeView },
  { path: '/basic', name: 'basic', component: WorkspaceView, props: { workspace: 'basic' } },
  { path: '/organization', name: 'organization', component: WorkspaceView, props: { workspace: 'organization' } },
  { path: '/base', redirect: '/basic' },
  { path: '/sys', redirect: '/organization' },
  ...legacyRoutes,
  { path: '/:pathMatch(.*)*', redirect: '/' }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
