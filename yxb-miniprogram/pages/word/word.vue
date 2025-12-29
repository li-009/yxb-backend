<template>
  <view class="word-page">
    <!-- 顶部标签 -->
    <view class="tabs">
      <view class="tab" :class="{ active: currentTab === 'all' }" @click="switchTab('all')">
        全部
      </view>
      <view class="tab" :class="{ active: currentTab === 'review' }" @click="switchTab('review')">
        待复习
      </view>
      <view class="tab" :class="{ active: currentTab === 'mastered' }" @click="switchTab('mastered')">
        已掌握
      </view>
    </view>

    <!-- 单词列表 -->
    <scroll-view class="word-list" scroll-y @scrolltolower="loadMore">
      <view class="word-card" v-for="word in wordList" :key="word.id" @click="showWordDetail(word)">
        <view class="word-header">
          <text class="word-text">{{ word.word }}</text>
          <text class="word-phonetic">{{ word.phonetic }}</text>
          <view class="word-status" :class="getMasteryClass(word.masteryStatus)">
            {{ getMasteryText(word.masteryStatus) }}
          </view>
        </view>
        <text class="word-meaning">{{ word.meaning }}</text>
        <view class="word-footer">
          <text class="word-language">{{ getLanguageName(word.language) }}</text>
          <text class="word-date">{{ formatDate(word.createTime) }}</text>
        </view>
        <view class="word-actions">
          <button class="action-btn" @click.stop="playPronunciation(word)">🔊</button>
          <button class="action-btn" @click.stop="markAsReviewed(word)">✓</button>
          <button class="action-btn danger" @click.stop="deleteWord(word)">🗑️</button>
        </view>
      </view>

      <view class="load-more" v-if="loading">
        <text>加载中...</text>
      </view>
      <view class="no-more" v-else-if="!hasMore && wordList.length > 0">
        <text>已加载全部</text>
      </view>
      <view class="empty" v-else-if="wordList.length === 0">
        <text class="empty-icon">📖</text>
        <text class="empty-text">暂无单词</text>
        <text class="empty-tip">在视频学习中点击单词可添加到单词本</text>
      </view>
    </scroll-view>

    <!-- 单词详情弹窗 -->
    <view class="word-modal" v-if="showDetail" @click="showDetail = false">
      <view class="word-detail-panel" @click.stop>
        <view class="detail-header">
          <text class="detail-word">{{ selectedWord.word }}</text>
          <text class="detail-phonetic">{{ selectedWord.phonetic }}</text>
        </view>
        <view class="detail-section">
          <text class="detail-label">释义</text>
          <text class="detail-content">{{ selectedWord.meaning }}</text>
        </view>
        <view class="detail-section" v-if="selectedWord.example">
          <text class="detail-label">例句</text>
          <text class="detail-content">{{ selectedWord.example }}</text>
        </view>
        <view class="detail-actions">
          <button class="detail-btn" @click="playPronunciation(selectedWord)">播放发音</button>
          <button class="detail-btn" :class="{ success: selectedWord.masteryStatus === 2 }" @click="toggleMastery">
            {{ selectedWord.masteryStatus === 2 ? '已掌握' : '标记为已掌握' }}
          </button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getWordList, updateWordMastery } from '../../api/study'

export default {
  data() {
    return {
      currentTab: 'all',
      wordList: [],
      pageNum: 1,
      pageSize: 20,
      hasMore: true,
      loading: false,
      showDetail: false,
      selectedWord: {}
    }
  },
  onLoad(options) {
    if (options.mode === 'review') {
      this.currentTab = 'review'
    }
    this.loadWords()
  },
  methods: {
    switchTab(tab) {
      if (this.currentTab === tab) return
      this.currentTab = tab
      this.pageNum = 1
      this.wordList = []
      this.hasMore = true
      this.loadWords()
    },
    async loadWords() {
      if (this.loading) return
      this.loading = true
      try {
        let masteryStatus = undefined
        if (this.currentTab === 'review') {
          masteryStatus = 1 // 复习中
        } else if (this.currentTab === 'mastered') {
          masteryStatus = 2 // 已掌握
        }
        const res = await getWordList({
          masteryStatus,
          pageNum: this.pageNum,
          pageSize: this.pageSize
        })
        if (res.code === 200) {
          const records = res.data?.records || []
          if (this.pageNum === 1) {
            this.wordList = records
          } else {
            this.wordList = [...this.wordList, ...records]
          }
          this.hasMore = records.length >= this.pageSize
        }
      } catch (e) {
        console.error('加载单词失败', e)
      } finally {
        this.loading = false
      }
    },
    loadMore() {
      if (this.hasMore && !this.loading) {
        this.pageNum++
        this.loadWords()
      }
    },
    showWordDetail(word) {
      this.selectedWord = word
      this.showDetail = true
    },
    getMasteryClass(status) {
      const classes = { 0: 'new', 1: 'reviewing', 2: 'mastered' }
      return classes[status] || 'new'
    },
    getMasteryText(status) {
      const texts = { 0: '未学习', 1: '复习中', 2: '已掌握' }
      return texts[status] || '未学习'
    },
    getLanguageName(code) {
      const names = { en: '英语', ja: '日语', ko: '韩语', fr: '法语', de: '德语', es: '西班牙语' }
      return names[code] || code
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return `${date.getMonth() + 1}/${date.getDate()}`
    },
    playPronunciation(word) {
      uni.showToast({ title: '播放发音功能开发中', icon: 'none' })
    },
    async markAsReviewed(word) {
      const newStatus = word.masteryStatus === 2 ? 1 : 2
      try {
        await updateWordMastery(word.id, newStatus)
        word.masteryStatus = newStatus
        uni.showToast({ title: newStatus === 2 ? '已标记为掌握' : '已移至复习', icon: 'success' })
      } catch (e) {
        uni.showToast({ title: '操作失败', icon: 'none' })
      }
    },
    async toggleMastery() {
      await this.markAsReviewed(this.selectedWord)
    },
    deleteWord(word) {
      uni.showModal({
        title: '确认删除',
        content: `确定要删除单词 "${word.word}" 吗？`,
        success: (res) => {
          if (res.confirm) {
            // TODO: 调用删除接口
            const index = this.wordList.findIndex(w => w.id === word.id)
            if (index >= 0) {
              this.wordList.splice(index, 1)
            }
            uni.showToast({ title: '已删除', icon: 'success' })
          }
        }
      })
    }
  }
}
</script>

