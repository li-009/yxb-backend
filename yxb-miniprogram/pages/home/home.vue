<template>
  <view class="home">
    <!-- 轮播图 Banner -->
    <swiper class="banner-swiper" indicator-dots autoplay circular :interval="4000">
      <swiper-item v-for="(banner, index) in banners" :key="index" @click="onBannerClick(banner)">
        <image class="banner-image" :src="banner.imageUrl" mode="aspectFill"></image>
        <view class="banner-overlay" v-if="banner.title">
          <view class="banner-tag" v-if="banner.tag">{{ banner.tag }}</view>
          <text class="banner-title">{{ banner.title }}</text>
          <text class="banner-subtitle">{{ banner.subtitle }}</text>
        </view>
      </swiper-item>
    </swiper>

    <!-- YouTube 入口 -->
    <view class="youtube-entry" @click="openYoutube">
      <image class="youtube-logo" src="/static/youtube-logo.png" mode="aspectFit"></image>
      <view class="youtube-text">
        <text class="youtube-title">在App内播放Youtube视频</text>
        <text class="youtube-desc">需要你的网络你能正常访问Youtube</text>
      </view>
      <text class="youtube-arrow">></text>
    </view>

    <!-- 视频分类区块 -->
    <view class="section" v-for="category in categories" :key="category.id">
      <view class="section-header">
        <view class="section-title-wrap">
          <view class="section-indicator"></view>
          <text class="section-title">{{ category.name }}</text>
        </view>
        <text class="section-more" @click="goToCategory(category)">更多 ></text>
      </view>
      <scroll-view class="video-scroll" scroll-x>
        <view class="video-list">
          <view class="video-card" v-for="video in category.videos" :key="video.id" @click="goToVideo(video)">
            <view class="video-cover-wrap">
              <image class="video-cover" :src="video.coverUrl" mode="aspectFill"></image>
              <view class="video-badge" v-if="video.isFree">免费</view>
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
      </scroll-view>
    </view>

    <!-- 每日快讯 -->
    <view class="section">
      <view class="section-header">
        <view class="section-title-wrap">
          <view class="section-indicator"></view>
          <text class="section-title">每日快讯</text>
        </view>
        <text class="section-more" @click="goToNews">更多 ></text>
      </view>
      <scroll-view class="video-scroll" scroll-x>
        <view class="video-list">
          <view class="video-card" v-for="news in dailyNews" :key="news.id" @click="goToVideo(news)">
            <view class="video-cover-wrap">
              <image class="video-cover" :src="news.coverUrl" mode="aspectFill"></image>
              <view class="video-badge" v-if="news.isFree">免费</view>
            </view>
            <view class="video-info">
              <text class="video-title">{{ news.title }}</text>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 底部占位 -->
    <view class="bottom-placeholder"></view>
  </view>
</template>

<script>
import { getVideoList } from '../../api/video'

