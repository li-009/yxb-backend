<template>
  <view class="login-page">
    <!-- Logo区域 -->
    <view class="logo-area">
      <text class="logo-icon">🦜</text>
      <text class="logo-text">鹦学伴</text>
      <text class="logo-slogan">外语学习视频助手</text>
    </view>

    <!-- 登录方式 -->
    <view class="login-area">
      <button class="login-btn wechat" open-type="getPhoneNumber" @getphonenumber="onGetPhoneNumber">
        <text class="btn-icon">📱</text>
        <text class="btn-text">微信手机号快捷登录</text>
      </button>
      
      <button class="login-btn" @click="wxLogin">
        <text class="btn-icon">💬</text>
        <text class="btn-text">微信授权登录</text>
      </button>

      <view class="divider">
        <view class="divider-line"></view>
        <text class="divider-text">或</text>
        <view class="divider-line"></view>
      </view>

      <view class="phone-login">
        <view class="input-group">
          <text class="input-prefix">+86</text>
          <input type="number" placeholder="请输入手机号" v-model="phone" maxlength="11" />
        </view>
        <view class="input-group">
          <input type="number" placeholder="请输入验证码" v-model="code" maxlength="6" />
          <button class="code-btn" :disabled="countdown > 0" @click="sendCode">
            {{ countdown > 0 ? countdown + 's' : '获取验证码' }}
          </button>
        </view>
        <button class="login-btn primary" @click="phoneLogin" :disabled="!phone || !code">
          登录
        </button>
      </view>
    </view>

    <!-- 协议 -->
    <view class="agreement">
      <checkbox :checked="agreed" @tap="agreed = !agreed" />
      <text class="agreement-text">
        我已阅读并同意
        <text class="link" @click="showAgreement('user')">《用户协议》</text>
        和
        <text class="link" @click="showAgreement('privacy')">《隐私政策》</text>
      </text>
    </view>
  </view>
</template>

<script>
import { miniappLogin, miniappPhone } from '../../api/wechat'

export default {
  data() {
    return {
      phone: '',
      code: '',
      countdown: 0,
      agreed: false,
      wxCode: ''
    }
  },
  methods: {
    async wxLogin() {
      if (!this.agreed) {
        uni.showToast({ title: '请先同意用户协议', icon: 'none' })
        return
      }
      try {
        const loginRes = await new Promise((resolve, reject) => {
          uni.login({
            success: resolve,
            fail: reject
          })
        })
        this.wxCode = loginRes.code
        const res = await miniappLogin(loginRes.code)
        if (res.code === 200) {
          this.handleLoginSuccess(res.data)
        } else {
          uni.showToast({ title: res.message || '登录失败', icon: 'none' })
        }
      } catch (e) {
        console.error('微信登录失败', e)
        uni.showToast({ title: '登录失败，请重试', icon: 'none' })
      }
    },
    async onGetPhoneNumber(e) {
      if (!this.agreed) {
        uni.showToast({ title: '请先同意用户协议', icon: 'none' })
        return
      }
      if (e.detail.errMsg !== 'getPhoneNumber:ok') {
        console.log('用户拒绝授权手机号')
        return
      }
      try {
        // 先获取登录code
        if (!this.wxCode) {
          const loginRes = await new Promise((resolve, reject) => {
            uni.login({ success: resolve, fail: reject })
          })
          this.wxCode = loginRes.code
        }
        // 调用后端接口获取手机号并登录
        const res = await miniappPhone(e.detail.code)
        if (res.code === 200) {
          this.handleLoginSuccess(res.data)
        } else {
          uni.showToast({ title: res.message || '获取手机号失败', icon: 'none' })
        }
      } catch (e) {
        console.error('获取手机号失败', e)
        uni.showToast({ title: '获取手机号失败', icon: 'none' })
      }
    },
    sendCode() {
      if (!this.phone || this.phone.length !== 11) {
        uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
        return
      }
      // TODO: 调用发送验证码接口
      uni.showToast({ title: '验证码已发送', icon: 'success' })
      this.countdown = 60
      const timer = setInterval(() => {
        this.countdown--
        if (this.countdown <= 0) {
          clearInterval(timer)
        }
      }, 1000)
    },
    phoneLogin() {
      if (!this.agreed) {
        uni.showToast({ title: '请先同意用户协议', icon: 'none' })
        return
      }
      if (!this.phone || this.phone.length !== 11) {
        uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
        return
      }
      if (!this.code || this.code.length !== 6) {
        uni.showToast({ title: '请输入6位验证码', icon: 'none' })
        return
      }
      // TODO: 调用手机号登录接口
      uni.showToast({ title: '功能开发中', icon: 'none' })
    },
    handleLoginSuccess(data) {
      uni.setStorageSync('token', data.token)
      uni.setStorageSync('userInfo', JSON.stringify(data.userInfo || {}))
      uni.showToast({ title: '登录成功', icon: 'success' })
      setTimeout(() => {
        uni.switchTab({ url: '/pages/home/home' })
      }, 1000)
    },
    showAgreement(type) {
      const titles = { user: '用户协议', privacy: '隐私政策' }
      uni.showModal({
        title: titles[type],
        content: '协议内容正在完善中，敬请期待...',
        showCancel: false
      })
    }
  }
}
</script>

<style>
.login-page {
  min-height: 100vh;
  background-color: #ffffff;
  padding: 0 48rpx;
  display: flex;
  flex-direction: column;
}

.logo-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 120rpx 0 80rpx;
}

.logo-icon {
  font-size: 120rpx;
}

.logo-text {
  font-size: 48rpx;
  font-weight: 600;
  color: #333333;
  margin-top: 20rpx;
}

.logo-slogan {
  font-size: 26rpx;
  color: #999999;
  margin-top: 12rpx;
}

.login-area {
  flex: 1;
}

.login-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 96rpx;
  border-radius: 48rpx;
  font-size: 30rpx;
  margin-bottom: 24rpx;
  background-color: #f5f5f5;
  color: #333333;
}

.login-btn.wechat {
  background-color: #07c160;
  color: #ffffff;
}

.login-btn.primary {
  background-color: #4CAF50;
  color: #ffffff;
}

.login-btn:disabled {
  opacity: 0.5;
}

.btn-icon {
  font-size: 36rpx;
  margin-right: 12rpx;
}

.btn-text {
  font-size: 30rpx;
}

.divider {
  display: flex;
  align-items: center;
  margin: 40rpx 0;
}

.divider-line {
  flex: 1;
  height: 1rpx;
  background-color: #e0e0e0;
}

.divider-text {
  padding: 0 24rpx;
  font-size: 26rpx;
  color: #999999;
}

.phone-login {
  margin-top: 20rpx;
}

.input-group {
  display: flex;
  align-items: center;
  height: 96rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 12rpx;
  padding: 0 24rpx;
  margin-bottom: 24rpx;
}

.input-prefix {
  font-size: 30rpx;
  color: #333333;
  margin-right: 16rpx;
  padding-right: 16rpx;
  border-right: 1rpx solid #e0e0e0;
}

.input-group input {
  flex: 1;
  font-size: 30rpx;
}

.code-btn {
  font-size: 26rpx;
  color: #4CAF50;
  background-color: transparent;
  padding: 0;
  margin: 0;
}

.code-btn:disabled {
  color: #999999;
}

.agreement {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40rpx 0 60rpx;
}

.agreement-text {
  font-size: 24rpx;
  color: #999999;
  margin-left: 8rpx;
}

.link {
  color: #4CAF50;
}
</style>