<style>
.word-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.tabs {
  display: flex;
  background-color: #ffffff;
  border-bottom: 1rpx solid #f0f0f0;
}

.tab {
  flex: 1;
  text-align: center;
  padding: 28rpx 0;
  font-size: 28rpx;
  color: #666666;
  position: relative;
}

.tab.active {
  color: #4CAF50;
  font-weight: 600;
}

.tab.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60rpx;
  height: 4rpx;
  background-color: #4CAF50;
  border-radius: 2rpx;
}

.word-list {
  height: calc(100vh - 100rpx);
  padding: 20rpx;
}

.word-card {
  background-color: #ffffff;
  border-radius: 12rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  position: relative;
}

.word-header {
  display: flex;
  align-items: baseline;
  gap: 16rpx;
  margin-bottom: 12rpx;
}

.word-text {
  font-size: 36rpx;
  font-weight: 600;
  color: #333333;
}

.word-phonetic {
  font-size: 26rpx;
  color: #999999;
}

.word-status {
  margin-left: auto;
  font-size: 22rpx;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.word-status.new {
  background-color: #f5f5f5;
  color: #999999;
}

.word-status.reviewing {
  background-color: #fff3e0;
  color: #ff9800;
}

.word-status.mastered {
  background-color: #e8f5e9;
  color: #4CAF50;
}

.word-meaning {
  font-size: 28rpx;
  color: #666666;
  display: block;
  margin-bottom: 12rpx;
}

.word-footer {
  display: flex;
  gap: 16rpx;
}

.word-language,
.word-date {
  font-size: 22rpx;
  color: #999999;
}

.word-actions {
  position: absolute;
  right: 24rpx;
  bottom: 24rpx;
  display: flex;
  gap: 12rpx;
}

.action-btn {
  width: 56rpx;
  height: 56rpx;
  border-radius: 28rpx;
  background-color: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  padding: 0;
  margin: 0;
  line-height: 56rpx;
}

.action-btn.danger {
  background-color: #ffebee;
}

.load-more,
.no-more {
  text-align: center;
  padding: 24rpx;
  color: #999999;
  font-size: 24rpx;
}

.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 100rpx;
}

.empty-text {
  font-size: 32rpx;
  color: #333333;
  margin-top: 24rpx;
}

.empty-tip {
  font-size: 26rpx;
  color: #999999;
  margin-top: 12rpx;
}

.word-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.word-detail-panel {
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 40rpx;
  width: 85%;
}

.detail-header {
  text-align: center;
  margin-bottom: 32rpx;
}

.detail-word {
  display: block;
  font-size: 48rpx;
  font-weight: 600;
  color: #333333;
}

.detail-phonetic {
  display: block;
  font-size: 28rpx;
  color: #999999;
  margin-top: 8rpx;
}

.detail-section {
  margin-bottom: 24rpx;
}

.detail-label {
  display: block;
  font-size: 24rpx;
  color: #999999;
  margin-bottom: 8rpx;
}

.detail-content {
  font-size: 30rpx;
  color: #333333;
  line-height: 1.6;
}

.detail-actions {
  display: flex;
  gap: 20rpx;
  margin-top: 32rpx;
}

.detail-btn {
  flex: 1;
  background-color: #4CAF50;
  color: #ffffff;
  font-size: 28rpx;
  border-radius: 8rpx;
}

.detail-btn.success {
  background-color: #e8f5e9;
  color: #4CAF50;
}
</style>
