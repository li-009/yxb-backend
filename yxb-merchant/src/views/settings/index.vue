<template>
  <div class="settings-page">
    <el-card>
      <template #header>账户设置</template>
      <el-form :model="form" label-width="100px" style="max-width: 500px;">
        <el-form-item label="商户名称">
          <el-input v-model="form.merchantName" />
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="form.contact" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="银行卡号">
          <el-input v-model="form.bankCard" placeholder="用于提现" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'

const form = reactive({
  merchantName: '优学教育',
  contact: '张老师',
  phone: '138****1234',
  bankCard: '6222****1234'
})

const handleSave = () => ElMessage.success('保存成功')
</script>
