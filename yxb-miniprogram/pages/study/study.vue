<template>
  <view class="study">
    <!-- 学习统计 -->
    <view class="stats-card">
      <view class="stats-item">
        <text class="stats-value">{{ stats.studyDays }}</text>
        <text class="stats-label">学习天数</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-value">{{ stats.wordCount }}</text>
        <text class="stats-label">掌握单词</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-value">{{ stats.videoCount }}</text>
        <text class="stats-label">学习视频</text>
      </view>
    </view>

    <!-- 快捷入口 -->
    <view class="quick-actions">
      <view class="action-item" @click="goToWords">
        <text class="action-icon">📖</text>
        <text class="action-name">单词本</text>
        <text class="action-badge" v-if="reviewCount > 0">{{ reviewCount }}</text>
      </view>
      <view class="action-item" @click="goToReview">
        <text class="action-icon">🔄</text>
        <text class="action-name">今日复习</text>
      </view>
      <view class="action-item" @click="goToNotes">
        <text class="action-icon">📝</text>
        <text class="action-name">我的笔记</text>
      </view>
      <view class="action-item" @click="goToFavorites">
        <text class="action-icon">⭐</text>
        <text class="action-name">收藏夹</text>
      </view>
    </view>

    <!-- 学习进度 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">继续学习</text>
        <text class="section-more" @click="goToAllProgress">全部 ></text>
      </view>
      <view class="progress-list" v-if="progressList.length > 0">
        <view class="progress-card" v-for="item in progressList" :key="item.id" @click="continueStudy(item)">
          <image class="progress-cover" :src="item.coverUrl" mode="aspectFill"></image>
          <view class="progress-info">
            <text class="progress-title">{{ item.videoTitle }}</text>
            <view class="progress-bar-wrap">
              <view class="progress-bar" :style="{ width: getProgressPercent(item) + '%' }"></view>
            </view>
            <text class="progress-text">已学习 {{ formatDuration(item.watchedDuration) }}</text>
          </view>
        </view>
      </view>
      <view class="empty-tip" v-else>
        <text>暂无学习记录，去首页发现好视频吧~</text>
      </view>
    </view>

    <!-- 待复习单词 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">待复习单词</text>
        <text class="section-more" @click="goToWords">更多 ></text>
      </view>
      <view class="word-list" v-if="reviewWords.length > 0">
        <view class="word-card" v-for="word in reviewWords" :key="word.id" @click="showWordDetail(word)">
          <view class="word-main">
            <text class="word-text">{{ word.word }}</text>
            <text class="word-phonetic">{{ word.phonetic }}</text>
          </view>
          <text class="word-meaning">{{ word.meaning }}</text>
        </view>
      </view>
      <view class="empty-tip" v-else>
        <text>暂无待复习单词</text>
      </view>
    </view>
  </view>
</template>

<script>
import { getProgressList, getReviewWords } from '../../api/study'

