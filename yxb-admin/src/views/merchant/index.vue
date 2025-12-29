<template>
  <div class="merchant-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>商户管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增商户
          </el-button>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="商户名称" min-width="150" />
        <el-table-column prop="contact" label="联系人" width="100" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="videoCount" label="视频数" width="80" />
        <el-table-column prop="balance" label="账户余额" width="120">
          <template #default="{ row }">
            ¥{{ row.balance }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status ? 'success' : 'danger'">
              {{ row.status ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="入驻时间" width="170" />
        <el-table-column label="操作" width="150" fixed="right">
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

const loading = ref(false)

const tableData = ref([
  { id: 1, name: '优学教育', contact: '张老师', phone: '138****1234', videoCount: 25, balance: '12,500.00', status: 1, createTime: '2024-11-01 10:00:00' },
  { id: 2, name: '语言之家', contact: '李经理', phone: '139****5678', videoCount: 18, balance: '8,200.00', status: 1, createTime: '2024-11-15 14:00:00' },
  { id: 3, name: '环球外语', contact: '王主任', phone: '137****9012', videoCount: 32, balance: '25,800.00', status: 1, createTime: '2024-10-20 09:00:00' }
])

const handleAdd = () => ElMessage.info('新增功能开发中')
const handleEdit = (row) => ElMessage.info(`编辑: ${row.name}`)
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除商户 "${row.name}" 吗？`, '提示', { type: 'warning' })
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
