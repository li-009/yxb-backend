<template>
  <view class="video-page">
    <!-- 自定义导航栏 -->
    <view class="nav-bar" :style="{ paddingTop: statusBarHeight + 'px' }">
      <view class="nav-back" @click="goBack">
        <text>←</text>
      </view>
      <text class="nav-title">{{ videoInfo.title || '视频播放' }}</text>
      <view class="nav-share" @click="shareVideo">
        <text>⋮</text>
      </view>
    </view>

    <!-- 视频播放器 -->
    <view class="player-wrap" :style="{ marginTop: navHeight + 'px' }">
      <video
        id="videoPlayer"
        class="video-player"
        :src="videoInfo.videoUrl"
        :poster="videoInfo.coverUrl"
        :initial-time="initialTime"
        :playback-rate="playbackRate"
        controls
        show-center-play-btn
        enable-progress-gesture
        @timeupdate="onTimeUpdate"
        @ended="onVideoEnd"
        @error="onVideoError"
      ></video>
    </view>

    <!-- 字幕显示区 -->
    <view class="subtitle-area" v-if="showSubtitle && currentSubtitle">
      <text class="subtitle-text" :class="{ 'subtitle-bilingual': subtitleMode === 'bilingual' }">
        {{ currentSubtitle.text }}
      </text>
      <text class="subtitle-translation" v-if="subtitleMode === 'bilingual' && currentSubtitle.translation">
        {{ currentSubtitle.translation }}
      </text>
    </view>

    <!-- 控制面板 -->
    <view class="control-panel">
      <view class="control-row">
        <view class="control-item" @click="toggleSubtitle">
          <text class="control-icon">📝</text>
          <text class="control-label">{{ showSubtitle ? '隐藏字幕' : '显示字幕' }}</text>
        </view>
        <view class="control-item" @click="switchSubtitleMode">
          <text class="control-icon">🌐</text>
          <text class="control-label">{{ getSubtitleModeText() }}</text>
        </view>
        <view class="control-item" @click="showSpeedPicker">
          <text class="control-icon">⏱️</text>
          <text class="control-label">{{ playbackRate }}x</text>
        </view>
        <view class="control-item" @click="toggleLoop">
          <text class="control-icon">🔁</text>
          <text class="control-label">{{ loopMode ? '循环中' : '单句循环' }}</text>
        </view>
      </view>
    </view>

    <!-- 字幕列表 -->
    <view class="subtitle-list-section">
      <view class="section-header">
        <text class="section-title">字幕列表</text>
        <text class="section-action" @click="exportSubtitles">导出字幕</text>
      </view>
      <scroll-view class="subtitle-scroll" scroll-y :scroll-into-view="scrollToId">
        <view
          class="subtitle-item"
          v-for="(item, index) in subtitles"
          :key="index"
          :id="'sub-' + index"
          :class="{ active: currentSubtitleIndex === index }"
          @click="seekToSubtitle(item)"
          @longpress="onSubtitleLongPress(item)"
        >
          <text class="subtitle-time">{{ formatTime(item.startTime) }}</text>
          <view class="subtitle-content">
            <text class="subtitle-original">{{ item.text }}</text>
            <text class="subtitle-trans" v-if="item.translation">{{ item.translation }}</text>
          </view>
          <view class="subtitle-actions">
            <text class="action-btn" @click.stop="lookupWord(item)">查词</text>
            <text class="action-btn" @click.stop="analyzeGrammar(item)">语法</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 倍速选择弹窗 -->
    <view class="speed-picker" v-if="showSpeedPanel" @click="showSpeedPanel = false">
      <view class="speed-panel" @click.stop>
        <text class="speed-title">播放速度</text>
        <view class="speed-options">
          <view
            class="speed-option"
            v-for="speed in speedOptions"
            :key="speed"
            :class="{ active: playbackRate === speed }"
            @click="setPlaybackRate(speed)"
          >
            {{ speed }}x
          </view>
        </view>
      </view>
    </view>

    <!-- AI功能弹窗 -->
    <view class="ai-modal" v-if="showAiModal" @click="showAiModal = false">
      <view class="ai-panel" @click.stop>
        <text class="ai-title">{{ aiTitle }}</text>
        <scroll-view class="ai-content" scroll-y>
          <text>{{ aiContent }}</text>
        </scroll-view>
        <view class="ai-actions">
          <button class="ai-btn" @click="addToWordBook" v-if="aiType === 'word'">加入单词本</button>
          <button class="ai-btn" @click="addToNotes" v-if="aiType === 'grammar'">添加笔记</button>
          <button class="ai-btn secondary" @click="showAiModal = false">关闭</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getVideoDetail, getVideoSubtitles, playVideo } from '../../api/video'
