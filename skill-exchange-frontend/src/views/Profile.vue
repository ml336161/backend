<template>
  <div class="profile-container">
    <Header />
    
    <div class="profile-content">
      <el-row :gutter="20">
        <el-col :span="16">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>基本信息</span>
                <el-button link @click="editDialogVisible = true" v-if="isOwnProfile">
                  编辑资料
                </el-button>
              </div>
            </template>
            
            <div class="user-info" v-if="profileUser">
              <el-avatar :size="100" :src="profileUser.avatar">
                {{ profileUser.nickname?.charAt(0) }}
              </el-avatar>
              <div class="info-detail">
                <h2>{{ profileUser.nickname }}</h2>
                <p>用户名：{{ profileUser.username }}</p>
                <p>邮箱：{{ profileUser.email || '未设置' }}</p>
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
          
          <el-card class="credit-radar" v-if="isOwnProfile">
            <template #header>
              <span>信用雷达图</span>
            </template>
            <div ref="radarChart" style="width: 100%; height: 400px;"></div>
          </el-card>
          
          <el-card class="my-skills">
            <template #header>
              <span>{{ isOwnProfile ? '我的技能' : '发布的技能' }}</span>
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
          <el-card v-if="isOwnProfile">
            <template #header>
              <span>快速操作</span>
            </template>
            <div class="quick-actions">
              <el-button @click="$router.push('/coin')">时间币明细</el-button>
              <el-button @click="$router.push('/exchanges')">技能交换</el-button>
              <el-button @click="$router.push('/friends')">我的好友</el-button>
              <el-button @click="$router.push('/chat')">私信</el-button>
              <el-button @click="$router.push('/feedback')">意见反馈</el-button>
            </div>
          </el-card>
          
          <el-card v-if="isOwnProfile">
            <template #header>
              <span>每日签到</span>
            </template>
            <div class="sign-in">
              <p>连续签到 {{ consecutiveDays }} 天</p>
              <el-button 
                type="primary" 
                size="large" 
                @click="handleSignIn" 
                :loading="signing"
                :disabled="todaySigned || signing"
              >
                {{ todaySigned ? '今日已签到' : '签到领币' }}
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <el-dialog v-model="editDialogVisible" title="编辑资料" width="500px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="昵称">
          <el-input v-model="editForm.nickname" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email" />
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="editForm.phone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import Header from '../components/Header.vue'
import { getUserById, updateUser, getCreditRadar } from '../api/user'
import { getSkillByUser } from '../api/skill'
import { signIn, checkSigned, getConsecutiveDays } from '../api/coin'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const profileUser = ref(null)
const skills = ref([])
const radarChart = ref(null)
const editDialogVisible = ref(false)
const signing = ref(false)
const todaySigned = ref(false)
const consecutiveDays = ref(0)

const editForm = reactive({
  nickname: '',
  email: '',
  phone: ''
})

const isOwnProfile = computed(() => {
  const userId = route.params.userId
  return !userId || parseInt(userId) === userStore.userId
})

const loadProfile = async () => {
  try {
    const userId = route.params.userId || userStore.userId
    const res = await getUserById(userId)
    profileUser.value = res.data
    
    // 更新store中的用户信息
    if (isOwnProfile.value) {
      userStore.setUser(res.data)
    }
  } catch (err) {
    console.error(err)
  }
}

const loadSkills = async () => {
  try {
    const userId = route.params.userId || userStore.userId
    const res = await getSkillByUser(userId)
    skills.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const loadCreditRadar = async () => {
  if (!isOwnProfile.value) return
  try {
    const res = await getCreditRadar()
    const data = res.data
    const chart = echarts.init(radarChart.value)
    chart.setOption({
      radar: {
        indicator: [
          { name: '技能数', max: 20 },
          { name: '交换数', max: 20 },
          { name: '好评数', max: 20 },
          { name: '登录天数', max: 30 },
          { name: '时间币', max: 100 }
        ]
      },
      series: [{
        type: 'radar',
        data: [{
          value: [
            data.skillCount || 0,
            data.exchangeCount || 0,
            data.goodCommentCount || 0,
            data.loginDays || 0,
            data.timeCoinBalance || 0
          ],
          name: '信用评分'
        }]
      }]
    })
  } catch (err) {
    console.error(err)
  }
}

const loadSignStatus = async () => {
  if (!isOwnProfile.value) return
  try {
    const [res1, res2] = await Promise.all([checkSigned(), getConsecutiveDays()])
    todaySigned.value = res1.data
    consecutiveDays.value = res2.data
  } catch (err) {
    console.error(err)
    todaySigned.value = false
    consecutiveDays.value = 0
  }
}

const handleSignIn = async () => {
  // 防止重复点击
  if (signing.value || todaySigned.value) {
    return
  }
  
  signing.value = true
  
  try {
    await signIn()
    
    // 立即更新状态
    todaySigned.value = true
    consecutiveDays.value += 1
    
    // 显示成功提示
    ElMessage.success('签到成功，获得1时间币')
    
    // 刷新用户信息
    await loadProfile()
    
  } catch (err) {
    console.error('签到失败:', err)
    
    if (err.message && err.message.includes('今日已签到')) {
      todaySigned.value = true
      ElMessage.info('今日已签到')
    } else {
      ElMessage.error(err.message || '签到失败')
    }
    
  } finally {
    signing.value = false
  }
}

const handleUpdate = async () => {
  try {
    await updateUser(editForm)
    ElMessage.success('更新成功')
    editDialogVisible.value = false
    await loadProfile()
  } catch (err) {
    console.error(err)
  }
}

watch(() => route.params.userId, () => {
  loadProfile()
  loadSkills()
})

onMounted(() => {
  loadProfile()
  loadSkills()
  loadCreditRadar()
  loadSignStatus()
  
  if (isOwnProfile.value && profileUser.value) {
    editForm.nickname = profileUser.value.nickname
    editForm.email = profileUser.value.email
    editForm.phone = profileUser.value.phone
  }
})
</script>

<style scoped>
.profile-content {
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

.credit-radar {
  margin-top: 20px;
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

.skill-cover h4 {
  font-size: 14px;
  margin-bottom: 5px;
}

.price {
  color: #ff6b00;
  font-weight: bold;
}

.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.quick-actions .el-button {
  width: 100%;
}

.sign-in {
  text-align: center;
}

.sign-in p {
  margin-bottom: 15px;
  color: #666;
}
</style>