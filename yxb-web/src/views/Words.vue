<template>
  <div class="words-page">
    <header class="header">
      <div class="container header-content">
        <div class="back" @click="goBack">←</div>
        <h1>单词本</h1>
        <el-button size="small" type="primary" @click="startReview">开始复习</el-button>
      </div>
    </header>

    <main class="main container">
      <div class="stats-row">
        <div class="stat-card">
          <div class="stat-value">{{ stats.total }}</div>
          <div class="stat-label">总单词</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ stats.mastered }}</div>
          <div class="stat-label">已掌握</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ stats.learning }}</div>
          <div class="stat-label">学习中</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ stats.needReview }}</div>
          <div class="stat-label">待复习</div>
        </div>
      </div>

      <div class="filter-tabs">
        <div class="tab" :class="{ active: filterStatus === null }" @click="filterStatus = null; loadWords()">全部</div>
        <div class="tab" :class="{ active: filterStatus === 0 }" @click="filterStatus = 0; loadWords()">未掌握</div>
        <div class="tab" :class="{ active: filterStatus === 1 }" @click="filterStatus = 1; loadWords()">学习中</div>
        <div class="tab" :class="{ active: filterStatus === 2 }" @click="filterStatus = 2; loadWords()">已掌握</div>
      </div>

      <div class="word-list" v-loading="loading">
        <div class="word-card" v-for="word in words" :key="word.id">
          <div class="word-main">
            <div class="word-text">{{ word.word }}</div>
            <div class="word-phonetic">{{ word.phonetic }}</div>
          </div>
          <div class="word-meaning">{{ word.meaning }}</div>
          <div class="word-example" v-if="word.example">
            <div class="example-text">{{ word.example }}</div>
            <div class="example-trans">{{ word.exampleTranslation }}</div>
          </div>
          <div class="word-footer">
            <span class="review-info">
              复习{{ word.reviewCount || 0 }}次
            </span>
            <div class="mastery-btns">
              <el-button size="small" :type="word.masteryStatus === 0 ? 'danger' : 'default'" @click="setMastery(word, 0)">未掌握</el-button>
              <el-button size="small" :type="word.masteryStatus === 1 ? 'warning' : 'default'" @click="setMastery(word, 1)">学习中</el-button>
              <el-button size="small" :type="word.masteryStatus === 2 ? 'success' : 'default'" @click="setMastery(word, 2)">已掌握</el-button>
            </div>
          </div>
        </div>
      </div>

      <el-empty v-if="!loading && words.length === 0" description="暂无单词" />

      <div class="pagination" v-if="total > pageSize">
        <el-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize" :current-page="pageNum" @current-change="handlePageChange" />
      </div>
    </main>

    <el-dialog v-model="showReview" title="单词复习" width="500px" :close-on-click-modal="false">
      <div class="review-card" v-if="currentReviewWord">
        <div class="review-word">{{ currentReviewWord.word }}</div>
        <div class="review-phonetic">{{ currentReviewWord.phonetic }}</div>
        <div class="review-meaning" v-if="showMeaning">{{ currentReviewWord.meaning }}</div>
        <el-button v-else @click="showMeaning = true">显示释义</el-button>
      </div>
      <template #footer>
        <el-button type="danger" @click="markReview(0)">不认识</el-button>
        <el-button type="warning" @click="markReview(1)">模糊</el-button>
        <el-button type="success" @click="markReview(2)">认识</el-button>
      </template>
    </el-dialog>

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
      <div class="nav-item active" @click="goTo('/study')">
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
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getWordList, getReviewWords, updateWordMastery } from '../api/study'
import { HomeFilled, FolderOpened, Plus, DataLine, User } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()

const words = ref([])
const loading = ref(false)
const filterStatus = ref(null)
const pageNum = ref(1)
const pageSize = ref(20)
const total = ref(0)

const showReview = ref(false)
const reviewWords = ref([])
const reviewIndex = ref(0)
const showMeaning = ref(false)

const stats = ref({
  total: 0,
  mastered: 0,
  learning: 0,
  needReview: 0
})

const currentReviewWord = computed(() => {
  return reviewWords.value[reviewIndex.value] || null
})

