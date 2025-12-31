<template>
  <div class="study-page">
    <header class="header">
      <div class="container header-content">
        <router-link to="/" class="logo">🦜 鹦学伴</router-link>
        <h1>学习中心</h1>
        <div></div>
      </div>
    </header>

    <main class="main container">
      <div class="stats-row">
        <div class="stat-card">
          <div class="stat-value">{{ stats.studyDays }}</div>
          <div class="stat-label">学习天数</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ stats.wordCount }}</div>
          <div class="stat-label">掌握单词</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ stats.videoCount }}</div>
          <div class="stat-label">学习视频</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ stats.totalMinutes }}</div>
          <div class="stat-label">学习时长(分)</div>
        </div>
      </div>

      <section class="section">
        <div class="section-header">
          <h2>继续学习</h2>
        </div>
        <div class="progress-list" v-if="progressList.length > 0">
          <div class="progress-card" v-for="item in progressList" :key="item.id" @click="continueWatch(item)">
            <img :src="item.coverUrl || defaultCover" class="cover" />
            <div class="info">
              <div class="title">{{ item.videoTitle }}</div>
              <el-progress :percentage="getPercent(item)" :show-text="false" />
              <div class="meta">已学习 {{ formatMinutes(item.watchedDuration) }}</div>
            </div>
          </div>
        </div>
        <el-empty description="暂无学习记录" v-else />
      </section>

      <section class="section">
        <div class="section-header">
          <h2>我的单词本</h2>
          <el-button size="small" @click="goToWordBook">查看全部</el-button>
        </div>
        <div class="word-list" v-if="words.length > 0">
          <div class="word-card" v-for="word in words" :key="word.id">
            <div class="word">{{ word.word }}</div>
            <div class="phonetic">{{ word.phonetic }}</div>
            <div class="meaning">{{ word.meaning }}</div>
          </div>
        </div>
        <el-empty description="暂无单词" v-else />
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const defaultCover = 'https://via.placeholder.com/160x90?text=Video'

const stats = ref({
  studyDays: 0,
  wordCount: 0,
  videoCount: 0,
  totalMinutes: 0
})

const progressList = ref([])
const words = ref([])

const getPercent = (item) => {
  if (!item.totalDuration) return 0
  return Math.min(100, Math.round((item.watchedDuration / item.totalDuration) * 100))
}

const formatMinutes = (seconds) => {
  if (!seconds) return '0分钟'
  const m = Math.floor(seconds / 60)
  return `${m}分钟`
}

const continueWatch = (item) => {
  router.push(`/video/${item.videoId}?t=${item.position}`)
}

const goToWordBook = () => {
  // 单词本页面
}

onMounted(() => {
  // 加载学习数据
  stats.value = { studyDays: 7, wordCount: 128, videoCount: 15, totalMinutes: 320 }
})
</script>

<style scoped>
.study-page {
  min-height: 100vh;
  background-color: #0d1117;
}

.header {
  background-color: #161b22;
  padding: 16px 0;
  border-bottom: 1px solid #30363d;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  font-size: 20px;
  color: #c9d1d9;
  text-decoration: none;
}

.main {
  padding: 32px 20px;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 40px;
}

.stat-card {
  background: linear-gradient(135deg, #238636, #1f6feb);
  border-radius: 12px;
  padding: 24px;
  text-align: center;
}

.stat-value {
  font-size: 36px;
  font-weight: 600;
  color: #fff;
}

.stat-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 8px;
}

.section {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h2 {
  font-size: 20px;
  margin: 0;
}

.progress-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
}

.progress-card {
  display: flex;
  background: #161b22;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s;
}

.progress-card:hover {
  transform: translateX(4px);
}

.progress-card .cover {
  width: 160px;
  height: 90px;
  object-fit: cover;
}

.progress-card .info {
  flex: 1;
  padding: 12px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.progress-card .title {
  font-size: 14px;
  font-weight: 500;
}

.progress-card .meta {
  font-size: 12px;
  color: #8b949e;
}

.word-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 12px;
}

.word-card {
  background: #161b22;
  border-radius: 8px;
  padding: 16px;
}

.word-card .word {
  font-size: 18px;
  font-weight: 600;
  color: #58a6ff;
}

.word-card .phonetic {
  font-size: 12px;
  color: #8b949e;
  margin-top: 4px;
}

.word-card .meaning {
  font-size: 14px;
  margin-top: 8px;
}
</style>
