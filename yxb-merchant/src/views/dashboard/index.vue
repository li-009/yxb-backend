<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #409eff;"><el-icon><VideoCamera /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.videoCount }}</div>
            <div class="stat-label">视频总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #67c23a;"><el-icon><View /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalPlay }}</div>
            <div class="stat-label">总播放量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #e6a23c;"><el-icon><Money /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">¥{{ stats.totalIncome }}</div>
            <div class="stat-label">累计收益</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #f56c6c;"><el-icon><Wallet /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">¥{{ stats.balance }}</div>
            <div class="stat-label">可提现余额</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-card style="margin-top: 20px;">
      <template #header>近7天收益趋势</template>
      <div ref="chartRef" style="height: 300px;"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import * as echarts from 'echarts'

const chartRef = ref(null)
const stats = reactive({
  videoCount: 25,
  totalPlay: '12,580',
  totalIncome: '8,650.00',
  balance: '5,230.00'
})

onMounted(() => {
  const chart = echarts.init(chartRef.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: ['12-17', '12-18', '12-19', '12-20', '12-21', '12-22', '12-23'] },
    yAxis: { type: 'value' },
    series: [{ name: '收益', type: 'bar', data: [120, 150, 180, 220, 190, 250, 280], itemStyle: { color: '#11998e' } }]
  })
  window.addEventListener('resize', () => chart.resize())
})
</script>

<style lang="scss" scoped>
.stat-card {
  :deep(.el-card__body) { display: flex; align-items: center; gap: 16px; }
  .stat-icon {
    width: 60px; height: 60px; border-radius: 8px;
    display: flex; align-items: center; justify-content: center;
    .el-icon { font-size: 28px; color: #fff; }
  }
  .stat-info {
    .stat-value { font-size: 24px; font-weight: 600; color: #333; }
    .stat-label { font-size: 14px; color: #999; margin-top: 4px; }
  }
}
</style>
