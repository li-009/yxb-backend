<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="logo">🦜</div>
        <h1>鹦学伴</h1>
        <p>商户中心</p>
      </div>
      
      <el-form ref="formRef" :model="form" :rules="rules" class="login-form">
        <el-form-item prop="username">
          <el-input 
            v-model="form.username" 
            placeholder="请输入商户账号"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="form.password" 
            type="password" 
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <div class="form-options">
            <el-checkbox v-model="form.remember">记住我</el-checkbox>
            <el-link type="primary" @click="handleRegister">申请入驻</el-link>
          </div>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            :loading="loading" 
            class="login-btn"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-footer">
        <span>测试账号: merchant / 123456</span>
      </div>
    </div>
    
    <div class="login-bg">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: 'merchant',
  password: '123456',
  remember: true
})

const rules = {
  username: [{ required: true, message: '请输入商户账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  
  loading.value = true
  
  setTimeout(() => {
    if (form.username === 'merchant' && form.password === '123456') {
      localStorage.setItem('merchant_token', 'mock_merchant_token_' + Date.now())
      localStorage.setItem('merchant_user', JSON.stringify({ 
        id: 1, 
        username: 'merchant', 
        merchantName: '优学教育',
        contact: '张老师',
        phone: '138****1234'
      }))
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error('账号或密码错误')
    }
    loading.value = false
  }, 800)
}

const handleRegister = () => {
  ElMessage.info('商户入驻功能开发中')
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  position: relative;
  overflow: hidden;
}

.login-card {
  width: 400px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  position: relative;
  z-index: 10;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
  
  .logo {
    font-size: 64px;
    margin-bottom: 10px;
  }
  
  h1 {
    font-size: 28px;
    font-weight: 600;
    color: #333;
    margin: 0 0 8px;
  }
  
  p {
    font-size: 14px;
    color: #11998e;
    margin: 0;
    font-weight: 500;
  }
}

.login-form {
  .el-form-item {
    margin-bottom: 24px;
  }
  
  .form-options {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  border: none;
  
  &:hover {
    opacity: 0.9;
  }
}

.login-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 12px;
  color: #999;
}

.login-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  
  .shape {
    position: absolute;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.1);
  }
  
  .shape-1 {
    width: 500px;
    height: 500px;
    top: -150px;
    right: -150px;
  }
  
  .shape-2 {
    width: 400px;
    height: 400px;
    bottom: -100px;
    left: -100px;
  }
}
</style>
