<template>
  <el-container class="app-shell">
    <el-aside width="224px" class="sidebar">
      <div class="brand">
        <span class="brand-mark">成泰</span>
        <div>
          <strong>成泰服装管理</strong>
          <small>FASHION MGMT</small>
        </div>
      </div>
      <el-menu :default-active="$route.path" router class="side-menu">
        <el-menu-item-group v-for="group in menuGroups" :key="group.label" :title="group.label">
          <el-menu-item v-for="item in group.items" :key="item.path" :index="item.path">
            {{ item.title }}
          </el-menu-item>
        </el-menu-item-group>
      </el-menu>
      <div class="sidebar-footer">
        <span class="online-dot"></span>
        <span>基础模块开发中</span>
      </div>
    </el-aside>
    <el-container>
      <el-header class="topbar">
        <div>
          <span class="topbar-label">成泰服装管理系统</span>
          <span class="topbar-divider">/</span>
          <span>{{ currentTitle }}</span>
        </div>
        <el-tag effect="dark" type="success">MySQL · Spring Boot · Vue</el-tag>
      </el-header>
      <el-main class="main-area">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { menuGroups } from './config/crudConfigs'

const route = useRoute()
const currentTitle = computed(() => {
  for (const group of menuGroups) {
    const item = group.items.find(entry => entry.path === route.path)
    if (item) return item.title
  }
  return '系统首页'
})
</script>
