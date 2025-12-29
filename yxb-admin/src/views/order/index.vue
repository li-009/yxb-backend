<template>
  <div class="order-page">
    <el-card>
      <template #header>订单管理</template>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="orderNo" label="订单号" width="200" />
        <el-table-column prop="userName" label="用户" width="120" />
        <el-table-column prop="productName" label="商品" min-width="150" />
        <el-table-column prop="amount" label="金额" width="100">
          <template #default="{ row }">¥{{ row.amount }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusType[row.status]">{{ statusText[row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="170" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const statusType = { 0: 'warning', 1: 'success', 2: 'danger', 3: 'info' }
const statusText = { 0: '待支付', 1: '已完成', 2: '已取消', 3: '已退款' }

const tableData = ref([
  { orderNo: 'YXB202412230001', userName: '学习达人', productName: 'VIP月卡', amount: '29.90', status: 1, createTime: '2024-12-23 10:30:00' },
  { orderNo: 'YXB202412220002', userName: '小明', productName: 'VIP年卡', amount: '199.00', status: 1, createTime: '2024-12-22 15:20:00' },
  { orderNo: 'YXB202412210003', userName: '外语爱好者', productName: 'VIP季卡', amount: '69.90', status: 0, createTime: '2024-12-21 09:00:00' }
])

const handleView = (row) => ElMessage.info(`查看订单: ${row.orderNo}`)
</script>

<style lang="scss" scoped>
</style>
