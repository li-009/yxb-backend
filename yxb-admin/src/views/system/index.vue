<template>
  <div class="system-page">
    <el-card>
      <template #header>系统设置</template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本设置" name="basic">
          <el-form :model="basicForm" label-width="120px" style="max-width: 500px;">
            <el-form-item label="系统名称">
              <el-input v-model="basicForm.name" />
            </el-form-item>
            <el-form-item label="系统Logo">
              <el-input v-model="basicForm.logo" placeholder="Logo URL" />
            </el-form-item>
            <el-form-item label="客服电话">
              <el-input v-model="basicForm.servicePhone" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSave">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="VIP设置" name="vip">
          <el-form :model="vipForm" label-width="120px" style="max-width: 500px;">
            <el-form-item label="月卡价格">
              <el-input-number v-model="vipForm.monthPrice" :min="0" :precision="2" />
            </el-form-item>
            <el-form-item label="季卡价格">
              <el-input-number v-model="vipForm.quarterPrice" :min="0" :precision="2" />
            </el-form-item>
            <el-form-item label="年卡价格">
              <el-input-number v-model="vipForm.yearPrice" :min="0" :precision="2" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSave">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const activeTab = ref('basic')

const basicForm = reactive({
  name: '鹦学伴',
  logo: '',
  servicePhone: '400-123-4567'
})

const vipForm = reactive({
  monthPrice: 29.90,
  quarterPrice: 69.90,
  yearPrice: 199.00
})

const handleSave = () => ElMessage.success('保存成功')
</script>
