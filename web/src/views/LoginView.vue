<template>
  <div class="login-page">
    <div class="login-background-line line-one"></div>
    <div class="login-background-line line-two"></div>

    <section class="login-brand-panel">
      <div class="login-brand-inner">
        <img :src="logoFull" alt="成泰服装管理系统" class="login-brand-logo" />
        <p class="login-brand-slogan">简单 · 高效 · 让每一件衣服都可追溯</p>
        <div class="login-feature-list">
          <div class="login-feature"><span>01</span><div><strong>业务协同</strong><small>订单、生产、采购和财务数据统一管理</small></div></div>
          <div class="login-feature"><span>02</span><div><strong>权限清晰</strong><small>部门、角色与页面授权实时同步</small></div></div>
          <div class="login-feature"><span>03</span><div><strong>安全登录</strong><small>JWT 身份认证，登录状态自动校验</small></div></div>
        </div>
      </div>
    </section>

    <section class="login-form-panel">
      <div class="login-card">
        <div class="login-card-heading">
          <p class="login-eyebrow">WELCOME BACK</p>
          <h1>登录系统</h1>
          <p>请输入员工账号和密码进入成泰服装管理系统</p>
        </div>

        <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @keyup.enter="submit">
          <el-form-item label="登录账号" prop="loginName">
            <el-input v-model="form.loginName" size="large" clearable autocomplete="username" placeholder="请输入登录名，例如 chenzhiyuan">
              <template #prefix><el-icon><User /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item label="登录密码" prop="password">
            <el-input v-model="form.password" size="large" show-password autocomplete="current-password" placeholder="请输入登录密码">
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>

          <div class="login-options">
            <el-checkbox v-model="remember">记住登录状态</el-checkbox>
            <span>Token 有效期 8 小时</span>
          </div>

          <el-button type="primary" size="large" class="login-submit" :loading="loading" @click="submit">
            登 录
          </el-button>
        </el-form>

        <div class="login-footer">
          <span>成泰服装管理系统</span>
          <span>JWT 安全认证</span>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Lock, User } from '@element-plus/icons-vue'
import logoFull from '../assets/chengtai-logo-full.png'
import { login } from '../api/auth'
import { saveAuth } from '../utils/auth'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const remember = ref(true)
const form = reactive({ loginName: '', password: '' })
const rules = {
  loginName: [{ required: true, message: '请输入登录账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入登录密码', trigger: 'blur' }]
}

async function submit() {
  if (loading.value) return
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const response = await login({ loginName: form.loginName.trim(), password: form.password })
    const payload = response.data?.data
    if (!payload?.accessToken) throw new Error(response.data?.message || '登录失败')
    saveAuth(payload, remember.value)
    ElMessage.success('登录成功')
    const redirect = typeof route.query.redirect === 'string' && route.query.redirect.startsWith('/')
      ? route.query.redirect
      : '/'
    await router.replace(redirect)
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || error?.message || '登录失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>
