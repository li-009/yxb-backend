<template>
  <div class="video-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的视频</span>
          <el-button type="primary" @click="handleUpload"><el-icon><Upload /></el-icon>上传视频</el-button>
        </div>
      </template>
      
      <el-table :data="tableData" stripe>
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column prop="duration" label="时长" width="80" />
        <el-table-column prop="playCount" label="播放量" width="100" />
        <el-table-column prop="income" label="收益" width="100">
          <template #default="{ row }">¥{{ row.income }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : row.status === 0 ? 'warning' : 'info'">
              {{ ['审核中', '已发布', '已下架'][row.status] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
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

const tableData = ref([
  { id: 1, title: '英语口语入门第一课', category: '英语', duration: '15:30', playCount: 1256, income: '125.60', status: 1 },
  { id: 2, title: '日语五十音图详解', category: '日语', duration: '25:00', playCount: 890, income: '89.00', status: 1 },
  { id: 3, title: '法语基础发音', category: '法语', duration: '20:15', playCount: 0, income: '0.00', status: 0 }
])

const handleUpload = () => ElMessage.info('上传功能开发中')
const handleEdit = (row) => ElMessage.info(`编辑: ${row.title}`)
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除 "${row.title}" 吗？`, '提示', { type: 'warning' })
    .then(() => ElMessage.success('删除成功')).catch(() => {})
}
</script>

<style lang="scss" scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
