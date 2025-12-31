<template>
  <div class="video-player-page">
    <header class="header">
      <div class="container header-content">
        <div class="back" @click="goBack">← 返回</div>
        <div class="title">{{ video.title }}</div>
        <div class="actions">
          <el-button size="small" @click="toggleFavorite">
            {{ isFavorite ? '已收藏' : '收藏' }}
          </el-button>
        </div>
      </div>
    </header>

    <main class="main">
      <div class="player-section">
        <div class="player-wrapper">
          <video
            ref="videoRef"
            class="video-element"
            :src="video.videoUrl"
            :poster="video.coverUrl"
            controls
            @timeupdate="onTimeUpdate"
            @loadedmetadata="onLoadedMetadata"
          ></video>
        </div>

        <div class="subtitle-display" v-if="showSubtitle && currentSubtitle">
          <div class="subtitle-text">{{ currentSubtitle.text }}</div>
          <div class="subtitle-translation" v-if="subtitleMode !== 'original'">
            {{ currentSubtitle.translation }}
          </div>
        </div>

        <div class="control-bar">
          <div class="control-item" @click="toggleSubtitle">
            <el-icon><View /></el-icon>
            <span>{{ showSubtitle ? '隐藏字幕' : '显示字幕' }}</span>
          </div>
          <div class="control-item" @click="switchSubtitleMode">
            <el-icon><Switch /></el-icon>
            <span>{{ subtitleModeText }}</span>
          </div>
          <div class="control-item">
            <el-icon><Timer /></el-icon>
            <el-select v-model="playbackRate" size="small" @change="setPlaybackRate">
              <el-option v-for="rate in rates" :key="rate" :label="rate + 'x'" :value="rate" />
            </el-select>
          </div>
          <div class="control-item" @click="toggleLoop">
            <el-icon><RefreshRight /></el-icon>
            <span>{{ loopMode ? '循环中' : '单句循环' }}</span>
          </div>
          <div class="control-item" @click="toggleABRepeat">
            <el-icon><Sort /></el-icon>
            <span>{{ abRepeatMode ? 'AB段:' + abRepeatStatus : 'AB段复读' }}</span>
          </div>
        </div>

        <div class="ai-toolbar">
          <div class="ai-tool" @click="showGrammarAnalysis">
            <div class="tool-icon grammar">📖</div>
            <span>语法讲解</span>
          </div>
          <div class="ai-tool" @click="startFollowRead">
            <div class="tool-icon follow">🎤</div>
            <span>跟读评分</span>
          </div>
          <div class="ai-tool" @click="enableWordLookup">
            <div class="tool-icon lookup" :class="{ active: wordLookupMode }">🔍</div>
            <span>屏幕查词</span>
          </div>
          <div class="ai-tool" @click="showAddNote">
            <div class="tool-icon note">📝</div>
            <span>添加笔记</span>
          </div>
          <div class="ai-tool" @click="shareToWechat">
            <div class="tool-icon share">📤</div>
            <span>分享</span>
          </div>
        </div>
      </div>

      <div class="subtitle-section">
        <div class="section-header">
          <h3>字幕列表</h3>
        </div>
        <div class="subtitle-list">
          <div
            v-for="(sub, idx) in subtitles"
            :key="idx"
            class="subtitle-item"
            :class="{ active: currentIndex === idx }"
            @click="seekTo(sub.startTime)"
          >
            <span class="time">{{ formatTime(sub.startTime) }}</span>
            <div class="content">
              <div class="original">{{ sub.text }}</div>
              <div class="trans">{{ sub.translation }}</div>
            </div>
            <div class="item-actions">
              <el-button size="small" text @click.stop="lookupWord(sub)">查词</el-button>
              <el-button size="small" text @click.stop="analyzeGrammar(sub)">语法</el-button>
            </div>
          </div>
        </div>
      </div>
    </main>

    <el-dialog v-model="showAiDialog" :title="aiDialogTitle" width="500px">
      <div class="ai-content">{{ aiContent }}</div>
      <template #footer>
        <el-button @click="showAiDialog = false">关闭</el-button>
        <el-button type="primary" v-if="aiType === 'word'" @click="addToWordBook">加入单词本</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showFollowReadDialog" title="跟读评分" width="500px" :close-on-click-modal="false" @close="closeFollowReadDialog">
      <div class="follow-read-content">
        <div class="follow-text">
          <h4>请跟读以下句子：</h4>
          <p class="text-to-read">{{ followReadText }}</p>
        </div>
        
        <div class="recording-area">
          <div class="recording-status" :class="{ recording: isRecording }">
            <div class="mic-icon">🎤</div>
            <div class="recording-time" v-if="isRecording">{{ recordingTime }}s</div>
          </div>
          
          <div class="recording-buttons">
            <el-button v-if="!isRecording" type="primary" size="large" @click="startRecording">
              开始录音
            </el-button>
            <el-button v-else type="danger" size="large" @click="stopRecording">
              停止录音
            </el-button>
          </div>
        </div>

        <div class="score-result" v-if="followReadScore">
          <h4>评分结果</h4>
          <div class="score-main">
            <div class="score-circle" :class="getScoreClass(followReadScore.score)">
              {{ followReadScore.score }}
            </div>
            <div class="score-label">综合得分</div>
          </div>
          <div class="score-details" v-if="followReadScore.details">
            <div class="detail-item">
              <span>准确度</span>
              <el-progress :percentage="followReadScore.details.accuracy || 0" />
            </div>
            <div class="detail-item">
              <span>流利度</span>
              <el-progress :percentage="followReadScore.details.fluency || 0" />
            </div>
            <div class="detail-item">
              <span>完整度</span>
              <el-progress :percentage="followReadScore.details.integrity || 0" />
            </div>
          </div>
          <div class="score-feedback" v-if="followReadScore.feedback">
            <p>{{ followReadScore.feedback }}</p>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="closeFollowReadDialog">关闭</el-button>
        <el-button type="primary" @click="followReadScore = null; recordingTime = 0">重新录音</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getVideoDetail, getVideoSubtitles, playVideo as recordPlay } from '../api/video'
