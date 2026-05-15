<template>
  <div class="home-container">
    <Header />
    
    <div class="banner">
      <div class="banner-content">
        <h1>技能互助平台</h1>
        <p>交换技能，分享知识，共同成长</p>
        <div class="banner-buttons">
          <el-button type="primary" size="large" @click="$router.push('/skills')">
            浏览技能
          </el-button>
          <el-button size="large" v-if="userStore.isLoggedIn" @click="$router.push('/publish')">
            发布技能
          </el-button>
        </div>
      </div>
    </div>
    
    <div class="features">
      <div class="feature-item">
        <el-icon :size="40" color="#409eff"><MagicStick /></el-icon>
        <h3>技能交换</h3>
        <p>用你的技能换取他人的技能</p>
      </div>
      <div class="feature-item">
        <el-icon :size="40" color="#67c23a"><Coin /></el-icon>
        <h3>时间币系统</h3>
        <p>用时间币进行技能交易</p>
      </div>
      <div class="feature-item">
        <el-icon :size="40" color="#e6a23c"><UserFilled /></el-icon>
        <h3>好友互动</h3>
        <p>添加好友，随时交流</p>
      </div>
    </div>
    
    <div class="hot-skills">
      <div class="section-header">
        <h2 class="section-title">热门技能</h2>
        <span class="section-more" @click="$router.push('/skills')">查看更多</span>
      </div>
      <div class="skill-scroll">
        <div class="skill-card-wrapper">
          <el-card 
            class="skill-card" 
            shadow="hover" 
            v-for="skill in hotSkills" 
            :key="skill.id"
            @click="$router.push('/skill/' + skill.id)"
          >
            <div class="skill-cover">
              <img v-if="skill.images" :src="'http://localhost:8080' + skill.images.split(',')[0]" />
              <el-icon v-else><Picture /></el-icon>
            </div>
            <div class="skill-info">
              <h3>{{ skill.title }}</h3>
              <p class="skill-desc">{{ skill.description }}</p>
              <div class="skill-footer">
                <span class="price">{{ skill.price }} 时间币</span>
                <div class="skill-stats">
                  <span><el-icon :size="14"><Eye /></el-icon>{{ skill.viewCount }}</span>
                  <span><el-icon :size="14"><Heart /></el-icon>{{ skill.likeCount }}</span>
                </div>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </div>
    
    <div class="latest-skills">
      <div class="section-header">
        <h2 class="section-title">最新发布</h2>
        <span class="section-more" @click="$router.push('/skills')">查看更多</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6" v-for="skill in latestSkills" :key="skill.id" @click="$router.push('/skill/' + skill.id)">
          <el-card class="skill-card-small" shadow="hover">
            <div class="skill-cover-small">
              <img v-if="skill.images" :src="'http://localhost:8080' + skill.images.split(',')[0]" />
              <el-icon v-else><Picture /></el-icon>
            </div>
            <div class="skill-info-small">
              <h4>{{ skill.title }}</h4>
              <p>{{ skill.price }} 时间币</p>
              <span class="publish-time">{{ formatTime(skill.createTime) }}发布</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../store/user'
import Header from '../components/Header.vue'
import { getHotSkills, getLatestSkills } from '../api/skill'

const userStore = useUserStore()
const hotSkills = ref([])
const latestSkills = ref([])

const loadHotSkills = async () => {
  try {
    const res = await getHotSkills({ limit: 6 })
    hotSkills.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const loadLatestSkills = async () => {
  try {
    const res = await getLatestSkills({ limit: 8 })
    latestSkills.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  if (days === 0) return '今天'
  if (days === 1) return '昨天'
  if (days < 7) return `${days}天前`
  return `${date.getMonth() + 1}月${date.getDate()}日`
}

onMounted(() => {
  loadHotSkills()
  loadLatestSkills()
})
</script>

<style scoped>
.banner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 100px 0;
  text-align: center;
  color: #fff;
}

.banner-content h1 {
  font-size: 48px;
  margin-bottom: 20px;
}

.banner-content p {
  font-size: 20px;
  margin-bottom: 40px;
  opacity: 0.9;
}

.banner-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.features {
  max-width: 1000px;
  margin: 60px auto;
  display: flex;
  justify-content: space-around;
}

.feature-item {
  text-align: center;
  width: 250px;
}

.feature-item h3 {
  margin: 16px 0 8px;
  color: #333;
}

.feature-item p {
  color: #666;
}

.hot-skills, .latest-skills {
  max-width: 1200px;
  margin: 60px auto;
  padding: 0 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.section-more {
  color: #409eff;
  cursor: pointer;
  font-size: 14px;
}

.section-more:hover {
  text-decoration: underline;
}

.skill-scroll {
  overflow: hidden;
  position: relative;
}

.skill-card-wrapper {
  display: flex;
  gap: 20px;
  overflow-x: auto;
  padding-bottom: 10px;
}

.skill-card-wrapper::-webkit-scrollbar {
  height: 6px;
}

.skill-card-wrapper::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.skill-card-wrapper::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}

.skill-card-wrapper::-webkit-scrollbar-thumb:hover {
  background: #999;
}

.skill-card {
  flex-shrink: 0;
  width: 280px;
  cursor: pointer;
}

.skill-card-small {
  cursor: pointer;
  margin-bottom: 20px;
}

.skill-cover {
  height: 180px;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.skill-cover-small {
  height: 120px;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.skill-cover img, .skill-cover-small img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.skill-info {
  padding: 16px;
}

.skill-info-small {
  padding: 12px;
}

.skill-info h3 {
  font-size: 18px;
  margin-bottom: 8px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.skill-info-small h4 {
  font-size: 14px;
  margin-bottom: 8px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.skill-desc {
  font-size: 13px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 12px;
}

.skill-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  color: #409eff;
  font-weight: bold;
  font-size: 16px;
}

.skill-stats {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #999;
}

.skill-info-small p {
  color: #ff6b00;
  font-weight: bold;
  margin: 0 0 8px 0;
}

.publish-time {
  font-size: 12px;
  color: #999;
}
</style>