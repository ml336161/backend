<template>
  <div class="chat-container">
    <Header />
    
    <div class="chat-content">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="chat-list-card">
            <template #header>
              <span>消息</span>
            </template>
            <div class="chat-list">
              <div v-for="friend in friends" :key="friend.friendUserId" 
                   class="chat-item"
                   :class="{ active: selectedUserId === friend.friendUserId }"
                   @click="selectChat(friend.friendUserId)">
                <el-avatar :src="friend.friendUser?.avatar">
                  {{ friend.friendUser?.nickname?.charAt(0) }}
                </el-avatar>
                <div class="chat-info">
                  <span class="name">{{ friend.friendUser?.nickname }}</span>
                  <span class="preview">{{ getLatestPreview(friend.friendUserId) }}</span>
                </div>
                <el-badge v-if="getUnreadCount(friend.friendUserId) > 0" 
                         :value="getUnreadCount(friend.friendUserId)" 
                         class="unread-badge" />
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="16">
          <el-card class="chat-area-card">
            <template #header v-if="selectedUserId">
              <span>{{ selectedUser?.nickname }}</span>
            </template>
            
            <div class="chat-messages" v-if="selectedUserId">
              <div v-for="msg in messages" :key="msg.id" 
                   :class="['message', msg.fromUserId === userStore.userId ? 'me' : 'other']">
                <el-avatar :src="getUserAvatar(msg.fromUserId)">
                  {{ getUserNickname(msg.fromUserId)?.charAt(0) }}
                </el-avatar>
                <div class="message-bubble">
                  <p>{{ msg.content }}</p>
                  <span class="time">{{ formatTime(msg.createTime) }}</span>
                </div>
              </div>
            </div>
            
            <div class="no-chat" v-else>
              <el-empty description="选择好友开始聊天" />
            </div>
            
            <div class="chat-input" v-if="selectedUserId">
              <el-input
                v-model="inputContent"
                type="textarea"
                :rows="3"
                placeholder="输入消息..."
                @keyup.enter.ctrl="handleSend"
              />
              <div class="input-actions">
                <el-button type="primary" @click="handleSend" :loading="sending">
                  发送
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '../store/user'
import Header from '../components/Header.vue'
import { getFriends } from '../api/friend'
import { getConversation, sendMessage, markAsRead } from '../api/chat'
import { formatTimeAgo } from '../utils/format'
import { ElMessage } from 'element-plus'

const route = useRoute()
const userStore = useUserStore()

const friends = ref([])
const selectedUserId = ref(null)
const selectedUser = ref(null)
const messages = ref([])
const inputContent = ref('')
const sending = ref(false)
const allMessages = ref({})

const loadFriends = async () => {
  try {
    const res = await getFriends()
    friends.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const selectChat = async (userId) => {
  selectedUserId.value = userId
  selectedUser.value = friends.value.find(f => f.friendUserId === userId)?.friendUser
  
  if (allMessages.value[userId]) {
    messages.value = allMessages.value[userId]
  } else {
    await loadMessages(userId)
  }
  
  await markAsRead(userId)
}

const loadMessages = async (userId) => {
  try {
    const res = await getConversation(userId)
    messages.value = res.data || []
    allMessages.value[userId] = messages.value
  } catch (err) {
    console.error(err)
  }
}

const handleSend = async () => {
  if (!inputContent.value.trim()) return
  
  sending.value = true
  try {
    await sendMessage({
      toUserId: selectedUserId.value,
      content: inputContent.value
    })
    inputContent.value = ''
    await loadMessages(selectedUserId.value)
  } catch (err) {
    console.error(err)
  } finally {
    sending.value = false
  }
}

const getUserAvatar = (userId) => {
  if (userId === userStore.userId) {
    return userStore.user?.avatar
  }
  const friend = friends.value.find(f => f.friendUserId === userId)
  return friend?.friendUser?.avatar
}

const getUserNickname = (userId) => {
  if (userId === userStore.userId) {
    return userStore.user?.nickname
  }
  const friend = friends.value.find(f => f.friendUserId === userId)
  return friend?.friendUser?.nickname
}

const getLatestPreview = (userId) => {
  const msgs = allMessages.value[userId] || []
  if (msgs.length === 0) return '暂无消息'
  const last = msgs[msgs.length - 1]
  return last.content.length > 20 ? last.content.slice(0, 20) + '...' : last.content
}

const getUnreadCount = (userId) => {
  const msgs = allMessages.value[userId] || []
  return msgs.filter(m => m.toUserId === userStore.userId && !m.isRead).length
}

const formatTime = (time) => {
  return formatTimeAgo(time)
}

onMounted(() => {
  loadFriends()
  if (route.params.userId) {
    setTimeout(() => selectChat(parseInt(route.params.userId)), 500)
  }
})
</script>

<style scoped>
.chat-content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.chat-list-card, .chat-area-card {
  height: calc(100vh - 120px);
}

.chat-list {
  height: 100%;
  overflow-y: auto;
}

.chat-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  cursor: pointer;
  border-radius: 4px;
  position: relative;
}

.chat-item:hover {
  background: #f5f7fa;
}

.chat-item.active {
  background: #e6f7ff;
}

.chat-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.chat-info .name {
  font-weight: bold;
  margin-bottom: 4px;
}

.chat-info .preview {
  font-size: 12px;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.unread-badge {
  position: absolute;
  right: 12px;
}

.chat-messages {
  height: 65%;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.message {
  display: flex;
  gap: 10px;
}

.message.me {
  flex-direction: row-reverse;
}

.message-bubble {
  max-width: 60%;
  background: #f5f7fa;
  padding: 10px 15px;
  border-radius: 8px;
}

.message.me .message-bubble {
  background: #409eff;
  color: #fff;
}

.message-bubble p {
  margin: 0 0 5px;
  word-break: break-word;
}

.message-bubble .time {
  font-size: 11px;
  opacity: 0.7;
}

.no-chat {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 70%;
}

.chat-input {
  padding: 20px;
  border-top: 1px solid #f0f0f0;
}

.input-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
}
</style>