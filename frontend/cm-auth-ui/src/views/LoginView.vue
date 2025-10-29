<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { Lock, User } from '@element-plus/icons-vue'
import { useAuthStore, type UserProfile } from '../stores/auth'

type LoginFormModel = {
  account: string
  password: string
}

const router = useRouter()
const authStore = useAuthStore()

const formRef = ref<FormInstance>()
const formModel = reactive<LoginFormModel>({
  account: '',
  password: ''
})

const rules: FormRules<LoginFormModel> = {
  account: [{ required: true, message: '请输入用户名/邮箱/手机号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const loading = ref(false)
const loginEndpoint = 'http://127.0.0.1:8001/v1/login/account'

const toRecord = (value: unknown): Record<string, unknown> => {
  if (value && typeof value === 'object' && !Array.isArray(value)) {
    return value as Record<string, unknown>
  }
  return {}
}

const resolveExpiryDate = (input?: unknown): Date => {
  const now = Date.now()
  if (typeof input === 'number' && Number.isFinite(input)) {
    if (input > 1e12) {
      return new Date(input)
    }
    return new Date(now + input * 1000)
  }
  if (typeof input === 'string') {
    const numeric = Number(input)
    if (Number.isFinite(numeric)) {
      return resolveExpiryDate(numeric)
    }
  }
  return new Date(now + 24 * 60 * 60 * 1000)
}

const setCookie = (name: string, value?: string, expiresAt?: unknown) => {
  if (!value) return
  const expiry = resolveExpiryDate(expiresAt)
  let cookie = `${name}=${encodeURIComponent(value)}; expires=${expiry.toUTCString()}; path=/; SameSite=Lax`
  if (window.location.protocol === 'https:') {
    cookie += '; Secure'
  }
  document.cookie = cookie
}

const handleSubmit = async () => {
  if (loading.value) return
  const formInstance = formRef.value
  if (!formInstance) return

  try {
    await formInstance.validate()
  } catch (_validationError) {
    return
  }

  loading.value = true

  const formData = new FormData()
  formData.append('account', formModel.account.trim())
  formData.append('password', formModel.password)

  try {
    const response = await fetch(loginEndpoint, {
      method: 'POST',
      body: formData,
      credentials: 'include'
    })

    if (!response.ok) {
      const reason = await response.text()
      throw new Error(reason || '登录失败，请检查账号或密码')
    }

    const contentType = response.headers.get('content-type') || ''
    let payload: Record<string, unknown> = {}

    if (contentType.includes('application/json')) {
      payload = (await response.json()) as Record<string, unknown>
    } else {
      const rawText = await response.text()
      if (rawText) {
        try {
          payload = JSON.parse(rawText) as Record<string, unknown>
        } catch (_err) {
          payload = { message: rawText }
        }
      }
    }

    const responseBody = toRecord(payload)
    const codeValue = 'code' in responseBody ? Number(responseBody.code) : 0
    if (Number.isFinite(codeValue) && codeValue !== 0) {
      throw new Error(String(responseBody.msg ?? '登录失败，请稍后再试'))
    }

    const payloadData = toRecord(responseBody.data ?? responseBody)

    const accessToken = payloadData.accessToken ? String(payloadData.accessToken) : undefined
    const refreshToken = payloadData.refreshToken ? String(payloadData.refreshToken) : undefined
    const idToken = payloadData.idToken ? String(payloadData.idToken) : undefined
    const expiresIn = payloadData.expiresIn

    setCookie('accessToken', accessToken, expiresIn)
    setCookie('refreshToken', refreshToken, expiresIn)
    setCookie('idToken', idToken, expiresIn)

    const userProfile: UserProfile = {
      account: String(
        payloadData.username ?? payloadData.account ?? formModel.account
      ),
      displayName: String(
        payloadData.displayName ??
          payloadData.name ??
          payloadData.username ??
          formModel.account
      ),
      email: payloadData.email ? String(payloadData.email) : undefined,
      mobile: payloadData.mobile ? String(payloadData.mobile) : undefined,
      lastLoginAt: payloadData.lastLoginAt
        ? String(payloadData.lastLoginAt)
        : new Date().toISOString(),
      token: accessToken
    }

    authStore.setUser(userProfile)
    ElMessage.success('登录成功，正在跳转...')
    await router.push({ name: 'profile' })
  } catch (error) {
    const message = error instanceof Error ? error.message : '登录失败，请稍后再试'
    ElMessage.error(message)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <el-card class="login-card" shadow="hover">
      <div class="login-card__header">
        <img src="/m_logo.svg" alt="Logo" class="login-card__logo" />
        <div>
          <h1>统一身份认证</h1>
          <p>请输入账号和密码以访问单点登录服务</p>
        </div>
      </div>

      <el-form ref="formRef" :model="formModel" :rules="rules" label-width="0">
        <el-form-item prop="account">
          <el-input
            v-model="formModel.account"
            autocomplete="username"
            placeholder="请输入用户名/邮箱/手机号"
            size="large"
            clearable
          >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="formModel.password"
            type="password"
            autocomplete="current-password"
            placeholder="请输入密码"
            size="large"
            show-password
            clearable
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-card__submit"
            :loading="loading"
            :disabled="loading"
            @click="handleSubmit"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: radial-gradient(circle at top, #eef2ff 0%, #e0f2fe 40%, #f8fafc 100%);
  padding: 2rem;
}

.login-card {
  width: min(420px, 100%);
  padding: 2rem 2.5rem;
  border-radius: 1.25rem;
  box-shadow: 0 40px 90px rgba(15, 23, 42, 0.08);
  border: none;
}

.login-card__header {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.login-card__logo {
  width: 64px;
  height: 64px;
}

.login-card__header h1 {
  margin: 0 0 0.5rem;
  font-size: 1.8rem;
  font-weight: 600;
  color: #1e293b;
}

.login-card__header p {
  margin: 0;
  color: #64748b;
  font-size: 0.95rem;
}

.login-card__submit {
  width: 100%;
  border-radius: 999px;
}
</style>
