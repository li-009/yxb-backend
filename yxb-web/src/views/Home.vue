<template>
  <div class="home">
    <header class="header">
      <div class="container header-content">
        <div class="logo">🦜 鹦学伴</div>
        <div class="search-box">
          <el-input v-model="searchKeyword" placeholder="搜索视频、单词、语法点" prefix-icon="Search" @keyup.enter="handleSearch" />
        </div>
        <div class="header-actions">
          <el-badge :value="3" :hidden="false" class="action-icon">
            <el-icon><Bell /></el-icon>
          </el-badge>
          <el-avatar :size="32" @click="goLogin">
            <el-icon><User /></el-icon>
          </el-avatar>
        </div>
      </div>
    </header>

    <main class="main container">
      <section class="stats-overview">
        <div class="stat-card primary">
          <div class="stat-main">
            <span class="stat-value">{{ stats.todayMinutes }}</span>
            <span class="stat-unit">分钟</span>
          </div>
          <div class="stat-label">今日学习</div>
          <div class="stat-sub">连续打卡 {{ stats.checkInDays }} 天</div>
          <el-button type="primary" size="small" class="checkin-btn">今日打卡</el-button>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ stats.reviewWords }}</div>
          <div class="stat-label">待复习单词</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ stats.pendingTasks }}</div>
          <div class="stat-label">未完成任务</div>
        </div>
      </section>

      <section class="quick-actions">
        <div class="action-item" @click="handleImportVideo">
          <div class="action-icon-wrap"><el-icon><Upload /></el-icon></div>
          <span>视频导入</span>
        </div>
        <div class="action-item" @click="goToRoute('/study')">
          <div class="action-icon-wrap"><el-icon><Microphone /></el-icon></div>
          <span>AI跟读</span>
        </div>
        <div class="action-item" @click="goToRoute('/words')">
          <div class="action-icon-wrap"><el-icon><Reading /></el-icon></div>
          <span>单词复习</span>
        </div>
        <div class="action-item" @click="goToRoute('/community')">
          <div class="action-icon-wrap"><el-icon><ChatDotRound /></el-icon></div>
          <span>学习社群</span>
        </div>
      </section>

      <section class="recommend-section">
        <div class="section-header">
          <h2>为你推荐</h2>
          <div class="header-actions">
            <el-select v-model="language" placeholder="语种" size="small" @change="loadVideos">
              <el-option label="全部" value="" />
              <el-option label="英语" value="en" />
              <el-option label="日语" value="ja" />
              <el-option label="韩语" value="ko" />
            </el-select>
            <el-button text @click="loadVideos">换一批</el-button>
          </div>
        </div>

        <div class="video-grid" v-loading="loading">
          <div class="video-card" v-for="video in videos" :key="video.id" @click="playVideo(video)">
            <div class="video-cover">
              <img :src="video.coverUrl || defaultCover" alt="" />
              <span class="video-duration">{{ formatDuration(video.duration) }}</span>
              <span class="video-level-tag">{{ getLevelName(video.level) }}</span>
            </div>
            <div class="video-info">
              <h3 class="video-title">{{ video.title }}</h3>
              <div class="video-meta">
                <span class="video-language">{{ getLanguageName(video.language) }}</span>
                <span class="video-plays"><el-icon><VideoPlay /></el-icon> {{ video.playCount || 0 }}</span>
              </div>
              <div class="video-actions">
                <el-button size="small" type="primary">立即学习</el-button>
                <el-button size="small" :icon="Star" circle @click.stop="toggleFavorite(video)" />
              </div>
            </div>
          </div>
        </div>

        <div class="empty-state" v-if="!loading && videos.length === 0">
          <el-empty description="暂无视频，请先上传视频" />
        </div>

        <div class="pagination" v-if="total > pageSize">
          <el-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize" :current-page="pageNum" @current-change="handlePageChange" />
        </div>
      </section>

      <section class="daily-section">
        <div class="section-header">
          <h2>每日一句</h2>
        </div>
        <div class="daily-card">
          <div class="daily-text">"The only way to do great work is to love what you do."</div>
          <div class="daily-trans">做出伟大工作的唯一方法就是热爱你所做的事。</div>
          <div class="daily-source">—— Steve Jobs</div>
        </div>
      </section>
    </main>

    <nav class="bottom-nav">
      <div class="nav-item active">
        <el-icon><HomeFilled /></el-icon>
        <span>首页</span>
      </div>
      <div class="nav-item" @click="goToRoute('/library')">
        <el-icon><FolderOpened /></el-icon>
        <span>资源库</span>
      </div>
      <div class="nav-item add-btn" @click="handleImportVideo">
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
import { getReviewWords, getNoteCount, getCollectCount } from '../api/study'
import { Star, Bell, User, Upload, Microphone, Reading, ChatDotRound, HomeFilled, FolderOpened, Plus, DataLine, VideoPlay } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const defaultCover = 'https://via.placeholder.com/320x180?text=Video'

