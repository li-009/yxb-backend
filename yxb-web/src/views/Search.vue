<template>
  <div class="search-page">
    <header class="header">
      <div class="container header-content">
        <div class="back" @click="goBack">←</div>
        <div class="search-box">
          <el-input v-model="keyword" placeholder="搜索视频、单词、语法点" prefix-icon="Search" @keyup.enter="doSearch" clearable />
        </div>
        <el-button type="primary" @click="doSearch">搜索</el-button>
      </div>
    </header>

    <main class="main container">
      <div class="filter-bar" v-if="hasSearched">
        <el-select v-model="language" placeholder="语种" size="small" clearable @change="doSearch">
          <el-option label="全部" value="" />
          <el-option label="英语" value="en" />
          <el-option label="日语" value="ja" />
          <el-option label="韩语" value="ko" />
        </el-select>
        <el-select v-model="level" placeholder="难度" size="small" clearable @change="doSearch">
          <el-option label="全部" value="" />
          <el-option label="入门" :value="1" />
          <el-option label="初级" :value="2" />
          <el-option label="中级" :value="3" />
          <el-option label="高级" :value="4" />
        </el-select>
      </div>

      <div class="search-history" v-if="!hasSearched && searchHistory.length > 0">
        <div class="history-header">
          <span>搜索历史</span>
          <el-button text size="small" @click="clearHistory">清空</el-button>
        </div>
        <div class="history-tags">
          <el-tag v-for="item in searchHistory" :key="item" @click="searchFromHistory(item)" closable @close="removeHistory(item)">
            {{ item }}
          </el-tag>
        </div>
      </div>

      <div class="hot-search" v-if="!hasSearched">
        <h3>热门搜索</h3>
        <div class="hot-tags">
          <el-tag v-for="item in hotKeywords" :key="item" type="info" @click="searchFromHistory(item)">
            {{ item }}
          </el-tag>
        </div>
      </div>

      <div class="search-results" v-if="hasSearched">
        <div class="result-header">
          <span>共找到 {{ total }} 个结果</span>
        </div>

        <div class="video-grid" v-loading="loading">
          <div class="video-card" v-for="video in videos" :key="video.id" @click="playVideo(video)">
            <div class="video-cover">
              <img :src="video.coverUrl || defaultCover" alt="" />
              <span class="video-duration">{{ formatDuration(video.duration) }}</span>
            </div>
            <div class="video-info">
              <h3 class="video-title" v-html="highlightKeyword(video.title)"></h3>
              <div class="video-meta">
                <span class="language">{{ getLanguageName(video.language) }}</span>
                <span class="level">{{ getLevelName(video.level) }}</span>
              </div>
            </div>
          </div>
        </div>

        <el-empty v-if="!loading && videos.length === 0" description="未找到相关内容" />

        <div class="pagination" v-if="total > pageSize">
          <el-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize" :current-page="pageNum" @current-change="handlePageChange" />
        </div>
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
      <div class="nav-item" @click="goTo('/profile')">
        <el-icon><User /></el-icon>
        <span>我的</span>
      </div>
    </nav>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { searchVideos } from '../api/video'
import { HomeFilled, FolderOpened, Plus, DataLine, User } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const defaultCover = 'https://via.placeholder.com/320x180?text=Video'

const keyword = ref('')
const language = ref('')
const level = ref('')
const videos = ref([])
const loading = ref(false)
const hasSearched = ref(false)
const pageNum = ref(1)
const pageSize = ref(12)
const total = ref(0)

const searchHistory = ref([])
const hotKeywords = ref(['商务英语', '日常口语', '美剧学英语', '日语N2', '雅思听力', '韩语入门'])

const languageMap = { en: '英语', ja: '日语', ko: '韩语' }
const levelMap = { 1: '入门', 2: '初级', 3: '中级', 4: '高级' }

const getLanguageName = (code) => languageMap[code] || code
const getLevelName = (lv) => levelMap[lv] || '初级'

const formatDuration = (seconds) => {
  if (!seconds) return '00:00'
  const m = Math.floor(seconds / 60)
  const s = seconds % 60
  return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`
}

const highlightKeyword = (text) => {
  if (!keyword.value || !text) return text
  const regex = new RegExp(`(${keyword.value})`, 'gi')
  return text.replace(regex, '<span class="highlight">$1</span>')
}

const loadHistory = () => {
  const saved = localStorage.getItem('searchHistory')
  if (saved) {
    searchHistory.value = JSON.parse(saved)
  }
}

const saveHistory = (kw) => {
  if (!kw) return
  const history = searchHistory.value.filter(h => h !== kw)
  history.unshift(kw)
  searchHistory.value = history.slice(0, 10)
  localStorage.setItem('searchHistory', JSON.stringify(searchHistory.value))
}

const clearHistory = () => {
  searchHistory.value = []
  localStorage.removeItem('searchHistory')
}

const removeHistory = (item) => {
  searchHistory.value = searchHistory.value.filter(h => h !== item)
  localStorage.setItem('searchHistory', JSON.stringify(searchHistory.value))
}

const searchFromHistory = (kw) => {
  keyword.value = kw
  doSearch()
}

const doSearch = async () => {
  if (!keyword.value.trim()) return
  
  hasSearched.value = true
  loading.value = true
  saveHistory(keyword.value)
  
  try {
    const res = await searchVideos(keyword.value, {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      language: language.value || undefined,
      level: level.value || undefined
    })
    if (res.data.code === 200) {
      videos.value = res.data.data.records || []
      total.value = res.data.data.total || 0
    }
  } catch (e) {
    console.error('搜索失败', e)
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page) => {
  pageNum.value = page
  doSearch()
}

const playVideo = (video) => {
  router.push(`/video/${video.id}`)
}

const goBack = () => router.back()
const goTo = (path) => router.push(path)

onMounted(() => {
  loadHistory()
  if (route.query.q) {
    keyword.value = route.query.q
    doSearch()
  }
})
</script>

<style scoped>
.search-page {
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

.main {
  padding: 20px;
}

.filter-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.search-history, .hot-search {
  margin-bottom: 24px;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  color: #c9d1d9;
}

h3 {
  font-size: 16px;
  margin: 0 0 12px;
  color: #c9d1d9;
}

.history-tags, .hot-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.history-tags .el-tag, .hot-tags .el-tag {
  cursor: pointer;
}

.result-header {
  margin-bottom: 16px;
  color: #8b949e;
  font-size: 14px;
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

.video-title :deep(.highlight) {
  color: #f0883e;
  font-weight: 600;
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
