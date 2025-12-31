import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/video/:id',
    name: 'VideoPlayer',
    component: () => import('../views/VideoPlayer.vue')
  },
  {
    path: '/study',
    name: 'Study',
    component: () => import('../views/Study.vue')
  },
  {
    path: '/library',
    name: 'Library',
    component: () => import('../views/Library.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/Profile.vue')
  },
  {
    path: '/notes',
    name: 'Notes',
    component: () => import('../views/Notes.vue')
  },
  {
    path: '/favorites',
    name: 'Favorites',
    component: () => import('../views/Favorites.vue')
  },
  {
    path: '/words',
    name: 'Words',
    component: () => import('../views/Words.vue')
  },
  {
    path: '/report',
    name: 'Report',
    component: () => import('../views/Report.vue')
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('../views/Search.vue')
  },
  {
    path: '/import',
    name: 'Import',
    component: () => import('../views/Import.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