const searchKeyword = ref('')
const videos = ref([])
const loading = ref(false)
const language = ref('')
const pageNum = ref(1)
const pageSize = ref(8)
const total = ref(0)

const stats = ref({
  todayMinutes: 0,
  checkInDays: 0,
  reviewWords: 0,
  pendingTasks: 0
})

const loadStats = async () => {
  try {
    const [reviewRes, noteRes, collectRes] = await Promise.all([
      getReviewWords(100).catch(() => ({ data: { data: [] } })),
      getNoteCount().catch(() => ({ data: { data: 0 } })),
      getCollectCount().catch(() => ({ data: { data: 0 } }))
    ])
    stats.value.reviewWords = reviewRes.data.data?.length || 0
    stats.value.pendingTasks = noteRes.data.data || 0
    stats.value.todayMinutes = Math.floor(Math.random() * 60) + 10
    stats.value.checkInDays = Math.floor(Math.random() * 14) + 1
  } catch (e) {
    console.error('加载统计失败', e)
  }
}

const languageMap = { en: '英语', ja: '日语', ko: '韩语', zh: '中文' }
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
    const res = await getVideoList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      language: language.value || undefined
    })
    if (res.data.code === 200) {
      videos.value = res.data.data.records || []
      total.value = res.data.data.total || 0
    }
  } catch (e) {
    console.error('加载视频失败', e)
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page) => {
  pageNum.value = page
  loadVideos()
}

const handleSearch = () => {
  if (searchKeyword.value) {
    router.push(`/search?q=${encodeURIComponent(searchKeyword.value)}`)
  }
}

const handleImportVideo = () => {
  router.push('/import')
}

const toggleFavorite = (video) => {
  ElMessage.success('已收藏')
}

const playVideo = (video) => {
  router.push(`/video/${video.id}`)
}

const goLogin = () => {
  router.push('/login')
}

const goToRoute = (path) => {
  router.push(path)
}

onMounted(() => {
  loadVideos()
  loadStats()
})
</script>

<style scoped>
.home {
  min-height: 100vh;
  padding-bottom: 80px;
  background: #0d1117;
}

.header {
  background: linear-gradient(135deg, #1f6feb, #238636);
  padding: 16px 0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.logo {
  font-size: 24px;
  font-weight: 600;
  color: #fff;
  flex-shrink: 0;
}

.search-box {
  flex: 1;
  max-width: 400px;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
  color: #fff;
}

.action-icon {
  cursor: pointer;
}

.main {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.stats-overview {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: #161b22;
  border-radius: 12px;
  padding: 20px;
  border: 1px solid #30363d;
}

.stat-card.primary {
  background: linear-gradient(135deg, #238636, #1f6feb);
  border: none;
  color: #fff;
}

.stat-main {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.stat-value {
  font-size: 32px;
  font-weight: 600;
}

.stat-unit {
  font-size: 14px;
  opacity: 0.8;
}

.stat-label {
  font-size: 14px;
  color: #8b949e;
  margin-top: 4px;
}

.stat-card.primary .stat-label {
  color: rgba(255,255,255,0.8);
}

.stat-sub {
  font-size: 12px;
  opacity: 0.7;
  margin-top: 8px;
}

.checkin-btn {
  margin-top: 12px;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin-bottom: 24px;
}

.action-item {
  background: #161b22;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid #30363d;
}

.action-item:hover {
  background: #21262d;
  transform: translateY(-2px);
}

.action-icon-wrap {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #1f6feb33, #23863633);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12px;
  font-size: 24px;
  color: #58a6ff;
}

.action-item span {
  font-size: 14px;
  color: #c9d1d9;
}

.recommend-section, .daily-section {
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-header h2 {
  font-size: 20px;
  margin: 0;
}

.section-header .header-actions {
  display: flex;
  gap: 12px;
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
}

.video-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
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

.video-level-tag {
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
}

.video-title {
  font-size: 15px;
  font-weight: 500;
  margin: 0 0 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
}

.video-meta {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #8b949e;
  margin-bottom: 12px;
}

.video-language {
  color: #58a6ff;
}

.video-plays {
  display: flex;
  align-items: center;
  gap: 4px;
}

.video-actions {
  display: flex;
  gap: 8px;
}

.daily-card {
  background: linear-gradient(135deg, #21262d, #161b22);
  border: 1px solid #30363d;
  border-radius: 12px;
  padding: 24px;
  text-align: center;
}

.daily-text {
  font-size: 20px;
  font-style: italic;
  color: #c9d1d9;
  margin-bottom: 12px;
}

.daily-trans {
  font-size: 14px;
  color: #8b949e;
  margin-bottom: 8px;
}

.daily-source {
  font-size: 12px;
  color: #58a6ff;
}

.empty-state {
  padding: 40px;
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
  transition: color 0.2s;
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
