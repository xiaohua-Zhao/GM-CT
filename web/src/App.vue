<template>
  <div class="app-shell">
    <header class="topbar">
      <div class="brand">
        <span class="brand-mark">成泰</span>
        <div>
          <strong>成泰服装管理系统</strong>
          <small>Spring Boot · MyBatis-Plus · Vue 3</small>
        </div>
      </div>
      <el-tag effect="dark" type="success">基础框架已启动</el-tag>
    </header>
    <main class="content">
      <router-view />
    </main>
  </div>
</template>
