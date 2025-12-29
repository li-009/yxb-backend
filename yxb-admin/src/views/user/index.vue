<template>
  <div class="user-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="handleExport">
            <el-icon><Download /></el-icon>导出
          </el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="query" class="search-form">
        <el-form-item label="用户昵称">
          <el-input v-model="query.nickname" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="query.phone" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="请选择" clearable>
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="vipStatus" label="VIP状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.vipStatus ? 'warning' : 'info'">
              {{ row.vipStatus ? 'VIP' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalLearningDays" label="学习天数" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status ? 'success' : 'danger'">
              {{ row.status ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="170" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link :type="row.status ? 'danger' : 'success'" @click="handleToggleStatus(row)">
              {{ row.status ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-model:current-page="query.page"
        v-model:page-size="query.size"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchData"
        @current-change="fetchData"
        style="margin-top: 20px; justify-content: flex-end;"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const total = ref(0)

const query = reactive({
  nickname: '',
  phone: '',
  status: null,
  page: 1,
  size: 10
})

const tableData = ref([
  { id: 1, nickname: '学习达人', phone: '138****1234', vipStatus: 1, totalLearningDays: 45, status: 1, createTime: '2024-12-20 10:30:00' },
  { id: 2, nickname: '外语爱好者', phone: '139****5678', vipStatus: 0, totalLearningDays: 12, status: 1, createTime: '2024-12-19 15:20:00' },
  { id: 3, nickname: '小明', phone: '137****9012', vipStatus: 1, totalLearningDays: 88, status: 1, createTime: '2024-12-18 09:00:00' },
  { id: 4, nickname: '英语小白', phone: '136****3456', vipStatus: 0, totalLearningDays: 5, status: 0, createTime: '2024-12-17 14:45:00' },
  { id: 5, nickname: '日语学习者', phone: '135****7890', vipStatus: 1, totalLearningDays: 120, status: 1, createTime: '2024-12-15 11:30:00' }
])

onMounted(() => {
  fetchData()
})

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    total.value = 156
    loading.value = false
  }, 500)
}

const handleSearch = () => {
  query.page = 1
  fetchData()
}

const handleReset = () => {
  Object.assign(query, { nickname: '', phone: '', status: null, page: 1 })
  fetchData()
}

const handleView = (row) => {
  ElMessage.info(`查看用户: ${row.nickname}`)
}

const handleToggleStatus = (row) => {
  const action = row.status ? '禁用' : '启用'
  ElMessageBox.confirm(`确定要${action}用户 "${row.nickname}" 吗？`, '提示', {
    type: 'warning'
  }).then(() => {
    row.status = row.status ? 0 : 1
    ElMessage.success(`${action}成功`)
  }).catch(() => {})
}

const handleExport = () => {
  ElMessage.info('导出功能开发中')
}
</script>

<style lang="scss" scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}
</style>
