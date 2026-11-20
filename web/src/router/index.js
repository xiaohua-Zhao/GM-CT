import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CrudPage from '../views/CrudPage.vue'
import { crudConfigs } from '../config/crudConfigs'

const routes = [
  { path: '/', name: 'home', component: HomeView },
  ...crudConfigs.map(config => ({
    path: config.path,
    name: config.key,
    component: CrudPage,
    props: { config }
  }))
]

export default createRouter({
  history: createWebHistory(),
  routes
})