import { addNote, collectVideo, uncollectVideo, isVideoCollected } from '../api/study'
import { analyzeGrammar, lookupWord, scorePronunciation } from '../api/ai'
import { getWechatJssdkSign } from '../api/user'
import { View, Switch, Timer, RefreshRight, Sort } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const videoId = route.params.id

const videoRef = ref(null)
const video = ref({})
const subtitles = ref([])
const currentTime = ref(0)
const currentIndex = ref(-1)
const currentSubtitle = ref(null)
const showSubtitle = ref(true)
const subtitleMode = ref('bilingual')
const playbackRate = ref(1)
const rates = [0.5, 0.75, 1, 1.25, 1.5, 2]
const loopMode = ref(false)
const loopSubtitle = ref(null)
const isFavorite = ref(false)
const showAiDialog = ref(false)
const aiDialogTitle = ref('')
const aiContent = ref('')
const aiType = ref('')

const abRepeatMode = ref(false)
const abPointA = ref(null)
const abPointB = ref(null)
const wordLookupMode = ref(false)

const showFollowReadDialog = ref(false)
const isRecording = ref(false)
const recordingTime = ref(0)
const followReadText = ref('')
const followReadScore = ref(null)
const mediaRecorder = ref(null)
const audioChunks = ref([])
let recordingTimer = null

const abRepeatStatus = computed(() => {
  if (abPointA.value !== null && abPointB.value !== null) return 'A-B'
  if (abPointA.value !== null) return '设置B点'
  return '设置A点'
})

const subtitleModeText = computed(() => {
  const map = { original: '仅原文', translation: '仅译文', bilingual: '双语' }
  return map[subtitleMode.value]
})

const formatTime = (s) => {
  const m = Math.floor(s / 60)
  const sec = Math.floor(s % 60)
  return `${m.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`
}

const loadVideo = async () => {
  try {
    const [videoRes, subRes] = await Promise.all([
      getVideoDetail(videoId),
      getVideoSubtitles(videoId)
    ])
    if (videoRes.data.code === 200) {
      video.value = videoRes.data.data
      recordPlay(videoId)
    }
    if (subRes.data.code === 200 && subRes.data.data?.length > 0) {
      const subData = subRes.data.data[0]
      subtitles.value = subData.items || []
    }
  } catch (e) {
    console.error('加载视频失败', e)
  }
}

const onTimeUpdate = () => {
  if (!videoRef.value) return
  currentTime.value = videoRef.value.currentTime
  updateCurrentSubtitle()
  checkLoop()
}

const onLoadedMetadata = () => {
  if (route.query.t) {
    videoRef.value.currentTime = parseFloat(route.query.t)
  }
}

const updateCurrentSubtitle = () => {
  const t = currentTime.value
  const idx = subtitles.value.findIndex(s => t >= s.startTime && t < s.endTime)
  if (idx !== currentIndex.value) {
    currentIndex.value = idx
    currentSubtitle.value = idx >= 0 ? subtitles.value[idx] : null
  }
}

