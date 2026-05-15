<template>
  <div class="skill-detail-container">
    <Header />
    
    <div class="skill-detail" v-if="skill">
      <el-row :gutter="40">
        <el-col :span="16">
          <el-card>
            <div class="skill-images">
              <img v-if="skill.images" :src="'http://localhost:8080' + skill.images.split(',')[0]" />
              <div v-else class="no-image">
                <el-icon :size="100"><Picture /></el-icon>
              </div>
            </div>
            
            <h1 class="skill-title">{{ skill.title }}</h1>
            
            <div class="skill-meta">
              <el-tag>{{ skill.typeName }}</el-tag>
              <span class="price">{{ skill.price }} 时间币</span>
              <span>浏览 {{ skill.viewCount }}</span>
              <span>点赞 {{ skill.likeCount }}</span>
              <span>收藏 {{ skill.collectCount }}</span>
            </div>
            
            <div class="skill-description">
              <h3>技能描述</h3>
              <p>{{ skill.description }}</p>
            </div>
            
            <div class="skill-info-list">
              <div class="info-item">
                <el-icon><Clock /></el-icon>
                <span>服务时长：{{ skill.duration || '待定' }}</span>
              </div>
              <div class="info-item">
                <el-icon><Location /></el-icon>
                <span>服务地点：{{ skill.location || '待定' }}</span>
              </div>
            </div>
            
            <div class="action-buttons">
              <el-button type="primary" size="large" @click="handleApply" :disabled="!userStore.isLoggedIn">
                申请交换
              </el-button>
              <el-button @click="handleLike">
                <el-icon><Star /></el-icon>
                {{ skill.liked ? '已点赞' : '点赞' }}
              </el-button>
              <el-button @click="handleCollect">
                <el-icon><Collection /></el-icon>
                {{ skill.collected ? '已收藏' : '收藏' }}
              </el-button>
              <el-button @click="handleReport">
                <el-icon><Warning /></el-icon>
                举报
              </el-button>
            </div>
          </el-card>
          
          <el-card class="comments-section">
            <h3>评论</h3>
            <div class="comment-input" v-if="userStore.isLoggedIn">
              <el-input
                v-model="commentContent"
                type="textarea"
                :rows="3"
                placeholder="写下你的评论..."
              />
              <el-button type="primary" @click="submitComment">发表评论</el-button>
            </div>
            <div v-else class="login-tip">
              <el-link type="primary" @click="$router.push('/login')">登录</el-link>后发表评论
            </div>
            
            <div class="comments-list">
              <div v-for="comment in comments" :key="comment.id" class="comment-item">
                <div class="comment-avatar">
                  <el-avatar :size="40" :src="comment.user?.avatar">
                    {{ comment.user?.nickname?.charAt(0) }}
                  </el-avatar>
                </div>
                <div class="comment-content">
                  <div class="comment-header">
                    <span class="nickname">{{ comment.user?.nickname }}</span>
                    <span class="time">{{ formatTime(comment.createTime) }}</span>
                  </div>
                  <p>{{ comment.content }}</p>
                </div>
              </div>
              <el-empty v-if="comments.length === 0" description="暂无评论" />
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card class="user-card">
            <div class="user-info">
              <el-avatar :size="80" :src="skill.user?.avatar">
                {{ skill.user?.nickname?.charAt(0) }}
              </el-avatar>
              <h3>{{ skill.user?.nickname }}</h3>
              <p>{{ skill.user?.email }}</p>
            </div>
            <div class="user-stats">
              <div class="stat-item">
                <span class="value">{{ skill.user?.timeCoin || 0 }}</span>
                <span class="label">时间币</span>
              </div>
              <div class="stat-item">
                <span class="value">{{ skill.user?.creditScore || 0 }}</span>
                <span class="label">信用分</span>
              </div>
            </div>
            <el-button type="primary" @click="$router.push('/chat/' + skill.userId)" :disabled="!userStore.isLoggedIn || skill.userId === userStore.userId || !isFriend">
              私信
            </el-button>
            <div class="tooltip" v-if="userStore.isLoggedIn && skill.userId !== userStore.userId && !isFriend">
              请先添加好友才能私信
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <el-dialog v-model="applyDialogVisible" title="申请技能交换" width="500px">
      <el-form :model="applyForm" label-width="80px">
        <el-form-item label="预约时间">
          <el-date-picker
            v-model="appointmentDateTime"
            type="datetime"
            placeholder="选择预约时间"
            format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="applyForm.remark" type="textarea" :rows="3" placeholder="填写备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="applyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitApply">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import Header from '../components/Header.vue'
import { getSkillById, likeSkill, collectSkill } from '../api/skill'
import { getCommentsBySkill, createComment } from '../api/comment'
import { createExchange } from '../api/exchange'
import { checkFriend } from '../api/friend'
import { formatTimeAgo } from '../utils/format'
import { ElMessage } from 'element-plus'
import { Picture, Clock, Location, Star, Collection, Warning } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const skill = ref(null)
const comments = ref([])
const commentContent = ref('')
const applyDialogVisible = ref(false)
const appointmentDateTime = ref(null)  // 使用原生 Date 对象
const applyForm = ref({
  remark: ''
})
const isFriend = ref(false)

