<template>
  <view class="category-page">
    <!-- 视频列表 -->
    <view class="video-grid">
      <view class="video-card" v-for="video in videoList" :key="video.id" @click="goToVideo(video)">
        <view class="video-cover-wrap">
          <image class="video-cover" :src="video.coverUrl" mode="aspectFill"></image>
          <view class="video-badge" v-if="video.isFree">免费</view>
          <view class="video-duration">{{ formatDuration(video.duration) }}</view>
        </view>
        <view class="video-info">
          <text class="video-title">{{ video.title }}</text>
          <view class="video-tags">
            <text class="video-tag" v-if="video.language">{{ video.language }}</text>
            <text class="video-tag">{{ video.languageName }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 加载更多 -->
    <view class="load-more" v-if="loading">
      <text>加载中...</text>
    </view>
    <view class="no-more" v-else-if="!hasMore && videoList.length > 0">
      <text>已加载全部</text>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="!loading && videoList.length === 0">
      <text class="empty-icon">🎬</text>
      <text class="empty-text">暂无视频</text>
    </view>
  </view>
</template>

<script>
import { getVideoList } from '../../api/video'

export default {
  data() {
    return {
      categoryId: '',
      categoryName: '',
      videoList: [],
      pageNum: 1,
      pageSize: 20,
      hasMore: true,
      loading: false
    }
  },
  onLoad(options) {
    this.categoryId = options.id
    this.categoryName = decodeURIComponent(options.name || '分类')
    uni.setNavigationBarTitle({ title: this.categoryName })
    this.loadVideos()
  },
  onReachBottom() {
    this.loadMore()
  },
  onPullDownRefresh() {
    this.pageNum = 1
    this.videoList = []
    this.hasMore = true
    this.loadVideos().finally(() => {
      uni.stopPullDownRefresh()
    })
  },
  methods: {
    async loadVideos() {
      if (this.loading) return
      this.loading = true
      try {
        const res = await getVideoList({
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          categoryId: this.categoryId
        })
        if (res.code === 200) {
          const records = res.data?.records || []
          if (this.pageNum === 1) {
            this.videoList = records
          } else {
            this.videoList = [...this.videoList, ...records]
          }
          this.hasMore = records.length >= this.pageSize
        }
      } catch (e) {
        console.error('加载视频失败', e)
        // Mock 数据
        if (this.pageNum === 1) {
          this.videoList = [
            { id: 1, title: '示例视频1', coverUrl: 'https://picsum.photos/300/200?random=1', isFree: true, language: '英语', languageName: '中文', duration: 360 },
            { id: 2, title: '示例视频2', coverUrl: 'https://picsum.photos/300/200?random=2', isFree: true, language: '英语', languageName: '中文', duration: 480 }
          ]
        }
        this.hasMore = false
      } finally {
        this.loading = false
      }
    },
    loadMore() {
      if (this.hasMore && !this.loading) {
        this.pageNum++
        this.loadVideos()
      }
    },
    goToVideo(video) {
      uni.navigateTo({
        url: `/pages/video/video?id=${video.id}`
      })
    },
    formatDuration(seconds) {
      if (!seconds) return ''
      const min = Math.floor(seconds / 60)
      const sec = seconds % 60
      return `${min}:${sec.toString().padStart(2, '0')}`
    }
  }
}
</script>

<style>
.category-page {
  min-height: 100vh;
  background-color: #121212;
  padding: 20rpx;
  padding-bottom: 40rpx;
}

.video-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.video-card {
  width: calc(50% - 10rpx);
}

.video-cover-wrap {
  position: relative;
  width: 100%;
  height: 200rpx;
  border-radius: 12rpx;
  overflow: hidden;
}

.video-cover {
  width: 100%;
  height: 100%;
}

.video-badge {
  position: absolute;
  top: 12rpx;
  left: 12rpx;
  background-color: #ff6b35;
  color: #ffffff;
  font-size: 20rpx;
  padding: 4rpx 12rpx;
  border-radius: 6rpx;
}

.video-duration {
  position: absolute;
  bottom: 12rpx;
  right: 12rpx;
  background-color: rgba(0, 0, 0, 0.7);
  color: #ffffff;
  font-size: 20rpx;
  padding: 4rpx 12rpx;
  border-radius: 6rpx;
}

.video-info {
  padding: 12rpx 4rpx;
}

.video-title {
  font-size: 26rpx;
  color: #ffffff;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
}

.video-tags {
  display: flex;
  gap: 12rpx;
  margin-top: 10rpx;
}

.video-tag {
  font-size: 20rpx;
  color: #999999;
  background-color: #2a2a2a;
  padding: 4rpx 12rpx;
  border-radius: 6rpx;
}

.load-more,
.no-more {
  text-align: center;
  padding: 32rpx;
  color: #666666;
  font-size: 24rpx;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 120rpx 48rpx;
}

.empty-icon {
  font-size: 100rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #666666;
  margin-top: 24rpx;
}
</style>