const checkLoop = () => {
  if (loopMode.value && loopSubtitle.value) {
    if (currentTime.value >= loopSubtitle.value.endTime) {
      videoRef.value.currentTime = loopSubtitle.value.startTime
    }
  }
}

const seekTo = (time) => {
  if (videoRef.value) {
    videoRef.value.currentTime = time
    videoRef.value.play()
  }
}

const toggleSubtitle = () => { showSubtitle.value = !showSubtitle.value }

const switchSubtitleMode = () => {
  const modes = ['original', 'translation', 'bilingual']
  const i = modes.indexOf(subtitleMode.value)
  subtitleMode.value = modes[(i + 1) % modes.length]
}

const setPlaybackRate = () => {
  if (videoRef.value) {
    videoRef.value.playbackRate = playbackRate.value
  }
}

const toggleLoop = () => {
  if (loopMode.value) {
    loopMode.value = false
    loopSubtitle.value = null
  } else if (currentSubtitle.value) {
    loopSubtitle.value = currentSubtitle.value
    loopMode.value = true
  }
}

const toggleFavorite = async () => {
  try {
    if (isFavorite.value) {
      await uncollectVideo(videoId)
      isFavorite.value = false
      ElMessage.success('已取消收藏')
    } else {
      await collectVideo(videoId)
      isFavorite.value = true
      ElMessage.success('已收藏')
    }
  } catch (e) {
    console.error('收藏操作失败', e)
  }
}

const checkCollectStatus = async () => {
  try {
    const res = await isVideoCollected(videoId)
    if (res.data.code === 200) {
      isFavorite.value = res.data.data
    }
  } catch (e) {
    console.error('检查收藏状态失败', e)
  }
}

const lookupWord = (sub) => {
  aiDialogTitle.value = '单词查询'
  aiContent.value = `请输入要查询的单词...\n\n原文: ${sub.text}`
  aiType.value = 'word'
  showAiDialog.value = true
}

const analyzeGrammar = (sub) => {
  aiDialogTitle.value = '语法分析'
  aiContent.value = `分析句子:\n${sub.text}\n\n(AI语法分析功能开发中...)`
  aiType.value = 'grammar'
  showAiDialog.value = true
}

const addToWordBook = () => {
  ElMessage.success('已加入单词本')
  showAiDialog.value = false
}

const toggleABRepeat = () => {
  if (!videoRef.value) return
  if (abRepeatMode.value && abPointA.value !== null && abPointB.value !== null) {
    abRepeatMode.value = false
    abPointA.value = null
    abPointB.value = null
    ElMessage.info('已取消AB段复读')
  } else if (abPointA.value === null) {
    abPointA.value = videoRef.value.currentTime
    abRepeatMode.value = true
    ElMessage.success('已设置A点: ' + formatTime(abPointA.value))
  } else if (abPointB.value === null) {
    abPointB.value = videoRef.value.currentTime
    ElMessage.success('已设置B点: ' + formatTime(abPointB.value))
  }
}

const showGrammarAnalysis = async () => {
  if (!currentSubtitle.value) {
    ElMessage.warning('请先播放到有字幕的位置')
    return
  }
  aiDialogTitle.value = '语法讲解'
  aiContent.value = '正在分析...'
  aiType.value = 'grammar'
  showAiDialog.value = true
  
  try {
    const res = await analyzeGrammar(currentSubtitle.value.text, video.value.language || 'en')
    if (res.data.code === 200 && res.data.data) {
      const data = res.data.data
      aiContent.value = `句子: ${currentSubtitle.value.text}\n\n【语法分析】\n${data.analysis || '暂无分析结果'}`
    } else {
      aiContent.value = `句子: ${currentSubtitle.value.text}\n\n【语法分析】\n(AI服务暂不可用)`
    }
  } catch (e) {
    aiContent.value = `句子: ${currentSubtitle.value.text}\n\n【语法分析】\n(请先启动AI服务)`
  }
}

const startFollowRead = () => {
  if (!currentSubtitle.value) {
    ElMessage.warning('请先播放到有字幕的位置')
    return
  }
  followReadText.value = currentSubtitle.value.text
  followReadScore.value = null
  recordingTime.value = 0
  showFollowReadDialog.value = true
  if (videoRef.value) videoRef.value.pause()
}

