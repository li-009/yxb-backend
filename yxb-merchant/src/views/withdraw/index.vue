<template>
  <div class="withdraw-page">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <div class="balance-info">
            <div class="label">可提现余额</div>
            <div class="value">¥5,230.00</div>
            <el-button type="primary" style="margin-top: 20px;" @click="handleWithdraw">申请提现</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <template #header>提现记录</template>
          <el-table :data="tableData" stripe>
            <el-table-column prop="createTime" label="申请时间" width="170" />
            <el-table-column prop="amount" label="金额" width="120">
              <template #default="{ row }">¥{{ row.amount }}</template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="['warning', 'success', 'danger'][row.status]">
                  {{ ['处理中', '已到账', '已拒绝'][row.status] }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const tableData = ref([
  { createTime: '2024-12-20 10:00:00', amount: '1,000.00', status: 1, remark: '已打款至尾号1234银行卡' },
  { createTime: '2024-12-15 14:30:00', amount: '2,000.00', status: 1, remark: '已打款至尾号1234银行卡' }
])

const handleWithdraw = () => ElMessage.info('提现功能开发中')
</script>

<style lang="scss" scoped>
.balance-info { text-align: center; padding: 20px;
  .label { font-size: 14px; color: #999; }
  .value { font-size: 36px; font-weight: 600; color: #11998e; margin-top: 10px; }
}
</style>
