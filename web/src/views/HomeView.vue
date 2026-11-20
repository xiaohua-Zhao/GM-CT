<template>
  <section class="hero">
    <div>
      <p class="eyebrow">SYSTEM STATUS</p>
      <h1>项目初始化完成</h1>
      <p class="subtitle">后端、数据库、日志和附件目录已接通，可以开始开发业务模块。</p>
    </div>
    <el-button :loading="loading" type="primary" @click="loadHealth">刷新连接状态</el-button>
  </section>

  <section class="status-grid">
    <el-card shadow="never" class="status-card">
      <template #header><span>后端服务</span></template>
      <div class="status-value" :class="health?.data?.application ? 'ok' : 'muted'">
        <span class="dot"></span>
        {{ health?.data?.application ? '运行中' : '检查中' }}
      </div>
      <p class="meta">Spring Boot 3.5.3</p>
    </el-card>

    <el-card shadow="never" class="status-card">
      <template #header><span>MySQL 数据库</span></template>
      <div class="status-value" :class="health?.data?.database === 'UP' ? 'ok' : 'danger'">
        <span class="dot"></span>
        {{ health?.data?.database || '检查中' }}
      </div>
      <p class="meta">数据库：{{ health?.data?.databaseName || 'user' }}</p>
    </el-card>

    <el-card shadow="never" class="status-card">
      <template #header><span>运行日志</span></template>
      <div class="status-value ok"><span class="dot"></span>已启用</div>
      <p class="meta">{{ health?.data?.logFile || 'logs/chengtai-fashion-mgmt.log' }}</p>
    </el-card>

    <el-card shadow="never" class="status-card">
      <template #header><span>附件目录</span></template>
      <div class="status-value ok"><span class="dot"></span>已启用</div>
      <p class="meta">{{ health?.data?.attachmentRoot || './attachments' }}</p>
    </el-card>
  </section>

  <el-card shadow="never" class="panel">
    <template #header>
      <div class="panel-title">
        <span>附件上传测试</span>
        <el-tag type="info">POST /api/files/upload</el-tag>
      </div>
    </template>
    <el-upload
      drag
      :http-request="handleUpload"
      :show-file-list="false"
      :disabled="uploading"
    >
      <el-icon class="upload-icon"><UploadFilled /></el-icon>
      <div class="el-upload__text">拖拽文件到此处，或 <em>点击上传</em></div>
      <template #tip>
        <div class="upload-tip">文件保存到 attachments/年/月/日，返回文件路径和访问地址。</div>
      </template>
    </el-upload>
    <div v-if="uploadResult" class="upload-result">
      <strong>上传成功</strong>
      <span>{{ uploadResult.originalName }}</span>
      <span class="path">{{ uploadResult.relativePath }}</span>
    </div>
  </el-card>

  <el-card shadow="never" class="panel">
    <template #header><span>健康检查返回</span></template>
    <pre class="json-view">{{ JSON.stringify(health, null, 2) }}</pre>
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'
import { getHealth, uploadAttachment } from '../api/system'

const loading = ref(false)
const uploading = ref(false)
const health = ref(null)
const uploadResult = ref(null)

async function loadHealth() {
  loading.value = true
  try {
    const response = await getHealth()
    health.value = response.data
    if (response.data?.code !== 0) {
      ElMessage.warning(response.data?.message || '健康检查返回异常')
    }
  } catch (error) {
    ElMessage.error(error?.message || '无法连接后端服务')
  } finally {
    loading.value = false
  }
}

async function handleUpload(options) {
  uploading.value = true
  try {
    const response = await uploadAttachment(options.file)
    uploadResult.value = response.data?.data
    ElMessage.success('附件上传成功')
    await loadHealth()
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || error?.message || '附件上传失败')
  } finally {
    uploading.value = false
  }
}

onMounted(loadHealth)
</script>
