<template>
  <div class="favorites-page">
    <header class="header">
      <div class="container header-content">
        <div class="back" @click="goBack">←</div>
        <h1>我的收藏</h1>
        <div></div>
      </div>
    </header>

    <main class="main container">
      <div class="video-grid" v-loading="loading">
        <div class="video-card" v-for="video in videos" :key="video.id" @click="playVideo(video)">
          <div class="video-cover">
            <img :src="video.coverUrl || defaultCover" alt="" />
            <span class="video-duration">{{ formatDuration(video.duration) }}</span>
          </div>
          <div class="video-info">
            <h3 class="video-title">{{ video.title }}</h3>
            <div class="video-meta">
              <span class="language">{{ getLanguageName(video.language) }}</span>
              <span class="level">{{ getLevelName(video.level) }}</span>
            </div>
            <el-button size="small" type="danger" text @click.stop="handleUncollect(video)">
              取消收藏
            </el-button>
          </div>
        </div>
      </div>

      <el-empty v-if="!loading && videos.length === 0" description="暂无收藏" />

      <div class="pagination" v-if="total > pageSize">
        <el-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize" :current-page="pageNum" @current-change="handlePageChange" />
      </div>
    </main>

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
      <div class="nav-item active" @click="goTo('/profile')">
        <el-icon><User /></el-icon>
        <span>我的</span>
      </div>
    </nav>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCollectedVideos, uncollectVideo } from '../api/study'
import { getVideoDetail } from '../api/video'
import { HomeFilled, FolderOpened, Plus, DataLine, User } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const defaultCover = 'https://via.placeholder.com/320x180?text=Video'

const videos = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(12)
const total = ref(0)

const languageMap = { en: '英语', ja: '日语', ko: '韩语' }
const levelMap = { 1: '入门', 2: '初级', 3: '中级', 4: '高级' }

const getLanguageName = (code) => languageMap[code] || code
const getLevelName = (level) => levelMap[level] || '初级'

const formatDuration = (seconds) => {
  if (!seconds) return '00:00'
  const m = Math.floor(seconds / 60)
  const s = seconds % 60
  return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`
}

const loadVideos = async () => {
  loading.value = true
  try {
    const res = await getCollectedVideos(pageNum.value, pageSize.value)
    if (res.data.code === 200) {
      const videoIds = res.data.data.records || []
      total.value = res.data.data.total || 0
      
      const videoDetails = await Promise.all(
        videoIds.map(async (id) => {
          try {
            const detail = await getVideoDetail(id)
            return detail.data.code === 200 ? detail.data.data : null
          } catch {
            return null
          }
        })
      )
      videos.value = videoDetails.filter(v => v !== null)
    }
  } catch (e) {
    console.error('加载收藏失败', e)
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page) => {
  pageNum.value = page
  loadVideos()
}

const handleUncollect = async (video) => {
  try {
    await uncollectVideo(video.id)
    ElMessage.success('已取消收藏')
    videos.value = videos.value.filter(v => v.id !== video.id)
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const playVideo = (video) => {
  router.push(`/video/${video.id}`)
}

const goBack = () => router.back()
const goTo = (path) => router.push(path)

onMounted(() => {
  loadVideos()
})
</script>

<style scoped>
.favorites-page {
  min-height: 100vh;
  background: #0d1117;
  padding-bottom: 80px;
}

.header {
  background: #161b22;
  padding: 16px 0;
  border-bottom: 1px solid #30363d;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.back {
  font-size: 20px;
  cursor: pointer;
  color: #58a6ff;
  padding: 8px;
}

h1 {
  font-size: 18px;
  margin: 0;
}

.main {
  padding: 20px;
}

.video-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 20px;
}

.video-card {
  background: #161b22;
  border: 1px solid #30363d;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.2s;
}

.video-card:hover {
  transform: translateY(-4px);
  border-color: #58a6ff;
}

.video-cover {
  position: relative;
  aspect-ratio: 16/9;
}

.video-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.video-duration {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.8);
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.video-info {
  padding: 16px;
}

.video-title {
  font-size: 15px;
  margin: 0 0 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.video-meta {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #8b949e;
  margin-bottom: 12px;
}

.language {
  color: #58a6ff;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 24px;
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
