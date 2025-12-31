<template>
  <div class="import-page">
    <header class="header">
      <div class="container header-content">
        <div class="back" @click="goBack">← 返回</div>
        <h1>视频导入</h1>
      </div>
    </header>

    <main class="main container">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="URL导入" name="url">
          <div class="import-form">
            <el-form :model="urlForm" label-position="top">
              <el-form-item label="视频链接" required>
                <el-input v-model="urlForm.videoUrl" placeholder="输入视频URL（支持YouTube、Bilibili等）" />
              </el-form-item>
              <el-form-item label="视频标题" required>
                <el-input v-model="urlForm.title" placeholder="输入视频标题" />
              </el-form-item>
              <el-form-item label="视频描述">
                <el-input v-model="urlForm.description" type="textarea" rows="3" placeholder="输入视频描述" />
              </el-form-item>
              <el-form-item label="封面图片">
                <el-input v-model="urlForm.coverUrl" placeholder="输入封面图片URL" />
              </el-form-item>
              <div class="form-row">
                <el-form-item label="语种">
                  <el-select v-model="urlForm.language">
                    <el-option label="英语" value="en" />
                    <el-option label="日语" value="ja" />
                    <el-option label="韩语" value="ko" />
                  </el-select>
                </el-form-item>
                <el-form-item label="难度">
                  <el-select v-model="urlForm.level">
                    <el-option label="入门" :value="1" />
                    <el-option label="初级" :value="2" />
                    <el-option label="中级" :value="3" />
                    <el-option label="高级" :value="4" />
                  </el-select>
                </el-form-item>
                <el-form-item label="时长(秒)">
                  <el-input-number v-model="urlForm.duration" :min="0" />
                </el-form-item>
              </div>
              <el-button type="primary" size="large" @click="handleUrlImport" :loading="loading">
                导入视频
              </el-button>
            </el-form>
          </div>
        </el-tab-pane>

        <el-tab-pane label="本地上传" name="upload">
          <div class="upload-form">
            <el-upload
              class="upload-area"
              drag
              :auto-upload="false"
              :on-change="handleFileChange"
              accept="video/*"
            >
              <el-icon class="el-icon--upload"><Upload /></el-icon>
              <div class="el-upload__text">
                拖拽视频文件到此处，或 <em>点击上传</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">支持 mp4, webm, mkv 格式，最大 500MB</div>
              </template>
            </el-upload>

            <el-form :model="uploadForm" label-position="top" v-if="selectedFile">
              <el-form-item label="视频标题" required>
                <el-input v-model="uploadForm.title" placeholder="输入视频标题" />
              </el-form-item>
              <el-form-item label="视频描述">
                <el-input v-model="uploadForm.description" type="textarea" rows="3" />
              </el-form-item>
              <div class="form-row">
                <el-form-item label="语种">
                  <el-select v-model="uploadForm.language">
                    <el-option label="英语" value="en" />
                    <el-option label="日语" value="ja" />
                    <el-option label="韩语" value="ko" />
                  </el-select>
                </el-form-item>
                <el-form-item label="难度">
                  <el-select v-model="uploadForm.level">
                    <el-option label="入门" :value="1" />
                    <el-option label="初级" :value="2" />
                    <el-option label="中级" :value="3" />
                    <el-option label="高级" :value="4" />
                  </el-select>
                </el-form-item>
              </div>
              <el-button type="primary" size="large" @click="handleUpload" :loading="loading">
                上传视频
              </el-button>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { importVideoByUrl, uploadVideo } from '../api/video'
import { Upload } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const activeTab = ref('url')
const loading = ref(false)
const selectedFile = ref(null)

const urlForm = ref({
  videoUrl: '',
  title: '',
  description: '',
  coverUrl: '',
  language: 'en',
  level: 1,
  duration: 0
})

const uploadForm = ref({
  title: '',
  description: '',
  language: 'en',
  level: 1
})

const handleFileChange = (file) => {
  selectedFile.value = file.raw
  uploadForm.value.title = file.name.replace(/\.[^/.]+$/, '')
}

const handleUrlImport = async () => {
  if (!urlForm.value.videoUrl || !urlForm.value.title) {
    ElMessage.warning('请填写视频链接和标题')
    return
  }
  loading.value = true
  try {
    const res = await importVideoByUrl(urlForm.value)
    if (res.data.code === 200) {
      ElMessage.success('视频导入成功')
      router.push(`/video/${res.data.data.id}`)
    } else {
      ElMessage.error(res.data.message || '导入失败')
    }
  } catch (e) {
    console.error('导入失败', e)
    ElMessage.error('导入失败，请检查视频链接')
  } finally {
    loading.value = false
  }
}

const handleUpload = async () => {
  if (!selectedFile.value || !uploadForm.value.title) {
    ElMessage.warning('请选择视频文件并填写标题')
    return
  }
  loading.value = true
  try {
    const res = await uploadVideo(
      selectedFile.value,
      uploadForm.value.title,
      uploadForm.value.description,
      uploadForm.value.language,
      uploadForm.value.level
    )
    if (res.data.code === 200) {
      ElMessage.success('视频上传成功')
      router.push(`/video/${res.data.data.id}`)
    } else {
      ElMessage.error(res.data.message || '上传失败')
    }
  } catch (e) {
    console.error('上传失败', e)
    ElMessage.error('上传失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => router.back()
</script>

<style scoped>
.import-page {
  min-height: 100vh;
  background: #0d1117;
}

.header {
  background: #161b22;
  padding: 16px 0;
  border-bottom: 1px solid #30363d;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 20px;
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.back {
  cursor: pointer;
  color: #58a6ff;
}

h1 {
  font-size: 20px;
  margin: 0;
}

.main {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px 20px;
}

.import-form, .upload-form {
  padding: 24px 0;
}

.form-row {
  display: flex;
  gap: 20px;
}

.form-row .el-form-item {
  flex: 1;
}

.upload-area {
  width: 100%;
  margin-bottom: 24px;
}

:deep(.el-upload-dragger) {
  background: #161b22;
  border-color: #30363d;
}

:deep(.el-upload-dragger:hover) {
  border-color: #58a6ff;
}

:deep(.el-icon--upload) {
  font-size: 48px;
  color: #8b949e;
}
</style>
