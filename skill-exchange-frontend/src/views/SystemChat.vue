<template>
  <div class="system-chat-container">
    <Header />
    
    <div class="chat-content">
      <div class="chat-header">
        <div class="header-left">
          <el-icon class="system-icon" :size="24"><Bell /></el-icon>
          <span class="chat-title">系统消息</span>
        </div>
        <div class="header-right">
          <el-button text @click="refreshMessages">
            <el-icon><Refresh /></el-icon> 刷新
          </el-button>
        </div>
      </div>
      
      <div class="message-list" ref="messageList">
        <div v-if="messages.length === 0" class="empty-state">
          <el-icon :size="64" class="empty-icon"><Bell /></el-icon>
          <p>暂无系统消息</p>
          <p class="empty-hint">新注册用户会收到欢迎消息</p>
        </div>
        
        <div v-else class="message-items">
          <div 
            v-for="msg in messages" 
            :key="msg.id" 
            class="message-item"
            :class="{ 'is-read': msg.status === 1 }"
          >
            <div class="message-icon">
              <el-icon :size="32">
                <component :is="getMessageIcon(msg.type)" />
              </el-icon>
            </div>
            <div class="message-content">
              <div class="message-header">
                <span class="message-title">{{ getMessageTitle(msg.type) }}</span>
                <span class="message-time">{{ formatDate(msg.createTime) }}</span>
              </div>
              <p class="message-text">{{ msg.content }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import Header from '../components/Header.vue'
import { getSystemMessages } from '../api/systemMessage'
import { Bell, Refresh, Gift, MessageSquare, Info } from '@element-plus/icons-vue'

const messages = ref([])
const messageList = ref(null)

const loadMessages = async () => {
  try {
    const res = await getSystemMessages()
    messages.value = res.data || []
    await nextTick(() => {
      if (messageList.value) {
        messageList.value.scrollTop = 0
      }
    })
  } catch (err) {
    console.error(err)
  }
}

const refreshMessages = () => {
  loadMessages()
}

const getMessageIcon = (type) => {
  const icons = {
    1: Gift,    // 欢迎消息
    2: MessageSquare, // 反馈回复
    3: Info     // 系统通知
  }
  return icons[type] || Info
}

const getMessageTitle = (type) => {
  const titles = {
    1: '欢迎消息',
    2: '反馈回复',
    3: '系统通知'
  }
  return titles[type] || '系统消息'
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  loadMessages()
})
</script>

<style scoped>
.system-chat-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.chat-content {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  height: calc(100vh - 60px);
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #fff;
  border-bottom: 1px solid #eee;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.system-icon {
  color: #409eff;
}

.chat-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: #999;
}

.empty-icon {
  margin-bottom: 16px;
  color: #ccc;
}

.empty-hint {
  font-size: 13px;
  margin-top: 8px;
}

.message-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message-item {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: all 0.2s;
}

.message-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.message-item.is-read {
  opacity: 0.8;
}

.message-content {
  flex: 1;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.message-title {
  font-weight: 600;
  color: #333;
  font-size: 15px;
}

.message-time {
  font-size: 12px;
  color: #999;
}

.message-text {
  margin: 0;
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  word-break: break-all;
}
</style>
