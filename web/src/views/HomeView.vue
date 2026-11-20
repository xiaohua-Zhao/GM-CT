<template>
  <section class="overview-page">
    <div class="page-heading">
      <div>
        <h1 class="page-title">首页看板</h1>
        <p class="page-desc">基础资料与组织权限一屏总览，点击卡片可进入对应模块。</p>
      </div>
      <div class="page-actions">
        <el-button :loading="loading" @click="loadStats">刷新数据</el-button>
      </div>
    </div>

    <div class="home-stats">
      <article v-for="item in stats" :key="item.key" class="home-stat" @click="go(item)">
        <div class="home-stat-head">
          <span>{{ item.label }}</span>
          <span class="home-stat-icon"><el-icon><component :is="item.icon" /></el-icon></span>
        </div>
        <div class="home-stat-value">{{ item.count }}</div>
        <div class="home-stat-note">{{ item.note }}</div>
      </article>
    </div>

    <div class="home-columns">
      <section class="content-panel">
        <div class="panel-heading">
          <div>
            <h3>快捷入口</h3>
            <p>从常用模块快速开始工作</p>
          </div>
        </div>
        <div class="quick-grid">
          <article class="quick-card" @click="router.push('/organization?tab=depts')">
            <el-icon><OfficeBuilding /></el-icon>
            <strong>员工与组织</strong>
            <span>部门、员工、角色和页面授权统一维护</span>
          </article>
          <article class="quick-card" @click="router.push('/basic?tab=units')">
            <el-icon><Goods /></el-icon>
            <strong>基础数据管理</strong>
            <span>单位、供应商、银行账户、款式和物料</span>
          </article>
          <article class="quick-card" @click="router.push('/organization?tab=roles')">
            <el-icon><Key /></el-icon>
            <strong>角色权限</strong>
            <span>为角色勾选页面，并为员工关联多个角色</span>
          </article>
        </div>
      </section>

      <section class="content-panel">
        <div class="panel-heading">
          <div>
            <h3>基础模块状态</h3>
            <p>当前已接入的真实业务数据</p>
          </div>
        </div>
        <div class="readiness-list">
          <div class="readiness-item">
            <i class="readiness-dot ok"></i>
            <div><strong>组织架构</strong><span>已维护 {{ countOf('depts') }} 个部门</span></div>
            <span class="readiness-tag">已就绪</span>
          </div>
          <div class="readiness-item">
            <i class="readiness-dot" :class="{ ok: countOf('staffs') > 0 }"></i>
            <div><strong>员工档案</strong><span>{{ countOf('staffs') > 0 ? '已录入 ' + countOf('staffs') + ' 名员工' : '尚未录入员工资料' }}</span></div>
            <span class="readiness-tag">{{ countOf('staffs') > 0 ? '已就绪' : '待录入' }}</span>
          </div>
          <div class="readiness-item">
            <i class="readiness-dot ok"></i>
            <div><strong>角色与页面</strong><span>{{ countOf('roles') }} 个角色 · {{ countOf('pages') }} 个页面</span></div>
            <span class="readiness-tag">已就绪</span>
          </div>
          <div class="readiness-item">
            <i class="readiness-dot ok"></i>
            <div><strong>基础资料</strong><span>{{ countOf('styles') }} 个款式 · {{ countOf('materials') }} 种物料</span></div>
            <span class="readiness-tag">已就绪</span>
          </div>
        </div>
      </section>
    </div>
  </section>
</template>

<script setup>
import { markRaw, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getPage } from '../api/crud'
import {
  Box, Collection, Document, Goods, Key, OfficeBuilding, Postcard, UserFilled
} from '@element-plus/icons-vue'

const router = useRouter()
const loading = ref(false)
const stats = reactive([
  { key: 'depts', label: '组织部门', count: 0, api: '/ct/depts', path: '/organization', tab: 'depts', icon: markRaw(OfficeBuilding), note: '部门层级与负责人' },
  { key: 'staffs', label: '员工档案', count: 0, api: '/ct/staffs', path: '/organization', tab: 'staffs', icon: markRaw(UserFilled), note: '在岗员工基础资料' },
  { key: 'roles', label: '角色配置', count: 0, api: '/ct/roles', path: '/organization', tab: 'roles', icon: markRaw(Key), note: '角色与页面授权' },
  { key: 'pages', label: '系统页面', count: 0, api: '/ct/pages', path: '/organization', tab: 'pages', icon: markRaw(Document), note: '页面与访问路径' },
  { key: 'units', label: '合作单位', count: 0, api: '/sys/units', path: '/basic', tab: 'units', icon: markRaw(OfficeBuilding), note: '客户及合作单位' },
  { key: 'suppliers', label: '供应商', count: 0, api: '/sys/suppliers', path: '/basic', tab: 'suppliers', icon: markRaw(Goods), note: '物料供应合作伙伴' },
  { key: 'styles', label: '款式档案', count: 0, api: '/sys/styles', path: '/basic', tab: 'styles', icon: markRaw(Postcard), note: '款式、报价与样衣费' },
  { key: 'materials', label: '物料档案', count: 0, api: '/sys/materials', path: '/basic', tab: 'materials', icon: markRaw(Box), note: '面辅料及包装材料' }
])

const countOf = key => stats.find(item => item.key === key)?.count || 0
const go = item => router.push({ path: item.path, query: { tab: item.tab } })

async function loadStats() {
  loading.value = true
  const results = await Promise.allSettled(
    stats.map(item => getPage(item.api, { current: 1, size: 1 }))
  )
  results.forEach((result, index) => {
    if (result.status === 'fulfilled') {
      stats[index].count = result.value.data?.data?.total || 0
    }
  })
  loading.value = false
}

onMounted(loadStats)
</script>