import { updateProgress, addWord } from '../../api/study'
import { lookupWord as apiLookupWord, analyzeGrammar as apiAnalyzeGrammar } from '../../api/ai'

export default {
  data() {
    return {
      statusBarHeight: 20,
      navHeight: 44,
      videoId: null,
      videoInfo: {},
      initialTime: 0,
      currentTime: 0,
      subtitles: [],
      currentSubtitle: null,
      currentSubtitleIndex: -1,
      scrollToId: '',
      showSubtitle: true,
      subtitleMode: 'bilingual', // original, translation, bilingual
      playbackRate: 1.0,
      speedOptions: [0.5, 0.75, 1.0, 1.25, 1.5, 2.0],
      showSpeedPanel: false,
      loopMode: false,
      loopSubtitle: null,
      showAiModal: false,
      aiTitle: '',
      aiContent: '',
      aiType: '',
      currentWord: null
    }
  },
  onLoad(options) {
    this.videoId = options.id
    if (options.position) {
      this.initialTime = parseInt(options.position) || 0
    }
    const systemInfo = uni.getSystemInfoSync()
    this.statusBarHeight = systemInfo.statusBarHeight
    this.navHeight = this.statusBarHeight + 44
    this.loadVideoData()
  },
  onUnload() {
    this.saveProgress()
  },
  methods: {
    async loadVideoData() {
      try {
        const [videoRes, subtitleRes] = await Promise.all([
          getVideoDetail(this.videoId),
          getVideoSubtitles(this.videoId)
        ])
        if (videoRes.code === 200) {
          this.videoInfo = videoRes.data
          playVideo(this.videoId)
        }
        if (subtitleRes.code === 200) {
          this.subtitles = subtitleRes.data || []
        }
      } catch (e) {
        console.error('加载视频数据失败', e)
        uni.showToast({ title: '加载失败', icon: 'none' })
      }
    },
    onTimeUpdate(e) {
      this.currentTime = e.detail.currentTime
      this.updateCurrentSubtitle()
      if (this.loopMode && this.loopSubtitle) {
        if (this.currentTime >= this.loopSubtitle.endTime) {
          const videoContext = uni.createVideoContext('videoPlayer', this)
          videoContext.seek(this.loopSubtitle.startTime)
        }
      }
    },
    updateCurrentSubtitle() {
      const time = this.currentTime
      const index = this.subtitles.findIndex(
        sub => time >= sub.startTime && time < sub.endTime
      )
      if (index !== this.currentSubtitleIndex) {
        this.currentSubtitleIndex = index
        this.currentSubtitle = index >= 0 ? this.subtitles[index] : null
        if (index >= 0) {
          this.scrollToId = 'sub-' + index
        }
      }
    },
    seekToSubtitle(subtitle) {
      const videoContext = uni.createVideoContext('videoPlayer', this)
      videoContext.seek(subtitle.startTime)
      videoContext.play()
    },
    onSubtitleLongPress(subtitle) {
      uni.showActionSheet({
        itemList: ['复制原文', '复制译文', '设为循环起点', '添加笔记'],
        success: (res) => {
          switch (res.tapIndex) {
            case 0:
              uni.setClipboardData({ data: subtitle.text })
              break
            case 1:
              uni.setClipboardData({ data: subtitle.translation || subtitle.text })
              break
            case 2:
              this.loopSubtitle = subtitle
              this.loopMode = true
              uni.showToast({ title: '已设置循环', icon: 'success' })
              break
            case 3:
              uni.showToast({ title: '功能开发中', icon: 'none' })
              break
          }
        }
      })
    },
    toggleSubtitle() {
      this.showSubtitle = !this.showSubtitle
    },
    switchSubtitleMode() {
      const modes = ['original', 'translation', 'bilingual']
      const currentIndex = modes.indexOf(this.subtitleMode)
      this.subtitleMode = modes[(currentIndex + 1) % modes.length]
    },
    getSubtitleModeText() {
      const texts = { original: '仅原文', translation: '仅译文', bilingual: '双语' }
      return texts[this.subtitleMode]
    },
    showSpeedPicker() {
      this.showSpeedPanel = true
    },
    setPlaybackRate(speed) {
      this.playbackRate = speed
      this.showSpeedPanel = false
    },
    toggleLoop() {
      if (this.loopMode) {
        this.loopMode = false
        this.loopSubtitle = null
        uni.showToast({ title: '已取消循环', icon: 'none' })
      } else if (this.currentSubtitle) {
        this.loopSubtitle = this.currentSubtitle
        this.loopMode = true
        uni.showToast({ title: '单句循环已开启', icon: 'success' })
      } else {
        uni.showToast({ title: '请先播放到有字幕的位置', icon: 'none' })
      }
    },
    async lookupWord(subtitle) {
      const word = await this.selectWord(subtitle.text)
      if (!word) return
      this.aiTitle = '单词查询'
      this.aiContent = '查询中...'
      this.aiType = 'word'
      this.showAiModal = true
      try {
        const res = await apiLookupWord(word, this.videoInfo.language || 'en')
        if (res.code === 200) {
          const data = res.data
          this.currentWord = { ...data, word }
          this.aiContent = `${data.word}\n${data.phonetic}\n\n释义：${data.meaning}\n\n例句：${data.example || '暂无'}`
        } else {
          this.aiContent = '查询失败，请稍后重试'
        }
      } catch (e) {
        this.aiContent = '查询失败：' + e.message
      }
    },
    async analyzeGrammar(subtitle) {
      this.aiTitle = '语法分析'
      this.aiContent = '分析中...'
      this.aiType = 'grammar'
      this.showAiModal = true
      try {
        const res = await apiAnalyzeGrammar(subtitle.text, this.videoInfo.language || 'en')
        if (res.code === 200) {
          this.aiContent = res.data.analysis || res.data
        } else {
          this.aiContent = '分析失败，请稍后重试'
        }
      } catch (e) {
        this.aiContent = '分析失败：' + e.message
      }
    },
    async selectWord(text) {
      return new Promise((resolve) => {
        uni.showModal({
          title: '输入要查询的单词',
          editable: true,
          placeholderText: '请输入单词',
          success: (res) => {
            if (res.confirm && res.content) {
              resolve(res.content.trim())
            } else {
              resolve(null)
            }
          }
        })
      })
    },
    async addToWordBook() {
      if (!this.currentWord) return
      try {
        await addWord({
          word: this.currentWord.word,
          phonetic: this.currentWord.phonetic,
          meaning: this.currentWord.meaning,
          example: this.currentWord.example,
          language: this.videoInfo.language || 'en'
        })
        uni.showToast({ title: '已加入单词本', icon: 'success' })
        this.showAiModal = false
      } catch (e) {
        uni.showToast({ title: '添加失败', icon: 'none' })
      }
    },
    addToNotes() {
      uni.showToast({ title: '功能开发中', icon: 'none' })
    },
    formatTime(seconds) {
      const min = Math.floor(seconds / 60)
      const sec = Math.floor(seconds % 60)
      return `${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`
    },
    async saveProgress() {
      if (!this.videoId || this.currentTime <= 0) return
      try {
        await updateProgress({
          videoId: this.videoId,
          position: Math.floor(this.currentTime),
          watchedDuration: Math.floor(this.currentTime)
        })
      } catch (e) {
        console.error('保存进度失败', e)
      }
    },
    exportSubtitles() {
      const text = this.subtitles.map(s => `${this.formatTime(s.startTime)} ${s.text}`).join('\n')
      uni.setClipboardData({
        data: text,
        success: () => uni.showToast({ title: '已复制到剪贴板', icon: 'success' })
      })
    },
    shareVideo() {
      uni.showToast({ title: '功能开发中', icon: 'none' })
    },
    goBack() {
      uni.navigateBack()
    },
    onVideoEnd() {
      this.saveProgress()
    },
    onVideoError(e) {
      console.error('视频播放错误', e)
      uni.showToast({ title: '视频加载失败', icon: 'none' })
    }
  }
}
</script>

