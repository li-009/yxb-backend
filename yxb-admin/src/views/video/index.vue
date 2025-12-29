<template>
  <div class="video-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>视频管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>上传视频
          </el-button>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column prop="language" label="语言" width="80" />
        <el-table-column prop="duration" label="时长" width="80" />
        <el-table-column prop="playCount" label="播放量" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status ? 'success' : 'info'">
              {{ row.status ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="primary" @click="handlePreview(row)">预览</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)

const tableData = ref([
  { id: 1, title: '英语口语入门第一课', category: '英语', language: 'EN', duration: '15:30', playCount: 1256, status: 1, createTime: '2024-12-20 10:00:00' },
  { id: 2, title: '日语五十音图详解', category: '日语', language: 'JP', duration: '25:00', playCount: 890, status: 1, createTime: '2024-12-19 14:00:00' },
  { id: 3, title: '法语基础发音教程', category: '法语', language: 'FR', duration: '20:15', playCount: 456, status: 1, createTime: '2024-12-18 09:00:00' }
])

const handleAdd = () => ElMessage.info('上传功能开发中')
const handleEdit = (row) => ElMessage.info(`编辑: ${row.title}`)
const handlePreview = (row) => ElMessage.info(`预览: ${row.title}`)
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除 "${row.title}" 吗？`, '提示', { type: 'warning' })
    .then(() => ElMessage.success('删除成功'))
    .catch(() => {})
}
</script>

<style lang="scss" scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
