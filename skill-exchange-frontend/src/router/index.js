import { createRouter, createWebHistory } from 'vue-router'
import { isLoggedIn } from '../utils/auth'

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/skills',
    name: 'Skills',
    component: () => import('../views/Skills.vue'),
    meta: { title: '技能大厅' }
  },
  {
    path: '/skill/:id',
    name: 'SkillDetail',
    component: () => import('../views/SkillDetail.vue'),
    meta: { title: '技能详情' }
  },
  {
    path: '/publish',
    name: 'Publish',
    component: () => import('../views/Publish.vue'),
    meta: { title: '发布技能', requiresAuth: true }
  },
  {
    path: '/profile/:userId?',
    name: 'Profile',
    component: () => import('../views/Profile.vue'),
    meta: { title: '个人中心' }
  },
  {
    path: '/profile/collects',
    name: 'Collects',
    component: () => import('../views/Collects.vue'),
    meta: { title: '我的收藏', requiresAuth: true }
  },
  {
    path: '/profile/likes',
    name: 'Likes',
    component: () => import('../views/Likes.vue'),
    meta: { title: '我的点赞', requiresAuth: true }
  },
  {
    path: '/friends',
    name: 'Friends',
    component: () => import('../views/Friends.vue'),
    meta: { title: '好友', requiresAuth: true }
  },
  {
    path: '/chat/:userId?',
    name: 'Chat',
    component: () => import('../views/Chat.vue'),
    meta: { title: '私信', requiresAuth: true }
  },
  {
    path: '/coin',
    name: 'Coin',
    component: () => import('../views/Coin.vue'),
    meta: { title: '时间币', requiresAuth: true }
  },
  {
    path: '/exchanges',
    name: 'Exchanges',
    component: () => import('../views/Exchanges.vue'),
    meta: { title: '技能交换', requiresAuth: true }
  },
  {
    path: '/report',
    name: 'Report',
    component: () => import('../views/Report.vue'),
    meta: { title: '举报', requiresAuth: true }
  },
  {
    path: '/feedback',
    name: 'Feedback',
    component: () => import('../views/Feedback.vue'),
    meta: { title: '反馈', requiresAuth: true }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/Admin.vue'),
    meta: { title: '管理后台', requiresAuth: true, requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? to.meta.title + ' - 技能互助' : '技能互助'
  
  if (to.meta.requiresAuth && !isLoggedIn()) {
    next('/login')
  } else if (to.meta.requiresAdmin) {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    if (user.role !== 'admin') {
      next('/home')
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router