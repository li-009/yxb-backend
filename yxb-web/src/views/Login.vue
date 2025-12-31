<template>
  <div class="login-page">
    <div class="login-card">
      <div class="logo">🦜 鹦学伴</div>
      <h2>登录</h2>
      <el-form :model="form" label-position="top">
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
      <div class="links">
        <a href="#">忘记密码</a>
        <a href="#">注册账号</a>
      </div>

      <div class="divider">
        <span>或</span>
      </div>

      <div class="social-login">
        <el-button class="wechat-btn" @click="handleWechatLogin">
          <span class="wechat-icon">📱</span>
          微信小程序登录
        </el-button>
        <p class="wechat-tip">在微信小程序中打开可使用微信登录</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../api/user'

const router = useRouter()
const form = ref({ phone: '', password: '' })
const loading = ref(false)

const isWechatMiniapp = () => {
  return typeof wx !== 'undefined' && wx.miniProgram
}

const handleWechatLogin = async () => {
  if (isWechatMiniapp()) {
    ElMessage.info('正在调用微信登录...')
  } else {
    ElMessage.info('请在微信小程序中打开使用微信登录')
  }
}

const handleLogin = async () => {
  if (!form.value.phone || !form.value.password) {
    ElMessage.warning('请输入手机号和密码')
    return
  }
  loading.value = true
  try {
    const res = await login(form.value.phone, form.value.password)
    if (res.data.code === 200) {
      const token = res.data.data.token
      localStorage.setItem('token', token)
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error(res.data.message || '登录失败')
    }
  } catch (e) {
    console.error('登录失败', e)
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0d1117, #161b22);
}

.login-card {
  width: 400px;
  background: #21262d;
  border-radius: 12px;
  padding: 40px;
  border: 1px solid #30363d;
}

.logo {
  font-size: 32px;
  text-align: center;
  margin-bottom: 24px;
}

h2 {
  text-align: center;
  margin-bottom: 32px;
  color: #c9d1d9;
}

.login-btn {
  width: 100%;
}

.links {
  display: flex;
  justify-content: space-between;
  margin-top: 16px;
  font-size: 14px;
}

.links a {
  color: #58a6ff;
}

.divider {
  display: flex;
  align-items: center;
  margin: 24px 0;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: #30363d;
}

.divider span {
  padding: 0 16px;
  color: #8b949e;
  font-size: 14px;
}

.social-login {
  text-align: center;
}

.wechat-btn {
  width: 100%;
  background: #07c160;
  border-color: #07c160;
  color: #fff;
  font-size: 16px;
  height: 44px;
}

.wechat-btn:hover {
  background: #06ad56;
  border-color: #06ad56;
}

.wechat-icon {
  margin-right: 8px;
}

.wechat-tip {
  margin-top: 12px;
  font-size: 12px;
  color: #8b949e;
}
</style>
