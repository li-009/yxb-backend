<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="layout-aside">
      <div class="logo">
        <span class="logo-icon">🦜</span>
        <span class="logo-text">商户中心</span>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        background-color="#001529"
        text-color="#ffffffb3"
        active-text-color="#ffffff"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <template #title>工作台</template>
        </el-menu-item>
        <el-menu-item index="/video">
          <el-icon><VideoCamera /></el-icon>
          <template #title>视频管理</template>
        </el-menu-item>
        <el-menu-item index="/income">
          <el-icon><Money /></el-icon>
          <template #title>收益管理</template>
        </el-menu-item>
        <el-menu-item index="/withdraw">
          <el-icon><Wallet /></el-icon>
          <template #title>提现管理</template>
        </el-menu-item>
        <el-menu-item index="/settings">
          <el-icon><Setting /></el-icon>
          <template #title>账户设置</template>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header class="layout-header">
        <div class="header-left">
          <span class="merchant-name">{{ merchantInfo.merchantName }}</span>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <div class="user-info">
              <el-avatar :size="32">{{ merchantInfo.contact?.charAt(0) || 'M' }}</el-avatar>
              <span class="user-name">{{ merchantInfo.contact || '商户' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="settings">账户设置</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)

const merchantInfo = computed(() => {
  try {
    return JSON.parse(localStorage.getItem('merchant_user') || '{}')
  } catch {
    return {}
  }
})

const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      type: 'warning'
    }).then(() => {
      localStorage.removeItem('merchant_token')
      localStorage.removeItem('merchant_user')
      ElMessage.success('退出成功')
      router.push('/login')
    }).catch(() => {})
  } else if (command === 'settings') {
    router.push('/settings')
  }
}
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
}

.layout-aside {
  background-color: #001529;
  
  .logo {
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    
    .logo-icon { font-size: 28px; }
    .logo-text {
      font-size: 16px;
      font-weight: 600;
      color: #fff;
    }
  }
  
  .el-menu { border-right: none; }
}

.layout-header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  
  .merchant-name {
    font-size: 16px;
    font-weight: 500;
    color: #333;
  }
  
  .user-info {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    
    .user-name {
      font-size: 14px;
      color: #333;
    }
  }
}

.layout-main {
  background: #f0f2f5;
  padding: 20px;
}
</style>
