<template>
  <div class="notes-page">
    <header class="header">
      <div class="container header-content">
        <div class="back" @click="goBack">←</div>
        <h1>我的笔记</h1>
        <div></div>
      </div>
    </header>

    <main class="main container">
      <div class="filter-bar">
        <el-select v-model="filterVideo" placeholder="全部视频" size="small" clearable @change="loadNotes">
          <el-option label="全部视频" value="" />
        </el-select>
      </div>

      <div class="note-list" v-loading="loading">
        <div class="note-card" v-for="note in notes" :key="note.id">
          <div class="note-header">
            <span class="video-title">{{ note.videoTitle || '未知视频' }}</span>
            <span class="timestamp">{{ formatTime(note.timestamp) }}</span>
          </div>
          <div class="note-subtitle" v-if="note.subtitleText">
            "{{ note.subtitleText }}"
          </div>
          <div class="note-content">{{ note.content }}</div>
          <div class="note-footer">
            <span class="create-time">{{ formatDate(note.createTime) }}</span>
            <div class="actions">
              <el-button size="small" text @click="goToVideo(note)">跳转视频</el-button>
              <el-button size="small" text type="danger" @click="handleDelete(note)">删除</el-button>
            </div>
          </div>
        </div>
      </div>

      <el-empty v-if="!loading && notes.length === 0" description="暂无笔记" />

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
import { getNoteList, deleteNote } from '../api/study'
import { HomeFilled, FolderOpened, Plus, DataLine, User } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const notes = ref([])
const loading = ref(false)
const filterVideo = ref('')
const pageNum = ref(1)
const pageSize = ref(20)
const total = ref(0)

const formatTime = (seconds) => {
  if (!seconds) return '00:00'
  const m = Math.floor(seconds / 60)
  const s = seconds % 60
  return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const loadNotes = async () => {
  loading.value = true
  try {
    const res = await getNoteList(filterVideo.value || undefined, pageNum.value, pageSize.value)
    if (res.data.code === 200) {
      notes.value = res.data.data.records || []
      total.value = res.data.data.total || 0
    }
  } catch (e) {
    console.error('加载笔记失败', e)
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page) => {
  pageNum.value = page
  loadNotes()
}

const handleDelete = (note) => {
  ElMessageBox.confirm('确定删除这条笔记吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteNote(note.id)
      ElMessage.success('删除成功')
      loadNotes()
    } catch (e) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const goToVideo = (note) => {
  router.push(`/video/${note.videoId}?t=${note.timestamp}`)
}

const goBack = () => router.back()
const goTo = (path) => router.push(path)

onMounted(() => {
  loadNotes()
})
</script>

<style scoped>
.notes-page {
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

.filter-bar {
  margin-bottom: 20px;
}

.note-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.note-card {
  background: #161b22;
  border: 1px solid #30363d;
  border-radius: 12px;
  padding: 16px;
}

.note-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.video-title {
  font-size: 14px;
  color: #58a6ff;
}

.timestamp {
  font-size: 12px;
  color: #8b949e;
  background: #21262d;
  padding: 2px 8px;
  border-radius: 4px;
}

.note-subtitle {
  font-size: 13px;
  color: #8b949e;
  font-style: italic;
  margin-bottom: 8px;
  padding-left: 12px;
  border-left: 3px solid #30363d;
}

.note-content {
  font-size: 15px;
  color: #c9d1d9;
  line-height: 1.6;
  margin-bottom: 12px;
}

.note-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.create-time {
  font-size: 12px;
  color: #8b949e;
}

.actions {
  display: flex;
  gap: 8px;
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