const loadWords = async () => {
  loading.value = true
  try {
    const res = await getWordList(filterStatus.value, pageNum.value, pageSize.value)
    if (res.data.code === 200) {
      words.value = res.data.data.records || []
      total.value = res.data.data.total || 0
    }
  } catch (e) {
    console.error('加载单词失败', e)
  } finally {
    loading.value = false
  }
}

const loadStats = async () => {
  try {
    const [all, mastered, learning] = await Promise.all([
      getWordList(null, 1, 1),
      getWordList(2, 1, 1),
      getWordList(1, 1, 1)
    ])
    stats.value.total = all.data.data?.total || 0
    stats.value.mastered = mastered.data.data?.total || 0
    stats.value.learning = learning.data.data?.total || 0
    
    const review = await getReviewWords(100)
    stats.value.needReview = review.data.data?.length || 0
  } catch (e) {
    console.error('加载统计失败', e)
  }
}

const handlePageChange = (page) => {
  pageNum.value = page
  loadWords()
}

const setMastery = async (word, status) => {
  try {
    await updateWordMastery(word.id, status)
    word.masteryStatus = status
    ElMessage.success('更新成功')
  } catch (e) {
    ElMessage.error('更新失败')
  }
}

const startReview = async () => {
  try {
    const res = await getReviewWords(20)
    if (res.data.code === 200 && res.data.data?.length > 0) {
      reviewWords.value = res.data.data
      reviewIndex.value = 0
      showMeaning.value = false
      showReview.value = true
    } else {
      ElMessage.info('暂无需要复习的单词')
    }
  } catch (e) {
    ElMessage.error('加载复习单词失败')
  }
}

const markReview = async (status) => {
  if (currentReviewWord.value) {
    await updateWordMastery(currentReviewWord.value.id, status)
  }
  
  if (reviewIndex.value < reviewWords.value.length - 1) {
    reviewIndex.value++
    showMeaning.value = false
  } else {
    showReview.value = false
    ElMessage.success('复习完成！')
    loadStats()
  }
}

const goBack = () => router.back()
const goTo = (path) => router.push(path)

onMounted(() => {
  loadWords()
  loadStats()
})
</script>

<style scoped>
.words-page {
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

.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin-bottom: 20px;
}

.stat-card {
  background: #161b22;
  border: 1px solid #30363d;
  border-radius: 8px;
  padding: 16px;
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #58a6ff;
}

.stat-label {
  font-size: 12px;
  color: #8b949e;
  margin-top: 4px;
}

.filter-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
}

.tab {
  padding: 8px 16px;
  background: #21262d;
  border-radius: 20px;
  font-size: 14px;
  color: #8b949e;
  cursor: pointer;
  transition: all 0.2s;
}

.tab:hover, .tab.active {
  background: #1f6feb;
  color: #fff;
}

.word-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.word-card {
  background: #161b22;
  border: 1px solid #30363d;
  border-radius: 12px;
  padding: 16px;
}

.word-main {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 8px;
}

.word-text {
  font-size: 20px;
  font-weight: 600;
  color: #58a6ff;
}

.word-phonetic {
  font-size: 14px;
  color: #8b949e;
}

.word-meaning {
  font-size: 15px;
  color: #c9d1d9;
  margin-bottom: 8px;
}

.word-example {
  background: #21262d;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 12px;
}

.example-text {
  font-size: 14px;
  color: #c9d1d9;
  font-style: italic;
}

.example-trans {
  font-size: 13px;
  color: #8b949e;
  margin-top: 4px;
}

.word-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.review-info {
  font-size: 12px;
  color: #8b949e;
}

.mastery-btns {
  display: flex;
  gap: 8px;
}

.review-card {
  text-align: center;
  padding: 40px 20px;
}

.review-word {
  font-size: 32px;
  font-weight: 600;
  color: #58a6ff;
  margin-bottom: 8px;
}

.review-phonetic {
  font-size: 16px;
  color: #8b949e;
  margin-bottom: 24px;
}

.review-meaning {
  font-size: 18px;
  color: #c9d1d9;
  margin-top: 24px;
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