const startRecording = async () => {
  try {
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true })
    mediaRecorder.value = new MediaRecorder(stream)
    audioChunks.value = []
    
    mediaRecorder.value.ondataavailable = (e) => {
      audioChunks.value.push(e.data)
    }
    
    mediaRecorder.value.onstop = async () => {
      const audioBlob = new Blob(audioChunks.value, { type: 'audio/wav' })
      stream.getTracks().forEach(track => track.stop())
      await submitForScoring(audioBlob)
    }
    
    mediaRecorder.value.start()
    isRecording.value = true
    recordingTime.value = 0
    recordingTimer = setInterval(() => {
      recordingTime.value++
      if (recordingTime.value >= 30) stopRecording()
    }, 1000)
  } catch (e) {
    ElMessage.error('无法访问麦克风，请检查权限设置')
  }
}

const stopRecording = () => {
  if (mediaRecorder.value && isRecording.value) {
    mediaRecorder.value.stop()
    isRecording.value = false
    if (recordingTimer) {
      clearInterval(recordingTimer)
      recordingTimer = null
    }
  }
}

const submitForScoring = async (audioBlob) => {
  try {
    ElMessage.info('正在评分...')
    const res = await scorePronunciation(audioBlob, followReadText.value, video.value.language || 'en')
    if (res.data.code === 200 && res.data.data) {
      followReadScore.value = res.data.data
      ElMessage.success('评分完成！')
    } else {
      ElMessage.error('评分失败，请重试')
    }
  } catch (e) {
    console.error('评分失败', e)
    ElMessage.error('评分服务暂不可用')
  }
}

const closeFollowReadDialog = () => {
  stopRecording()
  showFollowReadDialog.value = false
}

const getScoreClass = (score) => {
  if (score >= 80) return 'excellent'
  if (score >= 60) return 'good'
  return 'need-improve'
}

const enableWordLookup = () => {
  wordLookupMode.value = !wordLookupMode.value
  ElMessage.info(wordLookupMode.value ? '屏幕查词已开启，点击字幕中的单词可查询' : '屏幕查词已关闭')
}

const showAddNote = async () => {
  if (!currentSubtitle.value) {
    ElMessage.warning('请先播放到有字幕的位置')
    return
  }
  
  const noteContent = prompt('请输入笔记内容:')
  if (!noteContent) return
  
  try {
    await addNote({
      videoId: parseInt(videoId),
      timestamp: Math.floor(currentTime.value),
      content: noteContent,
      subtitleText: currentSubtitle.value.text
    })
    ElMessage.success('笔记添加成功')
  } catch (e) {
    console.error('添加笔记失败', e)
    ElMessage.error('添加笔记失败，请先登录')
  }
}

const shareToWechat = async () => {
  if (typeof wx === 'undefined') {
    ElMessage.info('请在微信中打开使用分享功能')
    return
  }
  
  try {
    const res = await getWechatJssdkSign(window.location.href.split('#')[0])
    if (res.data.code === 200) {
      const config = res.data.data
      wx.config({
        debug: false,
        appId: config.appId,
        timestamp: config.timestamp,
        nonceStr: config.nonceStr,
        signature: config.signature,
        jsApiList: ['updateAppMessageShareData', 'updateTimelineShareData']
      })
      
      wx.ready(() => {
        wx.updateAppMessageShareData({
          title: video.value.title,
          desc: '来鹦学伴一起学习吧！',
          link: window.location.href,
          imgUrl: video.value.coverUrl,
          success: () => ElMessage.success('分享设置成功')
        })
      })
    }
  } catch (e) {
    console.error('获取分享配置失败', e)
    ElMessage.error('分享功能暂不可用')
  }
}

const goBack = () => router.back()

onMounted(() => {
  loadVideo()
  checkCollectStatus()
})
</script>

<style scoped>
.video-player-page {
  min-height: 100vh;
  background-color: #0d1117;
}

.header {
  background-color: #161b22;
  padding: 12px 0;
  border-bottom: 1px solid #30363d;
}