export default {
  data() {
    return {
      banners: [
        {
          id: 1,
          imageUrl: 'https://img1.doubanio.com/view/photo/l/public/p2329889272.jpg',
          tag: 'join us',
          title: '加入小鹦伴读打卡群',
          subtitle: '一起看电影学英语',
          link: ''
        },
        {
          id: 2,
          imageUrl: 'https://img2.doubanio.com/view/photo/l/public/p2315672647.jpg',
          tag: 'study',
          title: '一个人学习很孤单',
          subtitle: '一群人学习很温暖',
          link: ''
        }
      ],
      categories: [
        {
          id: 1,
          name: 'The Daily Show',
          videos: [
            { id: 1, title: '米歇尔·菲佛和丹尼斯·利里谈假期期间的"空巢老人"生活', coverUrl: 'https://picsum.photos/300/200?random=1', isFree: true, language: '英语', languageName: '中文' },
            { id: 2, title: '你的礼物会为你腾出空间', coverUrl: 'https://picsum.photos/300/200?random=2', isFree: true, language: '英语', languageName: '中文' }
          ]
        }
      ],
      dailyNews: [
        { id: 10, title: '每日新闻播报', coverUrl: 'https://picsum.photos/300/200?random=10', isFree: true },
        { id: 11, title: '国际要闻速递', coverUrl: 'https://picsum.photos/300/200?random=11', isFree: true }
      ]
    }
  },
  onLoad() {
    this.loadData()
  },
  onPullDownRefresh() {
    this.loadData().finally(() => {
      uni.stopPullDownRefresh()
    })
  },
  methods: {
    async loadData() {
      try {
        const res = await getVideoList({ pageNum: 1, pageSize: 10 })
        if (res.code === 200 && res.data?.records) {
          // 可以用真实数据替换 mock 数据
        }
      } catch (e) {
        console.log('首页无需登录，加载失败不影响使用')
      }
    },
    onBannerClick(banner) {
      if (banner.link) {
        uni.navigateTo({ url: banner.link })
      } else {
        uni.showToast({ title: '加入学习群功能开发中', icon: 'none' })
      }
    },
    openYoutube() {
      uni.showModal({
        title: 'YouTube视频',
        content: '请输入YouTube视频链接',
        editable: true,
        placeholderText: 'https://youtube.com/...',
        success: (res) => {
          if (res.confirm && res.content) {
            uni.navigateTo({
              url: `/pages/video/video?url=${encodeURIComponent(res.content)}`
            })
          }
        }
      })
    },
    goToCategory(category) {
      uni.navigateTo({
        url: `/pages/category/category?id=${category.id}&name=${encodeURIComponent(category.name)}`
      })
    },
    goToVideo(video) {
      uni.navigateTo({
        url: `/pages/video/video?id=${video.id}`
      })
    },
    goToNews() {
      uni.showToast({ title: '更多快讯', icon: 'none' })
    }
  }
}
</script>

<style>
.home {
  min-height: 100vh;
  background-color: #121212;
}

/* Banner 轮播 */
.banner-swiper {
  width: 100%;
  height: 360rpx;
}

.banner-image {
  width: 100%;
  height: 100%;
}

.banner-overlay {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 32rpx;
  background: linear-gradient(transparent, rgba(0,0,0,0.8));
}

.banner-tag {
  display: inline-block;
  background-color: #ff6b35;
  color: #ffffff;
  font-size: 20rpx;
  padding: 6rpx 16rpx;
  border-radius: 6rpx;
  margin-bottom: 12rpx;
}

.banner-title {
  display: block;
  font-size: 36rpx;
  font-weight: 600;
  color: #ffffff;
}

.banner-subtitle {
  display: block;
  font-size: 26rpx;
  color: rgba(255,255,255,0.8);
  margin-top: 8rpx;
}

/* YouTube 入口 */
.youtube-entry {
  display: flex;
  align-items: center;
  background-color: #1a1a1a;
  margin: 20rpx 24rpx;
  padding: 24rpx;
  border-radius: 16rpx;
}

.youtube-logo {
  width: 100rpx;
  height: 44rpx;
  margin-right: 20rpx;
}

.youtube-text {
  flex: 1;
}

.youtube-title {
  display: block;
  font-size: 28rpx;
  color: #ffffff;
  font-weight: 500;
}

.youtube-desc {
  display: block;
  font-size: 22rpx;
  color: #666666;
  margin-top: 6rpx;
}

.youtube-arrow {
  color: #666666;
  font-size: 32rpx;
}

/* 区块 */
.section {
  margin-top: 32rpx;
  padding: 0 24rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title-wrap {
  display: flex;
  align-items: center;
}

.section-indicator {
  width: 6rpx;
  height: 32rpx;
  background-color: #ff6b35;
  border-radius: 3rpx;
  margin-right: 16rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #ffffff;
}

.section-more {
  font-size: 24rpx;
  color: #ff6b35;
}

/* 视频滚动区 */
.video-scroll {
  white-space: nowrap;
}

.video-list {
  display: inline-flex;
  gap: 20rpx;
  padding-bottom: 10rpx;
}

.video-card {
  width: 300rpx;
  flex-shrink: 0;
  display: inline-block;
}

.video-cover-wrap {
  position: relative;
  width: 300rpx;
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

.video-info {
  padding: 16rpx 8rpx;
}

.video-title {
  font-size: 26rpx;
  color: #ffffff;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
  white-space: normal;
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

/* 底部占位 */
.bottom-placeholder {
  height: 120rpx;
}
</style>
