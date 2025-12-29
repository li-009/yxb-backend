<template>
  <view>
    <slot />
  </view>
</template>

<script>
export default {
  onLaunch() {
    console.log('鹦学伴启动')
    // 检查登录状态
    const token = uni.getStorageSync('token')
    if (token) {
      console.log('用户已登录')
    }
  },
  onShow() {},
  onHide() {}
}
</script>

<style>
/* 全局样式 */
page {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  font-size: 28rpx;
  color: #333333;
  background-color: #f5f5f5;
}

view, text {
  box-sizing: border-box;
}

button {
  margin: 0;
  padding: 0;
  border: none;
  outline: none;
  background-color: transparent;
}

button::after {
  border: none;
}

/* 安全区域适配 */
.safe-area-bottom {
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}

/* 通用类 */
.flex {
  display: flex;
}

.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.flex-between {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.text-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.text-primary {
  color: #4CAF50;
}

.text-secondary {
  color: #999999;
}

.bg-primary {
  background-color: #4CAF50;
}
</style>
