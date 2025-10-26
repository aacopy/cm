<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const user = computed(() => authStore.user.value)
const displayName = computed(
  () => user.value?.displayName || user.value?.account || '用户'
)
const avatarInitial = computed(() => displayName.value.charAt(0).toUpperCase())

onMounted(() => {
  if (!authStore.isAuthenticated.value) {
    router.replace({ name: 'login' })
  }
})

const handleLogout = () => {
  authStore.clearUser()
  ElMessage.success('您已退出登录')
  router.push({ name: 'login' })
}
</script>

<template>
  <div class="profile-page">
    <el-card class="profile-card" v-if="user">
      <div class="profile-card__header">
        <el-avatar size="large">{{ avatarInitial }}</el-avatar>
        <div>
          <h1>{{ displayName }}</h1>
          <p>欢迎回来，{{ user.account }}</p>
        </div>
        <el-button class="profile-card__logout" type="danger" plain @click="handleLogout">
          退出登录
        </el-button>
      </div>

      <el-divider />

      <el-descriptions :column="1" border label-class-name="profile-label">
        <el-descriptions-item label="账号">{{ user.account }}</el-descriptions-item>
        <el-descriptions-item v-if="user.email" label="邮箱">{{ user.email }}</el-descriptions-item>
        <el-descriptions-item v-if="user.mobile" label="手机号">{{ user.mobile }}</el-descriptions-item>
        <el-descriptions-item v-if="user.lastLoginAt" label="最近登录时间">
          {{ new Date(user.lastLoginAt).toLocaleString() }}
        </el-descriptions-item>
        <el-descriptions-item v-if="user.token" label="访问令牌">
          <el-tag size="small" type="info" class="token-tag">{{ user.token }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-result
      v-else
      icon="warning"
      title="未检测到登录信息"
      sub-title="请重新登录后访问个人中心"
    >
      <template #extra>
        <el-button type="primary" @click="router.push({ name: 'login' })">
          返回登录
        </el-button>
      </template>
    </el-result>
  </div>
</template>

<style scoped>
.profile-page {
  min-height: 100vh;
  padding: 3rem 1.5rem;
  background: radial-gradient(circle at top, #eef2ff 0%, #e0f2fe 45%, #f8fafc 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.profile-card {
  width: min(720px, 100%);
  border-radius: 1.25rem;
  padding: 2.5rem;
  box-shadow: 0 30px 80px rgba(15, 23, 42, 0.08);
  border: none;
}

.profile-card__header {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.profile-card__header h1 {
  margin: 0;
  font-size: 1.8rem;
  font-weight: 600;
  color: #1e293b;
}

.profile-card__header p {
  margin: 0.25rem 0 0;
  color: #64748b;
}

.profile-card__logout {
  margin-left: auto;
}

.profile-label {
  width: 120px;
  font-weight: 600;
}

.token-tag {
  font-family: 'Fira Code', 'Consolas', 'Courier New', monospace;
  letter-spacing: 0.5px;
}
</style>
