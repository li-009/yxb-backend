<template>
  <div class="profile-page">
    <div class="profile-header">
      <div class="user-info">
        <el-avatar :size="80" class="avatar">
          <el-icon :size="40"><User /></el-icon>
        </el-avatar>
        <div class="info">
          <div class="name">{{ user.name }}</div>
          <div class="level">{{ user.language }} · {{ user.level }}</div>
        </div>
        <div class="vip-badge" v-if="user.isVip">VIP</div>
        <el-button v-else type="warning" size="small">开通VIP</el-button>
      </div>
    </div>

    <section class="stats-section">
      <div class="stat-card">
        <div class="stat-icon">⏱️</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.totalHours }}</div>
          <div class="stat-label">累计学习(小时)</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">📖</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.wordCount }}</div>
          <div class="stat-label">掌握单词</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">🎤</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.followReadCount }}</div>
          <div class="stat-label">跟读次数</div>
        </div>
      </div>
    </section>

    <section class="menu-section">
      <div class="menu-title">学习资产</div>
      <div class="menu-list">
        <div class="menu-item" @click="goTo('/notes')">
          <div class="menu-icon">📝</div>
          <span>我的笔记</span>
          <span class="menu-badge">{{ assets.noteCount }}</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="goTo('/favorites')">
          <div class="menu-icon">⭐</div>
          <span>我的收藏</span>
          <span class="menu-badge">{{ assets.favoriteCount }}</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="goTo('/words')">
          <div class="menu-icon">📚</div>
          <span>单词本</span>
          <span class="menu-badge">{{ assets.wordCount }}</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="goTo('/report')">
          <div class="menu-icon">📊</div>
          <span>学习报告</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="bindWechat">
          <div class="menu-icon">💬</div>
          <span>微信关联</span>
          <span class="menu-value">{{ user.wechatBound ? '已绑定' : '未绑定' }}</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
      </div>
    </section>

    <section class="menu-section">
      <div class="menu-title">系统设置</div>
      <div class="menu-list">
        <div class="menu-item" @click="goTo('/settings/account')">
          <div class="menu-icon">👤</div>
          <span>账号设置</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="goTo('/settings/player')">
          <div class="menu-icon">🎬</div>
          <span>播放设置</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="goTo('/settings/privacy')">
          <div class="menu-icon">🔒</div>
          <span>隐私设置</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="goTo('/help')">
          <div class="menu-icon">❓</div>
          <span>帮助中心</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="showAbout">
          <div class="menu-icon">ℹ️</div>
          <span>关于我们</span>
          <span class="menu-value">v1.0.0</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
      </div>
    </section>

    <div class="logout-btn">
      <el-button type="danger" plain @click="handleLogout">退出登录</el-button>
    </div>

    <nav class="bottom-nav">
      <div class="nav-item" @click="goTo('/')">
        <el-icon><HomeFilled /></el-icon>
        <span>首页</span>
      </div>
      <div class="nav-item" @click="goTo('/library')">
        <el-icon><FolderOpened /></el-icon>
        <span>资源库</span>
      </div>
      <div class="nav-item add-btn">
        <el-icon><Plus /></el-icon>
      </div>
      <div class="nav-item" @click="goTo('/study')">
        <el-icon><DataLine /></el-icon>
        <span>学习</span>
      </div>
      <div class="nav-item active">
        <el-icon><User /></el-icon>
        <span>我的</span>
      </div>
    </nav>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { User, ArrowRight, HomeFilled, FolderOpened, Plus, DataLine } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const user = ref({
  name: '学习者',
  language: '英语',
  level: '中级',
  isVip: false,
  wechatBound: false
})

const stats = ref({
  totalHours: 32,
  wordCount: 256,
  followReadCount: 128
})

const assets = ref({
  noteCount: 15,
  favoriteCount: 28,
  wordCount: 256
})

const goTo = (path) => {
  if (path.startsWith('/settings') || path === '/help') {
    ElMessage.info('功能开发中')
    return
  }
  router.push(path)
}

const bindWechat = () => {
  ElMessage.info('微信绑定功能开发中')
}

const showAbout = () => {
  ElMessageBox.alert('鹦学伴 v1.0.0\n\n看视频，学外语\n沉浸式外语学习助手', '关于我们', {
    confirmButtonText: '确定'
  })
}

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('已退出登录')
    router.push('/login')
  }).catch(() => {})
}
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: #0d1117;
  padding-bottom: 80px;
}

.profile-header {
  background: linear-gradient(135deg, #1f6feb, #238636);
  padding: 40px 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
  max-width: 600px;
  margin: 0 auto;
}

.avatar {
  background: rgba(255,255,255,0.2);
}

.info {
  flex: 1;
}

.name {
  font-size: 20px;
  font-weight: 600;
  color: #fff;
}

.level {
  font-size: 14px;
  color: rgba(255,255,255,0.8);
  margin-top: 4px;
}

.vip-badge {
  background: linear-gradient(135deg, #f0883e, #d29922);
  color: #fff;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

.stats-section {
  display: flex;
  gap: 12px;
  padding: 20px;
  margin-top: -20px;
}

.stat-card {
  flex: 1;
  background: #161b22;
  border: 1px solid #30363d;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.stat-icon {
  font-size: 24px;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #58a6ff;
}

.stat-label {
  font-size: 12px;
  color: #8b949e;
}

.menu-section {
  padding: 0 20px;
  margin-bottom: 20px;
}

.menu-title {
  font-size: 14px;
  color: #8b949e;
  margin-bottom: 12px;
  padding-left: 4px;
}

.menu-list {
  background: #161b22;
  border: 1px solid #30363d;
  border-radius: 12px;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  cursor: pointer;
  transition: background 0.2s;
  border-bottom: 1px solid #30363d;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item:hover {
  background: #21262d;
}

.menu-icon {
  font-size: 20px;
}

.menu-item span:first-of-type {
  flex: 1;
  font-size: 15px;
}

.menu-badge {
  background: #1f6feb33;
  color: #58a6ff;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.menu-value {
  font-size: 13px;
  color: #8b949e;
}

.logout-btn {
  padding: 20px;
  text-align: center;
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #161b22;
  border-top: 1px solid #30363d;
  display: flex;
  justify-content: space-around;
  padding: 8px 0;
  z-index: 100;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  color: #8b949e;
  cursor: pointer;
  padding: 8px 16px;
}

.nav-item:hover, .nav-item.active {
  color: #58a6ff;
}

.nav-item.add-btn {
  background: linear-gradient(135deg, #1f6feb, #238636);
  color: #fff;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  padding: 0;
  margin-top: -16px;
  font-size: 24px;
}

.nav-item span {
  font-size: 12px;
}
</style>
