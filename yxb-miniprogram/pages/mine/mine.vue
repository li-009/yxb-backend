<template>
  <view class="mine">
    <!-- 用户信息 -->
    <view class="user-card">
      <view class="user-info" v-if="isLogin">
        <image class="avatar" :src="userInfo.avatar || '/static/default-avatar.png'" mode="aspectFill"></image>
        <view class="user-detail">
          <text class="nickname">{{ userInfo.nickname || '鹦学伴用户' }}</text>
          <text class="level">Lv.{{ userInfo.level || 1 }} {{ getLevelName(userInfo.level) }}</text>
        </view>
      </view>
      <view class="user-info" v-else @click="goLogin">
        <view class="avatar-placeholder">
          <text>👤</text>
        </view>
        <view class="user-detail">
          <text class="nickname">点击登录</text>
          <text class="level">登录后同步学习数据</text>
        </view>
      </view>
      <view class="user-stats" v-if="isLogin">
        <view class="stat-item">
          <text class="stat-value">{{ userInfo.studyDays || 0 }}</text>
          <text class="stat-label">学习天数</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ userInfo.totalMinutes || 0 }}</text>
          <text class="stat-label">学习分钟</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ userInfo.wordCount || 0 }}</text>
          <text class="stat-label">掌握单词</text>
        </view>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="menu-item" @click="goToHistory">
        <text class="menu-icon">📺</text>
        <text class="menu-text">播放历史</text>
        <text class="menu-arrow">></text>
      </view>
      <view class="menu-item" @click="goToDownloads">
        <text class="menu-icon">📥</text>
        <text class="menu-text">我的下载</text>
        <text class="menu-arrow">></text>
      </view>
      <view class="menu-item" @click="goToFavorites">
        <text class="menu-icon">⭐</text>
        <text class="menu-text">我的收藏</text>
        <text class="menu-arrow">></text>
      </view>
    </view>

    <view class="menu-section">
      <view class="menu-item" @click="goToSettings">
        <text class="menu-icon">⚙️</text>
        <text class="menu-text">设置</text>
        <text class="menu-arrow">></text>
      </view>
      <view class="menu-item" @click="goToFeedback">
        <text class="menu-icon">💬</text>
        <text class="menu-text">意见反馈</text>
        <text class="menu-arrow">></text>
      </view>
      <view class="menu-item" @click="goToAbout">
        <text class="menu-icon">ℹ️</text>
        <text class="menu-text">关于我们</text>
        <text class="menu-arrow">></text>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-section" v-if="isLogin">
      <button class="logout-btn" @click="logout">退出登录</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      isLogin: false,
      userInfo: {}
    }
  },
  onShow() {
    this.checkLogin()
  },
  methods: {
    checkLogin() {
      const token = uni.getStorageSync('token')
      const userInfo = uni.getStorageSync('userInfo')
      this.isLogin = !!token
      if (userInfo) {
        this.userInfo = JSON.parse(userInfo)
      }
    },
    getLevelName(level) {
      const names = ['新手学员', '初级学员', '中级学员', '高级学员', '资深学员', '学习达人']
      return names[Math.min(level || 1, 6) - 1]
    },
    goLogin() {
      uni.navigateTo({ url: '/pages/login/login' })
    },
    goToHistory() {
      uni.showToast({ title: '功能开发中', icon: 'none' })
    },
    goToDownloads() {
      uni.showToast({ title: '功能开发中', icon: 'none' })
    },
    goToFavorites() {
      uni.showToast({ title: '功能开发中', icon: 'none' })
    },
    goToSettings() {
      uni.showToast({ title: '功能开发中', icon: 'none' })
    },
    goToFeedback() {
      uni.showToast({ title: '功能开发中', icon: 'none' })
    },
    goToAbout() {
      uni.showModal({
        title: '关于鹦学伴',
        content: '鹦学伴 - 外语学习视频助手\n版本：1.0.0\n\n以视频为核心载体，结合AI智能辅助，提供"看-学-练-问-分享"全链路外语学习服务。',
        showCancel: false
      })
    },
    logout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            this.isLogin = false
            this.userInfo = {}
            uni.showToast({ title: '已退出登录', icon: 'success' })
          }
        }
      })
    }
  }
}
</script>

<style>
.mine {
  min-height: 100vh;
  background-color: #121212;
  padding-bottom: 120rpx;
}

.user-card {
  background: linear-gradient(135deg, #1a1a1a, #2a2a2a);
  padding: 40rpx 32rpx;
  margin-bottom: 20rpx;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 60rpx;
  border: 4rpx solid rgba(255, 255, 255, 0.5);
}

.avatar-placeholder {
  width: 120rpx;
  height: 120rpx;
  border-radius: 60rpx;
  background-color: rgba(255, 255, 255, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48rpx;
}

.user-detail {
  margin-left: 24rpx;
}

.nickname {
  display: block;
  font-size: 36rpx;
  font-weight: 600;
  color: #ffffff;
}

.level {
  display: block;
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 8rpx;
}

.user-stats {
  display: flex;
  justify-content: space-around;
  margin-top: 32rpx;
  padding-top: 32rpx;
  border-top: 1rpx solid rgba(255, 255, 255, 0.2);
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 40rpx;
  font-weight: 600;
  color: #ffffff;
}

.stat-label {
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 8rpx;
}

.menu-section {
  background-color: #1a1a1a;
  margin-bottom: 20rpx;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 32rpx;
  border-bottom: 1rpx solid #2a2a2a;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  font-size: 40rpx;
  margin-right: 24rpx;
}

.menu-text {
  flex: 1;
  font-size: 30rpx;
  color: #ffffff;
}

.menu-arrow {
  font-size: 28rpx;
  color: #666666;
}

.logout-section {
  padding: 40rpx 32rpx;
}

.logout-btn {
  background-color: #2a2a2a;
  color: #ff4444;
  font-size: 30rpx;
  border-radius: 12rpx;
  border: 1rpx solid #ff4444;
}
</style>
