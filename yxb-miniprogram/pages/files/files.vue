<template>
  <view class="files-page">
    <!-- 顶部操作栏 -->
    <view class="action-bar">
      <view class="action-btn" @click="importLocal">
        <text class="action-icon">📁</text>
        <text class="action-text">本地导入</text>
      </view>
      <view class="action-btn" @click="importWechat">
        <text class="action-icon">💬</text>
        <text class="action-text">微信导入</text>
      </view>
      <view class="action-btn" @click="importLink">
        <text class="action-icon">🔗</text>
        <text class="action-text">链接导入</text>
      </view>
    </view>

    <!-- 文件列表 -->
    <view class="file-list" v-if="fileList.length > 0">
      <view class="file-item" v-for="file in fileList" :key="file.id" @click="playFile(file)">
        <view class="file-cover">
          <image v-if="file.coverUrl" :src="file.coverUrl" mode="aspectFill"></image>
          <view v-else class="file-cover-placeholder">
            <text>🎬</text>
          </view>
        </view>
        <view class="file-info">
          <text class="file-name">{{ file.name }}</text>
          <text class="file-meta">{{ file.size }} · {{ file.date }}</text>
        </view>
        <view class="file-actions" @click.stop="showFileMenu(file)">
          <text>⋮</text>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-else>
      <text class="empty-icon">📂</text>
      <text class="empty-title">暂无本地视频</text>
      <text class="empty-desc">点击上方按钮导入视频文件</text>
      <text class="empty-tip">支持 mp4、mkv、avi、flv 等格式</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      fileList: []
    }
  },
  onLoad() {
    this.loadFiles()
  },
  methods: {
    loadFiles() {
      // 从本地存储加载文件列表
      const files = uni.getStorageSync('localFiles') || []
      this.fileList = files
    },
    importLocal() {
      uni.chooseVideo({
        sourceType: ['album'],
        success: (res) => {
          const file = {
            id: Date.now(),
            name: res.tempFilePath.split('/').pop(),
            path: res.tempFilePath,
            size: this.formatSize(res.size),
            duration: res.duration,
            date: this.formatDate(new Date()),
            coverUrl: ''
          }
          this.fileList.unshift(file)
          this.saveFiles()
          uni.showToast({ title: '导入成功', icon: 'success' })
        },
        fail: (err) => {
          if (err.errMsg !== 'chooseVideo:fail cancel') {
            uni.showToast({ title: '导入失败', icon: 'none' })
          }
        }
      })
    },
    importWechat() {
      uni.showToast({ title: '请从微信聊天中选择视频转发到本小程序', icon: 'none', duration: 2000 })
    },
    importLink() {
      uni.showModal({
        title: '导入视频链接',
        editable: true,
        placeholderText: '请输入视频链接（支持YouTube等）',
        success: (res) => {
          if (res.confirm && res.content) {
            uni.navigateTo({
              url: `/pages/video/video?url=${encodeURIComponent(res.content)}`
            })
          }
        }
      })
    },
    playFile(file) {
      uni.navigateTo({
        url: `/pages/video/video?path=${encodeURIComponent(file.path)}&name=${encodeURIComponent(file.name)}`
      })
    },
    showFileMenu(file) {
      uni.showActionSheet({
        itemList: ['播放', '重命名', '删除'],
        success: (res) => {
          switch (res.tapIndex) {
            case 0:
              this.playFile(file)
              break
            case 1:
              this.renameFile(file)
              break
            case 2:
              this.deleteFile(file)
              break
          }
        }
      })
    },
    renameFile(file) {
      uni.showModal({
        title: '重命名',
        editable: true,
        placeholderText: file.name,
        success: (res) => {
          if (res.confirm && res.content) {
            file.name = res.content
            this.saveFiles()
          }
        }
      })
    },
    deleteFile(file) {
      uni.showModal({
        title: '确认删除',
        content: `确定要删除 "${file.name}" 吗？`,
        success: (res) => {
          if (res.confirm) {
            const index = this.fileList.findIndex(f => f.id === file.id)
            if (index >= 0) {
              this.fileList.splice(index, 1)
              this.saveFiles()
              uni.showToast({ title: '已删除', icon: 'success' })
            }
          }
        }
      })
    },
    saveFiles() {
      uni.setStorageSync('localFiles', this.fileList)
    },
    formatSize(bytes) {
      if (bytes < 1024) return bytes + 'B'
      if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + 'KB'
      if (bytes < 1024 * 1024 * 1024) return (bytes / 1024 / 1024).toFixed(1) + 'MB'
      return (bytes / 1024 / 1024 / 1024).toFixed(1) + 'GB'
    },
    formatDate(date) {
      const m = date.getMonth() + 1
      const d = date.getDate()
      return `${m}月${d}日`
    }
  }
}
</script>

<style>
.files-page {
  min-height: 100vh;
  background-color: #121212;
  padding-bottom: 120rpx;
}

.action-bar {
  display: flex;
  justify-content: space-around;
  padding: 40rpx 24rpx;
  background-color: #1a1a1a;
}

.action-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx 40rpx;
  background-color: #2a2a2a;
  border-radius: 16rpx;
}

.action-icon {
  font-size: 48rpx;
}

.action-text {
  font-size: 24rpx;
  color: #ffffff;
  margin-top: 12rpx;
}

.file-list {
  padding: 20rpx 24rpx;
}

.file-item {
  display: flex;
  align-items: center;
  background-color: #1a1a1a;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 16rpx;
}

.file-cover {
  width: 120rpx;
  height: 80rpx;
  border-radius: 8rpx;
  overflow: hidden;
  flex-shrink: 0;
}

.file-cover image {
  width: 100%;
  height: 100%;
}

.file-cover-placeholder {
  width: 100%;
  height: 100%;
  background-color: #2a2a2a;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36rpx;
}

.file-info {
  flex: 1;
  margin-left: 20rpx;
}

.file-name {
  display: block;
  font-size: 28rpx;
  color: #ffffff;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.file-meta {
  display: block;
  font-size: 22rpx;
  color: #666666;
  margin-top: 8rpx;
}

.file-actions {
  padding: 16rpx;
  color: #666666;
  font-size: 32rpx;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 120rpx 48rpx;
}

.empty-icon {
  font-size: 120rpx;
}

.empty-title {
  font-size: 32rpx;
  color: #ffffff;
  margin-top: 32rpx;
}

.empty-desc {
  font-size: 26rpx;
  color: #666666;
  margin-top: 16rpx;
}

.empty-tip {
  font-size: 22rpx;
  color: #444444;
  margin-top: 12rpx;
}
</style>
