<template>
  <div class="friend-profile-container">
    <Header />
    
    <div class="friend-profile-content">
      <el-row :gutter="20">
        <el-col :span="16">
          <el-card>
            <template #header>
              <span>好友资料</span>
            </template>
            
            <div class="user-info" v-if="profileUser">
              <el-avatar :size="100" :src="profileUser.avatar">
                {{ profileUser.nickname?.charAt(0) }}
              </el-avatar>
              <div class="info-detail">
                <h2>{{ profileUser.nickname }}</h2>
                <p>用户名：{{ profileUser.username }}</p>
                <p>邮箱：{{ profileUser.email || '未设置' }}</p>
                <p>出生日期：{{ profileUser.birthday ? formatDate(profileUser.birthday) : '未设置' }}
                  <span v-if="calculateAge(profileUser.birthday) > 0">（{{ calculateAge(profileUser.birthday) }}岁）</span>
                </p>
                <div class="user-stats">
                  <div class="stat-item">
                    <span class="value">{{ profileUser.timeCoin }}</span>
                    <span class="label">时间币</span>
                  </div>
                  <div class="stat-item">
                    <span class="value">{{ profileUser.creditScore }}</span>
                    <span class="label">信用分</span>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
          
          <el-card class="my-skills">
            <template #header>
              <span>发布的技能</span>
            </template>
            <el-row :gutter="20">
              <el-col :span="8" v-for="skill in skills" :key="skill.id">
                <el-card shadow="hover" @click="$router.push('/skill/' + skill.id)">
                  <div class="skill-cover">
                    <img v-if="skill.images" :src="'http://localhost:8080' + skill.images.split(',')[0]" />
                    <div v-else class="no-image"><el-icon :size="40"><Picture /></el-icon></div>
                  </div>
                  <h4>{{ skill.title }}</h4>
                  <p class="price">{{ skill.price }} 时间币</p>
                </el-card>
              </el-col>
            </el-row>
            <el-empty v-if="skills.length === 0" description="暂无技能" />
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card class="quick-stats">
            <template #header>
              <span>数据统计</span>
            </template>
            <div class="stats-grid">
              <div class="stat-card" @click="showLikes = true">
                <div class="stat-icon"><el-icon :size="24" color="#f56c6c"><Heart /></el-icon></div>
                <div class="stat-info">
                  <span class="stat-value">{{ userLikes.length }}</span>
                  <span class="stat-label">点赞技能</span>
                </div>
              </div>
              <div class="stat-card" @click="showCollects = true">
                <div class="stat-icon"><el-icon :size="24" color="#409eff"><Star /></el-icon></div>
                <div class="stat-info">
                  <span class="stat-value">{{ userCollects.length }}</span>
                  <span class="stat-label">收藏技能</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <el-dialog v-model="showLikes" title="点赞的技能" width="800px">
      <div class="likes-list">
        <el-row :gutter="20">
          <el-col :span="6" v-for="skill in userLikes" :key="skill.id">
            <el-card shadow="hover" @click="$router.push('/skill/' + skill.id)">
              <div class="skill-cover">
                <img v-if="skill.images" :src="'http://localhost:8080' + skill.images.split(',')[0]" />
                <div v-else class="no-image"><el-icon :size="40"><Picture /></el-icon></div>
              </div>
              <h4>{{ skill.title }}</h4>
              <p class="price">{{ skill.price }} 时间币</p>
            </el-card>
          </el-col>
        </el-row>
        <el-empty v-if="userLikes.length === 0" description="暂无点赞的技能" />
      </div>
    </el-dialog>
    
    <el-dialog v-model="showCollects" title="收藏的技能" width="800px">
      <div class="collects-list">
        <el-row :gutter="20">
          <el-col :span="6" v-for="skill in userCollects" :key="skill.id">
            <el-card shadow="hover" @click="$router.push('/skill/' + skill.id)">
              <div class="skill-cover">
                <img v-if="skill.images" :src="'http://localhost:8080' + skill.images.split(',')[0]" />
                <div v-else class="no-image"><el-icon :size="40"><Picture /></el-icon></div>
              </div>
              <h4>{{ skill.title }}</h4>
              <p class="price">{{ skill.price }} 时间币</p>
            </el-card>
          </el-col>
        </el-row>
        <el-empty v-if="userCollects.length === 0" description="暂无收藏的技能" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import Header from '../components/Header.vue'
import { getUserById } from '../api/user'
import { getSkillByUser } from '../api/skill'
import { getUserLikesByUserId, getUserCollectsByUserId } from '../api/skill'
import { Picture, Heart, Star } from '@element-plus/icons-vue'

const route = useRoute()

const profileUser = ref(null)
const skills = ref([])
const userLikes = ref([])
const userCollects = ref([])
const showLikes = ref(false)
const showCollects = ref(false)

const loadProfile = async () => {
  try {
    const userId = route.params.userId
    if (!userId) return
    const res = await getUserById(userId)
    profileUser.value = res.data
  } catch (err) {
    console.error(err)
  }
}

const loadSkills = async () => {
  try {
    const userId = route.params.userId
    if (!userId) return
    const res = await getSkillByUser(userId)
    skills.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const loadLikes = async () => {
  try {
    const userId = route.params.userId
    if (!userId) return
    const res = await getUserLikesByUserId(userId)
    userLikes.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const loadCollects = async () => {
  try {
    const userId = route.params.userId
    if (!userId) return
    const res = await getUserCollectsByUserId(userId)
    userCollects.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const calculateAge = (birthday) => {
  if (!birthday) return 0
  const birthDate = new Date(birthday)
  const today = new Date()
  let age = today.getFullYear() - birthDate.getFullYear()
  const monthDiff = today.getMonth() - birthDate.getMonth()
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
    age--
  }
  return age
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
}

watch(() => route.params.userId, () => {
  loadProfile()
  loadSkills()
  loadLikes()
  loadCollects()
})

onMounted(() => {
  loadProfile()
  loadSkills()
  loadLikes()
  loadCollects()
})
</script>

<style scoped>
.friend-profile-content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  display: flex;
  gap: 30px;
  align-items: center;
}

.info-detail h2 {
  margin-bottom: 10px;
}

.info-detail p {
  color: #666;
  margin: 5px 0;
}

.user-stats {
  display: flex;
  gap: 40px;
  margin-top: 15px;
}

.stat-item {
  text-align: center;
}

.stat-item .value {
  display: block;
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
}

.stat-item .label {
  color: #999;
  font-size: 14px;
}

.my-skills {
  margin-top: 20px;
}

.skill-cover {
  height: 120px;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
}

.skill-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-image {
  color: #ccc;
}

.price {
  color: #ff6b00;
  font-weight: bold;
}

.quick-stats {
  margin-bottom: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s;
}

.stat-card:hover {
  background: #e9ecef;
}

.stat-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  border-radius: 8px;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 12px;
  color: #999;
}

.likes-list,
.collects-list {
  max-height: 500px;
  overflow-y: auto;
}
</style>