import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/index.vue'),
    meta: { title: '商户登录' }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/index.vue'),
        meta: { title: '工作台', icon: 'HomeFilled' }
      },
      {
        path: 'video',
        name: 'Video',
        component: () => import('../views/video/index.vue'),
        meta: { title: '视频管理', icon: 'VideoCamera' }
      },
      {
        path: 'income',
        name: 'Income',
        component: () => import('../views/income/index.vue'),
        meta: { title: '收益管理', icon: 'Money' }
      },
      {
        path: 'withdraw',
        name: 'Withdraw',
        component: () => import('../views/withdraw/index.vue'),
        meta: { title: '提现管理', icon: 'Wallet' }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('../views/settings/index.vue'),
        meta: { title: '账户设置', icon: 'Setting' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = `${to.meta.title || '商户中心'} - 鹦学伴`
  const token = localStorage.getItem('merchant_token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