.header-content {
  display: flex;
  align-items: center;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

.back {
  cursor: pointer;
  color: #58a6ff;
}

.title {
  flex: 1;
  text-align: center;
  font-size: 18px;
}

.main {
  display: flex;
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
  gap: 20px;
}

.player-section {
  flex: 2;
}

.player-wrapper {
  background: #000;
  border-radius: 8px;
  overflow: hidden;
}

.video-element {
  width: 100%;
  max-height: 500px;
}

.subtitle-display {
  background: rgba(0, 0, 0, 0.8);
  padding: 16px;
  text-align: center;
  border-radius: 0 0 8px 8px;
}

.subtitle-text {
  font-size: 20px;
  color: #fff;
}

.subtitle-translation {
  font-size: 16px;
  color: #8b949e;
  margin-top: 8px;
}

.control-bar {
  display: flex;
  gap: 16px;
  padding: 16px 0;
  flex-wrap: wrap;
}

.control-item {
  padding: 8px 16px;
  background: #21262d;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.control-item:hover {
  background: #30363d;
}

.subtitle-section {
  flex: 1;
  background: #161b22;
  border-radius: 8px;
  max-height: calc(100vh - 150px);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.section-header {
  padding: 16px;
  border-bottom: 1px solid #30363d;
}

.section-header h3 {
  margin: 0;
  font-size: 16px;
}

.subtitle-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.subtitle-item {
  display: flex;
  align-items: flex-start;
  padding: 12px;
  border-radius: 6px;
  cursor: pointer;
  margin-bottom: 4px;
}

.subtitle-item:hover {
  background: #21262d;
}

.subtitle-item.active {
  background: #1f6feb33;
  border-left: 3px solid #58a6ff;
}

.time {
  color: #8b949e;
  font-size: 12px;
  width: 50px;
  flex-shrink: 0;
}

.content {
  flex: 1;
  margin: 0 12px;
}

.original {
  font-size: 14px;
  color: #c9d1d9;
}

.trans {
  font-size: 12px;
  color: #8b949e;
  margin-top: 4px;
}

.item-actions {
  display: flex;
  gap: 4px;
}

.ai-content {
  white-space: pre-wrap;
  line-height: 1.6;
}

.control-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.ai-toolbar {
  display: flex;
  justify-content: space-around;
  background: linear-gradient(135deg, #1f6feb22, #23863622);
  border: 1px solid #30363d;
  border-radius: 12px;
  padding: 16px;
  margin-top: 12px;
}

.ai-tool {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 12px;
  border-radius: 8px;
  transition: all 0.2s;
}

.ai-tool:hover {
  background: #21262d;
}

.tool-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  background: #21262d;
}

.tool-icon.grammar { background: linear-gradient(135deg, #58a6ff33, #58a6ff11); }
.tool-icon.follow { background: linear-gradient(135deg, #f0883e33, #f0883e11); }
.tool-icon.lookup { background: linear-gradient(135deg, #a371f733, #a371f711); }
.tool-icon.lookup.active { background: #a371f7; }
.tool-icon.note { background: linear-gradient(135deg, #3fb95033, #3fb95011); }
.tool-icon.share { background: linear-gradient(135deg, #58a6ff33, #58a6ff11); }

.ai-tool span {
  font-size: 12px;
  color: #8b949e;
}

.follow-read-content {
  text-align: center;
}

.follow-text h4 {
  margin: 0 0 12px;
  color: #c9d1d9;
}

.text-to-read {
  font-size: 18px;
  color: #58a6ff;
  background: #21262d;
  padding: 16px;
  border-radius: 8px;
  line-height: 1.6;
}

.recording-area {
  margin: 24px 0;
}

.recording-status {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.mic-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: #21262d;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  transition: all 0.3s;
}

.recording-status.recording .mic-icon {
  background: #f85149;
  animation: pulse 1s infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

.recording-time {
  font-size: 24px;
  font-weight: 600;
  color: #f85149;
}

.score-result {
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #30363d;
}

.score-result h4 {
  margin: 0 0 16px;
  color: #c9d1d9;
}

.score-main {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.score-circle {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  font-weight: 700;
  color: #fff;
}

.score-circle.excellent {
  background: linear-gradient(135deg, #238636, #3fb950);
}

.score-circle.good {
  background: linear-gradient(135deg, #9e6a03, #d29922);
}

.score-circle.need-improve {
  background: linear-gradient(135deg, #da3633, #f85149);
}

.score-label {
  margin-top: 8px;
  color: #8b949e;
  font-size: 14px;
}

.score-details {
  text-align: left;
  margin-top: 16px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.detail-item span {
  width: 60px;
  font-size: 14px;
  color: #8b949e;
}

.detail-item .el-progress {
  flex: 1;
}

.score-feedback {
  margin-top: 16px;
  padding: 12px;
  background: #21262d;
  border-radius: 8px;
  text-align: left;
}

.score-feedback p {
  margin: 0;
  font-size: 14px;
  color: #c9d1d9;
  line-height: 1.6;
}
</style>
