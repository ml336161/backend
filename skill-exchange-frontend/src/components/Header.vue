<template>
  <el-header class="header">
    <div class="header-content">
      <div class="logo" @click="$router.push('/home')">
        <el-icon><Trophy /></el-icon>
        <span>技能互助</span>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        mode="horizontal"
        :ellipsis="false"
        class="nav-menu"
        router
      >
        <el-menu-item index="/home">首页</el-menu-item>
        <el-menu-item index="/skills">技能大厅</el-menu-item>
        <el-menu-item v-if="userStore.isLoggedIn" index="/publish">发布技能</el-menu-item>
        <el-menu-item v-if="userStore.isLoggedIn" index="/exchanges">技能交换</el-menu-item>
        <el-menu-item v-if="userStore.isLoggedIn && userStore.isAdmin" index="/admin">管理后台</el-menu-item>
      </el-menu>
      
      <div class="user-actions">
        <template v-if="userStore.isLoggedIn">
          <el-badge v-if="userStore.unreadCount > 0" :value="userStore.unreadCount" class="message-badge">
           <el-button link @click="$router.push('/chat')">
              <el-icon><ChatDotRound /></el-icon>
            </el-button>
          </el-badge>
          <el-button v-else link @click="$router.push('/chat')">
            <el-icon><ChatDotRound /></el-icon>
          </el-button>
          
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <el-avatar :size="32" :src="userStore.user?.avatar">
                {{ userStore.user?.nickname?.charAt(0) }}
              </el-avatar>
              <span class="username">{{ userStore.user?.nickname }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="friends">
                  <el-icon><UserFilled /></el-icon>
                  好友
                </el-dropdown-item>
                <el-dropdown-item command="coin">
                  <el-icon><Coin /></el-icon>
                  时间币
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        
        <template v-else>
          <el-button type="primary" @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </template>
      </div>
    </div>
  </el-header>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../store/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'friends':
      router.push('/friends')
      break
    case 'coin':
      router.push('/coin')
      break
    case 'logout':
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        userStore.logout()
        ElMessage.success('已退出登录')
        router.push('/home')
      }).catch(() => {})
      break
  }
}
</script>

<style scoped>
.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
  height: 60px;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
  cursor: pointer;
}

.nav-menu {
  border: none;
  flex: 1;
  margin-left: 40px;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.username {
  font-size: 14px;
}

.message-badge {
  margin-right: 8px;
}
</style>