const loadSkill = async () => {
  try {
    const res = await getSkillById(route.params.id)
    skill.value = res.data
    
    if (userStore.isLoggedIn && skill.value.userId !== userStore.userId) {
      try {
        const friendRes = await checkFriend(skill.value.userId)
        isFriend.value = friendRes.data
      } catch (err) {
        console.error(err)
        isFriend.value = false
      }
    }
  } catch (err) {
    console.error(err)
  }
}

const loadComments = async () => {
  try {
    const res = await getCommentsBySkill(route.params.id)
    comments.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const formatTime = (time) => {
  return formatTimeAgo(time)
}

const handleLike = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  
  try {
    // 1. 先保存当前状态（点击前的状态）
    const isCurrentlyLiked = skill.value.liked;
    
    // 2. 发送请求（告诉后端切换状态）
    await likeSkill(route.params.id);
    
    // 3. 【关键修改】手动翻转前端状态，而不是等刷新
    // 如果之前是 true，现在变 false；之前是 false，现在变 true
    skill.value.liked = !isCurrentlyLiked;
    
    // 4. 根据翻转后的状态更新计数和提示
    if (skill.value.liked) {
      // 变成了“已点赞”
      skill.value.likeCount++; 
      ElMessage.success("点赞成功");
    } else {
      // 变成了“未点赞”（取消）
      skill.value.likeCount--;
      ElMessage.info("已取消点赞");
    }
    
    // 注意：这里不需要 await loadSkill()，因为我们已经手动更新了 UI
    // 如果你想确保数据绝对准确，可以在后面再调一次 loadSkill()，但通常不需要
    
  } catch (err) {
    console.error(err)
    ElMessage.error("操作失败")
  }
}

const handleCollect = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  try {
    // 1. 保存当前状态
    const isCurrentlyCollected = skill.value.collected;
    
    // 2. 发送请求
    await collectSkill(route.params.id);
    
    // 3. 【关键修改】手动翻转状态
    skill.value.collected = !isCurrentlyCollected;
    
    // 4. 更新 UI
    if (skill.value.collected) {
      skill.value.collectCount++;
      ElMessage.success("收藏成功");
    } else {
      skill.value.collectCount--;
      ElMessage.info("已取消收藏");
    }
    
  } catch (err) {
    console.error(err)
    ElMessage.error("操作失败")
  }
}
const handleApply = () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  if (skill.value.userId === userStore.userId) {
    ElMessage.warning('不能申请自己的技能')
    return
  }
  applyDialogVisible.value = true
}

const submitApply = async () => {
  if (!appointmentDateTime.value) {
    ElMessage.warning('请选择预约时间')
    return
  }
  
  // 手动格式化日期为 yyyy-MM-dd HH:mm:ss
  const date = new Date(appointmentDateTime.value)
  const formattedDate = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
  
  try {
    await createExchange({
      skillId: skill.value.id,
      appointmentTime: formattedDate,
      remark: applyForm.value.remark
    })
    ElMessage.success('申请已提交')
    await userStore.refreshUser()
    applyDialogVisible.value = false
    appointmentDateTime.value = null  // 重置日期选择
  } catch (err) {
    console.error(err)
    ElMessage.error(err.message || '申请失败')
  }
}

const submitComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('评论内容不能为空')
    return
  }
  try {
    await createComment({
      skillId: skill.value.id,
      content: commentContent.value
    })
    commentContent.value = ''
    await loadComments()
    ElMessage.success('评论成功')
  } catch (err) {
    console.error(err)
  }
}

const handleReport = () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  router.push({ path: '/report', query: { type: 'skill', id: skill.value.id } })
}

onMounted(() => {
  loadSkill()
  loadComments()
})
</script>

<style scoped>
.skill-detail {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.skill-images {
  height: 400px;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  margin-bottom: 20px;
}

.skill-images img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.no-image {
  color: #ccc;
}

.skill-title {
  font-size: 24px;
  margin-bottom: 16px;
}

.skill-meta {
  display: flex;
  gap: 16px;
  align-items: center;
  margin-bottom: 20px;
}

.price {
  color: #ff6b00;
  font-size: 20px;
  font-weight: bold;
}

.skill-description {
  margin-bottom: 20px;
}

.skill-description h3 {
  margin-bottom: 10px;
}

.skill-description p {
  line-height: 1.8;
  color: #666;
}

.skill-info-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.comments-section {
  margin-top: 20px;
}

.comment-input {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.login-tip {
  text-align: center;
  padding: 20px;
  color: #999;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  display: flex;
  gap: 12px;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.nickname {
  font-weight: bold;
  color: #333;
}

.time {
  color: #999;
  font-size: 13px;
}

.user-card {
  text-align: center;
}

.user-info h3 {
  margin-top: 12px;
  margin-bottom: 8px;
}

.user-info p {
  color: #999;
  font-size: 14px;
}

.user-stats {
  display: flex;
  justify-content: center;
  gap: 40px;
  margin: 20px 0;
}

.stat-item {
  text-align: center;
}

.stat-item .value {
  display: block;
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}

.stat-item .label {
  color: #999;
  font-size: 13px;
}
</style>