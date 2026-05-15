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
    
    <div class="recent-skills">
      <h2 class="section-title">热门技能</h2>
      <el-row :gutter="20">
        <el-col :span="6" v-for="skill in skills" :key="skill.id" @click="$router.push('/skill/' + skill.id)">
          <el-card class="skill-card" shadow="hover">
            <div class="skill-cover">
              <img v-if="skill.images" :src="'http://localhost:8080' + skill.images.split(',')[0]" />
              <el-icon v-else><Picture /></el-icon>
            </div>
            <div class="skill-info">
              <h3>{{ skill.title }}</h3>
              <p>{{ skill.description }}</p>
              <div class="skill-footer">
                <span class="price">{{ skill.price }} 时间币</span>
                <span class="views">{{ skill.viewCount }} 浏览</span>
              </div>
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
import { getSkillList } from '../api/skill'

const userStore = useUserStore()
const skills = ref([])

const loadSkills = async () => {
  try {
    const res = await getSkillList({ pageNum: 1, pageSize: 8 })
    skills.value = res.data.list || []
  } catch (err) {
    console.error(err)
  }
}

onMounted(() => {
  loadSkills()
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

.recent-skills {
  max-width: 1200px;
  margin: 60px auto;
  padding: 0 20px;
}

.section-title {
  text-align: center;
  margin-bottom: 40px;
  color: #333;
}

.skill-card {
  cursor: pointer;
  margin-bottom: 20px;
}

.skill-cover {
  height: 150px;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.skill-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.skill-info {
  padding: 12px 0;
}

.skill-info h3 {
  font-size: 16px;
  margin-bottom: 8px;
  color: #333;
}

.skill-info p {
  font-size: 13px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 8px;
}

.skill-footer {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

.price {
  color: #409eff;
  font-weight: bold;
}
</style>