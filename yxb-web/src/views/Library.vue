<template>
  <div class="library-page">
    <header class="header">
      <div class="container header-content">
        <div class="back" @click="goBack">←</div>
        <div class="search-box">
          <el-input v-model="keyword" placeholder="搜索视频资源" prefix-icon="Search" @keyup.enter="handleSearch" />
        </div>
        <el-button :icon="Filter" circle @click="showFilter = true" />
      </div>
    </header>

    <div class="category-nav">
      <div 
        v-for="cat in categories" 
        :key="cat.value" 
        class="category-item"
        :class="{ active: currentCategory === cat.value }"
        @click="selectCategory(cat.value)"
      >
        {{ cat.label }}
      </div>
    </div>

    <main class="main container">
      <div class="video-grid" v-loading="loading">
        <div class="video-card" v-for="video in videos" :key="video.id" @click="playVideo(video)">
          <div class="video-cover">
            <img :src="video.coverUrl || defaultCover" alt="" />
            <span class="video-duration">{{ formatDuration(video.duration) }}</span>
            <span class="video-level">{{ getLevelName(video.level) }}</span>
          </div>
          <div class="video-info">
            <h3 class="video-title">{{ video.title }}</h3>
            <div class="video-meta">
              <span class="language">{{ getLanguageName(video.language) }}</span>
              <span class="plays">{{ video.playCount || 0 }} 播放</span>
            </div>
            <el-button size="small" :icon="Star" circle class="fav-btn" @click.stop="toggleFavorite(video)" />
          </div>
        </div>
      </div>

      <el-empty v-if="!loading && videos.length === 0" description="暂无资源" />

      <div class="load-more" v-if="hasMore">
        <el-button @click="loadMore" :loading="loading">加载更多</el-button>
      </div>
    </main>

    <el-drawer v-model="showFilter" title="筛选条件" direction="rtl" size="300px">
      <div class="filter-section">
        <h4>语种</h4>
        <el-radio-group v-model="filters.language">
          <el-radio label="">全部</el-radio>
          <el-radio label="en">英语</el-radio>
          <el-radio label="ja">日语</el-radio>
          <el-radio label="ko">韩语</el-radio>
        </el-radio-group>
      </div>
      <div class="filter-section">
        <h4>难度</h4>
        <el-radio-group v-model="filters.level">
          <el-radio :label="0">全部</el-radio>
          <el-radio :label="1">入门</el-radio>
          <el-radio :label="2">初级</el-radio>
          <el-radio :label="3">中级</el-radio>
          <el-radio :label="4">高级</el-radio>
        </el-radio-group>
      </div>
      <div class="filter-section">
        <h4>时长</h4>
        <el-radio-group v-model="filters.duration">
          <el-radio label="">全部</el-radio>
          <el-radio label="short">1-5分钟</el-radio>
          <el-radio label="medium">5-10分钟</el-radio>
          <el-radio label="long">10分钟以上</el-radio>
        </el-radio-group>
      </div>
      <template #footer>
        <el-button @click="resetFilters">重置</el-button>
        <el-button type="primary" @click="applyFilters">确认筛选</el-button>
      </template>
    </el-drawer>

    <nav class="bottom-nav">
      <div class="nav-item" @click="goToRoute('/')">
        <el-icon><HomeFilled /></el-icon>
        <span>首页</span>
      </div>
      <div class="nav-item active">
        <el-icon><FolderOpened /></el-icon>
        <span>资源库</span>
      </div>
      <div class="nav-item add-btn">
        <el-icon><Plus /></el-icon>
      </div>
      <div class="nav-item" @click="goToRoute('/study')">
        <el-icon><DataLine /></el-icon>
        <span>学习</span>
      </div>
      <div class="nav-item" @click="goToRoute('/profile')">
        <el-icon><User /></el-icon>
        <span>我的</span>
      </div>
    </nav>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getVideoList } from '../api/video'
import { Star, Filter, HomeFilled, FolderOpened, Plus, DataLine, User } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const defaultCover = 'https://via.placeholder.com/320x180?text=Video'

const keyword = ref('')
const loading = ref(false)
const showFilter = ref(false)
const videos = ref([])
const pageNum = ref(1)
const pageSize = ref(12)
const total = ref(0)
const hasMore = ref(false)
const currentCategory = ref('')

const categories = [
  { label: '全部', value: '' },
  { label: '影视片段', value: 'movie' },
  { label: '商务英语', value: 'business' },
  { label: '旅游外语', value: 'travel' },
  { label: '考级专题', value: 'exam' },
  { label: '儿童外语', value: 'kids' }
]

const filters = ref({
  language: '',
  level: 0,
  duration: ''
})

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

const loadVideos = async (append = false) => {
  loading.value = true
  try {
    const res = await getVideoList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      language: filters.value.language || undefined,
      category: currentCategory.value || undefined
    })
    if (res.data.code === 200) {
      const records = res.data.data.records || []
      videos.value = append ? [...videos.value, ...records] : records
      total.value = res.data.data.total || 0
      hasMore.value = videos.value.length < total.value
    }
  } catch (e) {
    console.error('加载视频失败', e)
  } finally {
    loading.value = false
  }
}

const loadMore = () => {
  pageNum.value++
  loadVideos(true)
}

const selectCategory = (cat) => {
  currentCategory.value = cat
  pageNum.value = 1
  loadVideos()
}

const handleSearch = () => {
  if (keyword.value) {
    ElMessage.info('搜索功能开发中')
  }
}

const resetFilters = () => {
  filters.value = { language: '', level: 0, duration: '' }
}

const applyFilters = () => {
  showFilter.value = false
  pageNum.value = 1
  loadVideos()
}

const toggleFavorite = (video) => {
  ElMessage.success('已收藏')
}

const playVideo = (video) => {
  router.push(`/video/${video.id}`)
}

const goBack = () => router.back()
const goToRoute = (path) => router.push(path)

onMounted(() => {
  loadVideos()
})
</script>

<style scoped>
.library-page {
  min-height: 100vh;
  background: #0d1117;
  padding-bottom: 80px;
}

.header {
  background: #161b22;
  padding: 12px 0;
  border-bottom: 1px solid #30363d;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.back {
  font-size: 20px;
  cursor: pointer;
  color: #58a6ff;
  padding: 8px;
}

.search-box {
  flex: 1;
}

.category-nav {
  display: flex;
  gap: 8px;
  padding: 12px 20px;
  overflow-x: auto;
  background: #161b22;
  border-bottom: 1px solid #30363d;
}

.category-item {
  padding: 8px 16px;
  background: #21262d;
  border-radius: 20px;
  font-size: 14px;
  color: #8b949e;
  cursor: pointer;
  white-space: nowrap;
  transition: all 0.2s;
}

.category-item:hover, .category-item.active {
  background: #1f6feb;
  color: #fff;
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
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid #30363d;
  position: relative;
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

.video-level {
  position: absolute;
  top: 8px;
  left: 8px;
  background: #238636;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.video-info {
  padding: 16px;
  position: relative;
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
}

.language {
  color: #58a6ff;
}

.fav-btn {
  position: absolute;
  top: 12px;
  right: 12px;
}

.load-more {
  text-align: center;
  margin-top: 24px;
}

.filter-section {
  margin-bottom: 24px;
}

.filter-section h4 {
  margin: 0 0 12px;
  font-size: 14px;
  color: #c9d1d9;
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
