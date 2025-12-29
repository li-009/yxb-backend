<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #409eff;">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.userCount }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #67c23a;">
            <el-icon><VideoCamera /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.videoCount }}</div>
            <div class="stat-label">视频总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #e6a23c;">
            <el-icon><Shop /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.merchantCount }}</div>
            <div class="stat-label">商户数量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #f56c6c;">
            <el-icon><Money /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">¥{{ stats.totalIncome }}</div>
            <div class="stat-label">总收入</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="16">
        <el-card>
          <template #header>
            <span>最近7天数据趋势</span>
          </template>
          <div ref="chartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>最新用户</span>
          </template>
          <el-table :data="recentUsers" size="small">
            <el-table-column prop="nickname" label="昵称" />
            <el-table-column prop="createTime" label="注册时间" width="100" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import * as echarts from 'echarts'

const chartRef = ref(null)

const stats = reactive({
  userCount: 1256,
  videoCount: 89,
  merchantCount: 12,
  totalIncome: '28,650'
})

const recentUsers = ref([
  { nickname: '小明', createTime: '12-23' },
  { nickname: '学习达人', createTime: '12-22' },
  { nickname: '外语爱好者', createTime: '12-22' },
  { nickname: '英语学习者', createTime: '12-21' },
  { nickname: '日语小白', createTime: '12-20' }
])

onMounted(() => {
  initChart()
})

const initChart = () => {
  const chart = echarts.init(chartRef.value)
  const option = {
    tooltip: { trigger: 'axis' },
    legend: { data: ['新增用户', '视频播放', '订单数'] },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      data: ['12-17', '12-18', '12-19', '12-20', '12-21', '12-22', '12-23']
    },
    yAxis: { type: 'value' },
    series: [
      {
        name: '新增用户',
        type: 'line',
        smooth: true,
        data: [12, 15, 18, 22, 19, 25, 28]
      },
      {
        name: '视频播放',
        type: 'line',
        smooth: true,
        data: [320, 380, 420, 510, 480, 550, 620]
      },
      {
        name: '订单数',
        type: 'line',
        smooth: true,
        data: [5, 8, 6, 12, 9, 15, 18]
      }
    ]
  }
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}
</script>

<style lang="scss" scoped>
.dashboard {
  .stat-card {
    :deep(.el-card__body) {
      display: flex;
      align-items: center;
      gap: 16px;
    }
    
    .stat-icon {
      width: 64px;
      height: 64px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      
      .el-icon {
        font-size: 32px;
        color: #fff;
      }
    }
    
    .stat-info {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #333;
      }
      
      .stat-label {
        font-size: 14px;
        color: #999;
        margin-top: 4px;
      }
    }
  }
}
</style>
