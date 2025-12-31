<template>
  <div class="report-page">
    <header class="header">
      <div class="container header-content">
        <div class="back" @click="goBack">←</div>
        <h1>学习报告</h1>
        <div class="period-tabs">
          <span :class="{ active: period === 'week' }" @click="changePeriod('week')">周报</span>
          <span :class="{ active: period === 'month' }" @click="changePeriod('month')">月报</span>
        </div>
      </div>
    </header>

    <main class="main container">
      <section class="summary-section">
        <h2>{{ periodTitle }}学习概览</h2>
        <div class="summary-cards">
          <div class="summary-card primary">
            <div class="card-icon">⏱️</div>
            <div class="card-content">
              <div class="card-value">{{ report.totalMinutes }}</div>
              <div class="card-label">学习时长(分钟)</div>
            </div>
          </div>
          <div class="summary-card">
            <div class="card-icon">📺</div>
            <div class="card-content">
              <div class="card-value">{{ report.videoCount }}</div>
              <div class="card-label">观看视频</div>
            </div>
          </div>
          <div class="summary-card">
            <div class="card-icon">📖</div>
            <div class="card-content">
              <div class="card-value">{{ report.newWords }}</div>
              <div class="card-label">新学单词</div>
            </div>
          </div>
          <div class="summary-card">
            <div class="card-icon">🎤</div>
            <div class="card-content">
              <div class="card-value">{{ report.followReadCount }}</div>
              <div class="card-label">跟读次数</div>
            </div>
          </div>
        </div>
      </section>

      <section class="chart-section">
        <h2>每日学习时长</h2>
        <div class="chart-container">
          <div class="bar-chart">
            <div class="bar-item" v-for="(day, idx) in report.dailyData" :key="idx">
              <div class="bar" :style="{ height: getBarHeight(day.minutes) + '%' }">
                <span class="bar-value">{{ day.minutes }}</span>
              </div>
              <div class="bar-label">{{ day.label }}</div>
            </div>
          </div>
        </div>
      </section>

      <section class="progress-section">
        <h2>学习进度</h2>
        <div class="progress-list">
          <div class="progress-item">
            <div class="progress-header">
              <span>单词掌握率</span>
              <span>{{ report.wordMasteryRate }}%</span>
            </div>
            <el-progress :percentage="report.wordMasteryRate" :show-text="false" />
          </div>
          <div class="progress-item">
            <div class="progress-header">
              <span>视频完成率</span>
              <span>{{ report.videoCompleteRate }}%</span>
            </div>
            <el-progress :percentage="report.videoCompleteRate" :show-text="false" status="success" />
          </div>
          <div class="progress-item">
            <div class="progress-header">
              <span>跟读达标率</span>
              <span>{{ report.followReadRate }}%</span>
            </div>
            <el-progress :percentage="report.followReadRate" :show-text="false" status="warning" />
          </div>
        </div>
      </section>

      <section class="ranking-section">
        <h2>学习排行</h2>
        <div class="ranking-list">
          <div class="ranking-item" v-for="(item, idx) in report.topVideos" :key="item.id">
            <span class="rank">{{ idx + 1 }}</span>
            <span class="title">{{ item.title }}</span>
            <span class="time">{{ item.minutes }}分钟</span>
          </div>
        </div>
      </section>

      <section class="achievement-section">
        <h2>本周成就</h2>
        <div class="achievement-list">
          <div class="achievement-item" v-for="ach in report.achievements" :key="ach.id" :class="{ unlocked: ach.unlocked }">
            <div class="achievement-icon">{{ ach.icon }}</div>
            <div class="achievement-info">
              <div class="achievement-name">{{ ach.name }}</div>
              <div class="achievement-desc">{{ ach.desc }}</div>
            </div>
          </div>
        </div>
      </section>
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
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { HomeFilled, FolderOpened, Plus, DataLine, User } from '@element-plus/icons-vue'

const router = useRouter()
const period = ref('week')

const periodTitle = computed(() => period.value === 'week' ? '本周' : '本月')

