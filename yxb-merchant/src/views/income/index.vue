<template>
  <div class="income-page">
    <el-card>
      <template #header>收益明细</template>
      <el-table :data="tableData" stripe>
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="videoTitle" label="视频" min-width="200" />
        <el-table-column prop="playCount" label="播放量" width="100" />
        <el-table-column prop="income" label="收益" width="120">
          <template #default="{ row }">
            <span style="color: #67c23a;">+¥{{ row.income }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const tableData = ref([
  { date: '2024-12-23', videoTitle: '英语口语入门第一课', playCount: 56, income: '5.60' },
  { date: '2024-12-22', videoTitle: '英语口语入门第一课', playCount: 48, income: '4.80' },
  { date: '2024-12-22', videoTitle: '日语五十音图详解', playCount: 32, income: '3.20' },
  { date: '2024-12-21', videoTitle: '英语口语入门第一课', playCount: 65, income: '6.50' }
])
</script>