<style>
.video-page {
  min-height: 100vh;
  background-color: #000000;
}

.nav-bar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 88rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24rpx;
  background-color: rgba(0, 0, 0, 0.8);
  z-index: 100;
}

.nav-back,
.nav-share {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-size: 36rpx;
}

.nav-title {
  flex: 1;
  text-align: center;
  color: #ffffff;
  font-size: 32rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.player-wrap {
  width: 100%;
}

.video-player {
  width: 100%;
  height: 420rpx;
}

.subtitle-area {
  background-color: rgba(0, 0, 0, 0.7);
  padding: 20rpx 32rpx;
  text-align: center;
}

.subtitle-text {
  color: #ffffff;
  font-size: 32rpx;
  line-height: 1.5;
}

.subtitle-translation {
  display: block;
  color: #cccccc;
  font-size: 28rpx;
  margin-top: 8rpx;
}

.control-panel {
  background-color: #1a1a1a;
  padding: 20rpx 0;
}

.control-row {
  display: flex;
  justify-content: space-around;
}

.control-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.control-icon {
  font-size: 40rpx;
}

.control-label {
  font-size: 22rpx;
  color: #999999;
  margin-top: 8rpx;
}

.subtitle-list-section {
  background-color: #ffffff;
  flex: 1;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.section-action {
  font-size: 26rpx;
  color: #4CAF50;
}

.subtitle-scroll {
  height: 500rpx;
}

.subtitle-item {
  display: flex;
  align-items: flex-start;
  padding: 20rpx 24rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.subtitle-item.active {
  background-color: #e8f5e9;
}

.subtitle-time {
  font-size: 24rpx;
  color: #999999;
  width: 80rpx;
  flex-shrink: 0;
}

.subtitle-content {
  flex: 1;
  margin: 0 16rpx;
}

.subtitle-original {
  font-size: 28rpx;
  color: #333333;
  display: block;
}

.subtitle-trans {
  font-size: 24rpx;
  color: #666666;
  display: block;
  margin-top: 4rpx;
}

.subtitle-actions {
  display: flex;
  gap: 16rpx;
}

.action-btn {
  font-size: 22rpx;
  color: #4CAF50;
  padding: 8rpx 16rpx;
  border: 1rpx solid #4CAF50;
  border-radius: 8rpx;
}

.speed-picker,
.ai-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 200;
}

.speed-panel,
.ai-panel {
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 32rpx;
  width: 80%;
}

.speed-title,
.ai-title {
  font-size: 32rpx;
  font-weight: 600;
  text-align: center;
  margin-bottom: 24rpx;
}

.speed-options {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.speed-option {
  flex: 1;
  min-width: 30%;
  text-align: center;
  padding: 20rpx;
  background-color: #f5f5f5;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.speed-option.active {
  background-color: #4CAF50;
  color: #ffffff;
}

.ai-content {
  max-height: 400rpx;
  font-size: 28rpx;
  color: #333333;
  line-height: 1.6;
  white-space: pre-wrap;
}

.ai-actions {
  display: flex;
  gap: 16rpx;
  margin-top: 24rpx;
}

.ai-btn {
  flex: 1;
  background-color: #4CAF50;
  color: #ffffff;
  font-size: 28rpx;
  border-radius: 8rpx;
}

.ai-btn.secondary {
  background-color: #f5f5f5;
  color: #666666;
}
</style>