const report = ref({
  totalMinutes: 320,
  videoCount: 12,
  newWords: 45,
  followReadCount: 86,
  wordMasteryRate: 72,
  videoCompleteRate: 85,
  followReadRate: 68,
  dailyData: [
    { label: '周一', minutes: 45 },
    { label: '周二', minutes: 60 },
    { label: '周三', minutes: 30 },
    { label: '周四', minutes: 55 },
    { label: '周五', minutes: 40 },
    { label: '周六', minutes: 50 },
    { label: '周日', minutes: 40 }
  ],
  topVideos: [
    { id: 1, title: '商务英语会议对话', minutes: 45 },
    { id: 2, title: '日常口语100句', minutes: 38 },
    { id: 3, title: '美剧经典片段精讲', minutes: 32 }
  ],
  achievements: [
    { id: 1, icon: '🔥', name: '连续打卡7天', desc: '坚持就是胜利', unlocked: true },
    { id: 2, icon: '📚', name: '单词达人', desc: '累计学习100个单词', unlocked: true },
    { id: 3, icon: '🎯', name: '跟读之星', desc: '跟读评分达到90分', unlocked: false },
    { id: 4, icon: '⭐', name: '学霸', desc: '单周学习超过5小时', unlocked: true }
  ]
})

const getBarHeight = (minutes) => {
  const max = Math.max(...report.value.dailyData.map(d => d.minutes))
  return max > 0 ? (minutes / max) * 100 : 0
}

const changePeriod = (p) => {
  period.value = p
}

const goBack = () => router.back()
const goTo = (path) => router.push(path)
</script>

<style scoped>
.report-page {
  min-height: 100vh;
  background: #0d1117;
  padding-bottom: 80px;
}

.header {
  background: linear-gradient(135deg, #1f6feb, #238636);
  padding: 16px 0;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.back {
  font-size: 20px;
  cursor: pointer;
  color: #fff;
  padding: 8px;
}

h1 {
  font-size: 18px;
  margin: 0;
  color: #fff;
}

.period-tabs {
  display: flex;
  gap: 12px;
}

.period-tabs span {
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 13px;
  cursor: pointer;
  color: rgba(255,255,255,0.7);
}

.period-tabs span.active {
  background: rgba(255,255,255,0.2);
  color: #fff;
}

.main {
  padding: 20px;
}

section {
  margin-bottom: 24px;
}

h2 {
  font-size: 16px;
  margin: 0 0 16px;
  color: #c9d1d9;
}

.summary-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.summary-card {
  background: #161b22;
  border: 1px solid #30363d;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.summary-card.primary {
  grid-column: span 2;
  background: linear-gradient(135deg, #1f6feb22, #23863622);
  border-color: #1f6feb44;
}

.card-icon {
  font-size: 32px;
}

.card-value {
  font-size: 24px;
  font-weight: 600;
  color: #58a6ff;
}

.card-label {
  font-size: 12px;
  color: #8b949e;
}

.chart-container {
  background: #161b22;
  border: 1px solid #30363d;
  border-radius: 12px;
  padding: 20px;
}

.bar-chart {
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
  height: 150px;
}

.bar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
}

.bar {
  width: 24px;
  background: linear-gradient(180deg, #58a6ff, #1f6feb);
  border-radius: 4px 4px 0 0;
  min-height: 4px;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  position: relative;
}

.bar-value {
  position: absolute;
  top: -20px;
  font-size: 11px;
  color: #8b949e;
}

.bar-label {
  font-size: 12px;
  color: #8b949e;
  margin-top: 8px;
}

.progress-list {
  background: #161b22;
  border: 1px solid #30363d;
  border-radius: 12px;
  padding: 16px;
}

.progress-item {
  margin-bottom: 16px;
}

.progress-item:last-child {
  margin-bottom: 0;
}

.progress-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 14px;
  color: #c9d1d9;
}

.ranking-list {
  background: #161b22;
  border: 1px solid #30363d;
  border-radius: 12px;
  overflow: hidden;
}

.ranking-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #30363d;
}

.ranking-item:last-child {
  border-bottom: none;
}

.rank {
  width: 24px;
  height: 24px;
  background: #21262d;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  color: #58a6ff;
  margin-right: 12px;
}

.ranking-item:first-child .rank {
  background: linear-gradient(135deg, #f0883e, #d29922);
  color: #fff;
}

.title {
  flex: 1;
  font-size: 14px;
  color: #c9d1d9;
}

.time {
  font-size: 13px;
  color: #8b949e;
}

.achievement-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.achievement-item {
  background: #161b22;
  border: 1px solid #30363d;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  opacity: 0.5;
}

.achievement-item.unlocked {
  opacity: 1;
  border-color: #238636;
}

.achievement-icon {
  font-size: 32px;
}

.achievement-name {
  font-size: 14px;
  font-weight: 500;
  color: #c9d1d9;
}

.achievement-desc {
  font-size: 12px;
  color: #8b949e;
  margin-top: 2px;
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