export default {
  data() {
    return {
      stats: {
        studyDays: 0,
        wordCount: 0,
        videoCount: 0
      },
      reviewCount: 0,
      progressList: [],
      reviewWords: []
    }
  },
  onShow() {
    this.loadData()
  },
  methods: {
    async loadData() {
      await Promise.all([
        this.loadProgress(),
        this.loadReviewWords()
      ])
    },
    async loadProgress() {
      try {
        const res = await getProgressList({ pageNum: 1, pageSize: 5 })
        if (res.code === 200) {
          this.progressList = res.data?.records || []
          this.stats.videoCount = res.data?.total || 0
        }
      } catch (e) {
        console.error('加载学习进度失败', e)
      }
    },
    async loadReviewWords() {
      try {
        const res = await getReviewWords(10)
        if (res.code === 200) {
          this.reviewWords = res.data || []
          this.reviewCount = this.reviewWords.length
        }
      } catch (e) {
        console.error('加载复习单词失败', e)
      }
    },
    getProgressPercent(item) {
      if (!item.totalDuration || item.totalDuration === 0) return 0
      return Math.min(100, Math.round((item.watchedDuration / item.totalDuration) * 100))
    },
    formatDuration(seconds) {
      if (!seconds) return '0分钟'
      const minutes = Math.floor(seconds / 60)
      if (minutes < 60) return `${minutes}分钟`
      const hours = Math.floor(minutes / 60)
      return `${hours}小时${minutes % 60}分钟`
    },
    continueStudy(item) {
      uni.navigateTo({
        url: `/pages/video/video?id=${item.videoId}&position=${item.position}`
      })
    },
    showWordDetail(word) {
      uni.showModal({
        title: word.word,
        content: `${word.phonetic}\n${word.meaning}\n\n例句：${word.example || '暂无'}`,
        showCancel: false
      })
    },
    goToWords() {
      uni.navigateTo({ url: '/pages/word/word' })
    },
    goToReview() {
      uni.navigateTo({ url: '/pages/word/word?mode=review' })
    },
    goToNotes() {
      uni.showToast({ title: '功能开发中', icon: 'none' })
    },
    goToFavorites() {
      uni.showToast({ title: '功能开发中', icon: 'none' })
    },
    goToAllProgress() {
      uni.showToast({ title: '功能开发中', icon: 'none' })
    }
  }
}
</script>

<style>
.study {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

.stats-card {
  display: flex;
  justify-content: space-around;
  align-items: center;
  background: linear-gradient(135deg, #4CAF50, #8BC34A);
  margin: 20rpx;
  padding: 40rpx 20rpx;
  border-radius: 16rpx;
}

.stats-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stats-value {
  font-size: 48rpx;
  font-weight: 600;
  color: #ffffff;
}

.stats-label {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 8rpx;
}

.stats-divider {
  width: 1rpx;
  height: 60rpx;
  background-color: rgba(255, 255, 255, 0.3);
}

.quick-actions {
  display: flex;
  justify-content: space-around;
  background-color: #ffffff;
  margin: 0 20rpx;
  padding: 32rpx 20rpx;
  border-radius: 16rpx;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.action-icon {
  font-size: 48rpx;
}

.action-name {
  font-size: 24rpx;
  color: #666666;
  margin-top: 8rpx;
}

.action-badge {
  position: absolute;
  top: -8rpx;
  right: -16rpx;
  background-color: #ff4444;
  color: #ffffff;
  font-size: 20rpx;
  padding: 4rpx 12rpx;
  border-radius: 20rpx;
}

.section {
  margin-top: 20rpx;
  background-color: #ffffff;
  padding: 24rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333333;
}

.section-more {
  font-size: 24rpx;
  color: #999999;
}

.progress-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.progress-card {
  display: flex;
  background-color: #fafafa;
  border-radius: 12rpx;
  overflow: hidden;
}

.progress-cover {
  width: 180rpx;
  height: 120rpx;
  flex-shrink: 0;
}

.progress-info {
  flex: 1;
  padding: 16rpx;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.progress-title {
  font-size: 28rpx;
  font-weight: 500;
  color: #333333;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.progress-bar-wrap {
  height: 8rpx;
  background-color: #e0e0e0;
  border-radius: 4rpx;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background-color: #4CAF50;
  border-radius: 4rpx;
}

.progress-text {
  font-size: 22rpx;
  color: #999999;
}

.word-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.word-card {
  background-color: #fafafa;
  padding: 20rpx;
  border-radius: 12rpx;
}

.word-main {
  display: flex;
  align-items: baseline;
  gap: 16rpx;
}

.word-text {
  font-size: 32rpx;
  font-weight: 600;
  color: #333333;
}

.word-phonetic {
  font-size: 24rpx;
  color: #999999;
}

.word-meaning {
  font-size: 26rpx;
  color: #666666;
  margin-top: 8rpx;
}

.empty-tip {
  text-align: center;
  padding: 40rpx;
  color: #999999;
  font-size: 26rpx;
}
</style>
