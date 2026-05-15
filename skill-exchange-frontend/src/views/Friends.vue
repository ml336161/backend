<template>
  <div class="friends-container">
    <Header />
    
    <div class="friends-content">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card>
            <template #header>
              <span>好友申请</span>
            </template>
            <div class="apply-list">
              <div v-for="apply in receivedApplies" :key="apply.id" class="apply-item">
                <div class="apply-info">
                  <el-avatar :src="apply.fromUser?.avatar">
                    {{ apply.fromUser?.nickname?.charAt(0) }}
                  </el-avatar>
                  <div class="apply-detail">
                    <span class="name">{{ apply.fromUser?.nickname }}</span>
                    <span class="msg">{{ apply.message || '想加你为好友' }}</span>
                  </div>
                </div>
                <div class="apply-actions" v-if="apply.status === 'pending'">
                  <el-button type="primary" size="small" @click="handleApply(apply.id, 'accept')">
                    接受
                  </el-button>
                  <el-button size="small" @click="handleApply(apply.id, 'reject')">
                    拒绝
                  </el-button>
                </div>
                <el-tag v-else size="small" :type="apply.status === 'accepted' ? 'success' : 'info'">
                  {{ apply.status === 'accepted' ? '已接受' : '已拒绝' }}
                </el-tag>
              </div>
              <el-empty v-if="receivedApplies.length === 0" description="暂无好友申请" />
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="16">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>我的好友</span>
                <el-button type="primary" @click="addDialogVisible = true">
                  <el-icon><Plus /></el-icon>
                  添加好友
                </el-button>
              </div>
            </template>
            <div class="friends-list">
              <div v-for="friend in friends" :key="friend.friendUserId" class="friend-item">
                <div class="friend-info" @click="$router.push('/chat/' + friend.friendUserId)">
                  <el-avatar :size="50" :src="friend.friendUser?.avatar">
                    {{ friend.friendUser?.nickname?.charAt(0) }}
                  </el-avatar>
                  <div class="friend-detail">
                    <span class="name">{{ friend.friendUser?.nickname }}</span>
                    <span class="time">{{ formatTime(friend.createTime) }}</span>
                  </div>
                </div>
                <el-button type="danger" size="small" @click="handleDelete(friend.friendUserId)">
                  删除
                </el-button>
              </div>
              <el-empty v-if="friends.length === 0" description="暂无好友，快去添加吧" />
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <el-dialog v-model="addDialogVisible" title="添加好友" width="500px">
      <el-input v-model="searchUsername" placeholder="请输入用户名" clearable />
      <div class="search-result" v-if="searchResult">
        <el-avatar :src="searchResult.avatar">
          {{ searchResult.nickname?.charAt(0) }}
        </el-avatar>
        <div class="result-info">
          <span class="name">{{ searchResult.nickname }}</span>
          <span class="username">@{{ searchResult.username }}</span>
        </div>
        <el-button type="primary" @click="handleAddFriend">
          添加好友
        </el-button>
      </div>
      <el-button @click="handleSearch" style="width: 100%; margin-top: 10px">
        搜索
      </el-button>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '../components/Header.vue'
import { getReceivedApplies, getFriends, applyFriend, handleApply as handleApplyApi, deleteFriend } from '../api/friend'
import { getUserByUsername } from '../api/user'
import { formatDate } from '../utils/format'
import { ElMessage, ElMessageBox } from 'element-plus'

const receivedApplies = ref([])
const friends = ref([])
const addDialogVisible = ref(false)
const searchUsername = ref('')
const searchResult = ref(null)

const loadApplies = async () => {
  try {
    const res = await getReceivedApplies()
    receivedApplies.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const loadFriends = async () => {
  try {
    const res = await getFriends()
    friends.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const formatTime = (time) => {
  return formatDate(time)
}

const handleDelete = async (userId) => {
  try {
    await ElMessageBox.confirm('确定要删除该好友吗？', '提示')
    await deleteFriend(userId)
    ElMessage.success('已删除好友')
    await loadFriends()
  } catch (err) {
    console.error(err)
  }
}

const handleSearch = async () => {
  if (!searchUsername.value.trim()) {
    ElMessage.warning('请输入用户名')
    return
  }
  try {
    const res = await getUserByUsername(searchUsername.value)
    searchResult.value = res.data
  } catch (err) {
    ElMessage.error('用户不存在')
    searchResult.value = null
  }
}

const handleApply = async (applyId, action) => {
  try {
    await handleApplyApi({ id: applyId, action })
    ElMessage.success(action === 'accept' ? '已接受' : '已拒绝')
    await loadApplies()
    if (action === 'accept') {
      await loadFriends()
    }
  } catch (err) {
    console.error(err)
  }
}

const handleAddFriend = async () => {
  try {
    await applyFriend({ toUserId: searchResult.value.id, message: '想认识你' })
    ElMessage.success('已发送好友申请')
    addDialogVisible.value = false
    searchResult.value = null
    searchUsername.value = ''
  } catch (err) {
    console.error(err)
  }
}

onMounted(() => {
  loadApplies()
  loadFriends()
})
</script>

<style scoped>
.friends-content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.apply-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.apply-info {
  display: flex;
  gap: 12px;
  align-items: center;
}

.apply-detail {
  display: flex;
  flex-direction: column;
}

.apply-detail .name {
  font-weight: bold;
  margin-bottom: 4px;
}

.apply-detail .msg {
  font-size: 12px;
  color: #999;
}

.friend-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.friend-info {
  display: flex;
  gap: 12px;
  align-items: center;
  cursor: pointer;
}

.friend-detail {
  display: flex;
  flex-direction: column;
}

.friend-detail .name {
  font-weight: bold;
  margin-bottom: 4px;
}

.friend-detail .time {
  font-size: 12px;
  color: #999;
}

.search-result {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-top: 15px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.result-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.result-info .name {
  font-weight: bold;
  margin-bottom: 4px;
}

.result-info .username {
  color: #999;
  font-size: 13px;
}
</style>