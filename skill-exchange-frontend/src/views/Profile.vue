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
          
          <el-card class="credit-radar" v-if="isOwnProfile">
            <template #header>
              <span>信用雷达图</span>
            </template>
            <div ref="radarChart" style="width: 100%; height: 400px;"></div>
          </el-card>
          
          <el-card class="my-skills">
            <template #header>
              <div class="card-header">
                <span>{{ isOwnProfile ? '我的技能' : '发布的技能' }}</span>
                <el-button v-if="isOwnProfile" type="primary" size="small" @click="$router.push('/publish')">
                  发布技能
                </el-button>
              </div>
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
                  <div class="skill-actions" v-if="isOwnProfile">
                    <el-button size="mini" @click.stop="editSkill(skill)">编辑</el-button>
                    <el-button size="mini" type="danger" @click.stop="deleteSkill(skill.id)">删除</el-button>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            <el-empty v-if="skills.length === 0" description="暂无技能" />
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card class="quick-stats">
            <template #header>
              <span>我的数据</span>
            </template>
            <div class="stats-grid">
              <div class="stat-card" @click="$router.push('/profile/collects')">
                <div class="stat-icon"><el-icon :size="24" color="#409eff"><Star /></el-icon></div>
                <div class="stat-info">
                  <span class="stat-value">{{ profileStats.collectCount || 0 }}</span>
                  <span class="stat-label">收藏技能</span>
                </div>
              </div>
              <div class="stat-card" @click="$router.push('/profile/likes')">
                <div class="stat-icon"><el-icon :size="24" color="#f56c6c"><Heart /></el-icon></div>
                <div class="stat-info">
                  <span class="stat-value">{{ profileStats.likeCount || 0 }}</span>
                  <span class="stat-label">点赞技能</span>
                </div>
              </div>
              <div class="stat-card" @click="$router.push('/friends')">
                <div class="stat-icon"><el-icon :size="24" color="#67c23a"><UserFilled /></el-icon></div>
                <div class="stat-info">
                  <span class="stat-value">{{ profileStats.friendCount || 0 }}</span>
                  <span class="stat-label">好友</span>
                </div>
              </div>
              <div class="stat-card" @click="goToMySkills">
                <div class="stat-icon"><el-icon :size="24" color="#e6a23c"><Briefcase /></el-icon></div>
                <div class="stat-info">
                  <span class="stat-value">{{ profileStats.skillCount || 0 }}</span>
                  <span class="stat-label">发布技能</span>
                </div>
              </div>
            </div>
          </el-card>
          
          <el-card class="time-coin-card" v-if="isOwnProfile">
            <template #header>
              <span>我的时间币</span>
            </template>
            <div class="time-coin-content">
              <div class="coin-balance" @click="$router.push('/coin')">
                <span class="coin-icon"><el-icon :size="32" color="#ffd700"><Coins /></el-icon></span>
                <span class="coin-value">{{ profileUser?.timeCoin || 0 }}</span>
              </div>
              <el-button 
                type="primary" 
                size="large" 
                @click="handleSignIn" 
                :loading="signing"
                :disabled="todaySigned || signing"
                class="sign-in-btn"
              >
                {{ todaySigned ? '今日已签到' : '签到领币' }}
              </el-button>
              <p v-if="consecutiveDays > 0" class="sign-in-tip">
                已连续签到 {{ consecutiveDays }} 天
              </p>
            </div>
          </el-card>
          
          <el-card class="exchange-stats" v-if="isOwnProfile">
            <template #header>
              <span>技能交换</span>
            </template>
            <div class="exchange-items">
              <div class="exchange-item" @click="$router.push('/exchanges?type=applied')">
                <span class="exchange-count">{{ profileStats.appliedExchangeCount || 0 }}</span>
                <span class="exchange-label">我申请的</span>
              </div>
              <div class="exchange-item" @click="$router.push('/exchanges?type=received')">
                <span class="exchange-count">{{ profileStats.receivedExchangeCount || 0 }}</span>
                <span class="exchange-label">我收到的</span>
              </div>
            </div>
          </el-card>
          
          <el-card class="password-card">
            <template #header>
              <span>账户安全</span>
            </template>
            <el-button type="text" @click="changePwdDialogVisible = true">
              修改密码
            </el-button>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <el-dialog v-model="editDialogVisible" title="编辑资料" width="500px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="头像">
          <div class="avatar-upload">
            <el-avatar :size="80" :src="editForm.avatar">
              {{ editForm.nickname?.charAt(0) }}
            </el-avatar>
            <el-upload
              class="avatar-uploader"
              action="http://localhost:8080/api/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
              :headers="uploadHeaders"
            >
              <el-button size="small" type="primary">上传头像</el-button>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="editForm.username" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="editForm.nickname" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email" />
        </el-form-item>
        <el-form-item label="出生日期">
          <el-date-picker v-model="editForm.birthday" type="date" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate">保存</el-button>
      </template>
    </el-dialog>
    
    <el-dialog v-model="changePwdDialogVisible" title="修改密码" width="400px">
      <el-form :model="pwdForm" label-width="100px">
        <el-form-item label="原密码">
          <el-input v-model="pwdForm.oldPassword" type="password" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.newPassword" type="password" />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="pwdForm.confirmPassword" type="password" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="changePwdDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleChangePassword">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import Header from '../components/Header.vue'
import { getUserById, updateUser, getCreditRadar, getProfileStats, updatePassword } from '../api/user'
import { getSkillByUser, deleteSkill as deleteSkillApi } from '../api/skill'
import { signIn, checkSigned, getConsecutiveDays } from '../api/coin'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const profileUser = ref(null)
const skills = ref([])
const profileStats = ref({})
const radarChart = ref(null)
const editDialogVisible = ref(false)
const changePwdDialogVisible = ref(false)
const signing = ref(false)
const todaySigned = ref(false)
const consecutiveDays = ref(0)

const editForm = reactive({
  username: '',
  nickname: '',
  email: '',
  birthday: '',
  avatar: ''
})

const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const isOwnProfile = computed(() => {
  const userId = route.params.userId
  return !userId || parseInt(userId) === userStore.userId
})

const uploadHeaders = computed(() => {
  return {
    Authorization: `Bearer ${userStore.token}`
  }
})

const loadProfile = async () => {
  try {
    const userId = route.params.userId || userStore.userId
    const res = await getUserById(userId)
    profileUser.value = res.data
    
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

const loadProfileStats = async () => {
  if (!isOwnProfile.value) return
  try {
    const res = await getProfileStats()
    profileStats.value = res.data || {}
  } catch (err) {
    console.error(err)
    profileStats.value = {}
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
  if (signing.value || todaySigned.value) return
  
  signing.value = true
  
  try {
    await signIn()
    
    todaySigned.value = true
    consecutiveDays.value += 1
    ElMessage.success('签到成功，获得1时间币')
    
    await userStore.refreshUser()
    await loadProfile()
    await loadProfileStats()
    
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
    await updateUser({
      username: editForm.username,
      nickname: editForm.nickname,
      email: editForm.email,
      birthday: editForm.birthday,
      avatar: editForm.avatar
    })
    ElMessage.success('更新成功')
    editDialogVisible.value = false
    await userStore.refreshUser()
    await loadProfile()
  } catch (err) {
    console.error(err)
    ElMessage.error(err.message || '更新失败')
  }
}

const handleChangePassword = async () => {
  if (!pwdForm.oldPassword || !pwdForm.newPassword || !pwdForm.confirmPassword) {
    ElMessage.error('请填写所有字段')
    return
  }
  if (pwdForm.newPassword !== pwdForm.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }
  try {
    await updatePassword({
      oldPassword: pwdForm.oldPassword,
      newPassword: pwdForm.newPassword
    })
    ElMessage.success('密码修改成功')
    changePwdDialogVisible.value = false
    pwdForm.oldPassword = ''
    pwdForm.newPassword = ''
    pwdForm.confirmPassword = ''
  } catch (err) {
    console.error(err)
    ElMessage.error(err.message || '密码修改失败')
  }
}

const handleAvatarSuccess = async (response) => {
  if (response.code === 200) {
    const avatarUrl = 'http://localhost:8080' + response.data
    editForm.avatar = avatarUrl
    await updateUser({ avatar: avatarUrl })
    ElMessage.success('头像上传成功')
    await userStore.refreshUser()
    await loadProfile()
  } else {
    ElMessage.error('头像上传失败')
  }
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('请上传图片文件')
    return false
  }
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2MB')
    return false
  }
  return true
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

const editSkill = (skill) => {
  router.push(`/publish?id=${skill.id}`)
}

const deleteSkill = async (skillId) => {
  try {
    await deleteSkillApi(skillId)
    ElMessage.success('删除成功')
    await loadSkills()
  } catch (err) {
    console.error(err)
    ElMessage.error(err.message || '删除失败')
  }
}

const goToMySkills = () => {
  router.push('/skills?filter=mine')
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
}

watch(() => route.params.userId, () => {
  loadProfile()
  loadSkills()
})

onMounted(() => {
  loadProfile()
  loadSkills()
  loadProfileStats()
  loadCreditRadar()
  loadSignStatus()
  
  if (isOwnProfile.value && profileUser.value) {
    editForm.username = profileUser.value.username
    editForm.nickname = profileUser.value.nickname
    editForm.email = profileUser.value.email
    editForm.birthday = profileUser.value.birthday
    editForm.avatar = profileUser.value.avatar
  }
})

watch(() => profileUser.value, (newUser) => {
  if (isOwnProfile.value && newUser) {
    editForm.username = newUser.username
    editForm.nickname = newUser.nickname
    editForm.email = newUser.email
    editForm.birthday = newUser.birthday
    editForm.avatar = newUser.avatar
  }
}, { immediate: true })
</script>

<style scoped>
.profile-content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.avatar-upload {
  display: flex;
  align-items: center;
  gap: 20px;
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

.skill-actions {
  display: flex;
  gap: 8px;
  margin-top: 10px;
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

.time-coin-card {
  margin-bottom: 20px;
}

.time-coin-content {
  text-align: center;
}

.coin-balance {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 20px;
  cursor: pointer;
}

.coin-icon {
  background: linear-gradient(135deg, #ffd700 0%, #ffb700 100%);
  padding: 12px;
  border-radius: 50%;
}

.coin-value {
  font-size: 48px;
  font-weight: bold;
  color: #ffd700;
}

.sign-in-btn {
  width: 100%;
  margin-bottom: 10px;
}

.sign-in-tip {
  font-size: 12px;
  color: #67c23a;
  margin: 0;
}

.exchange-stats {
  margin-bottom: 20px;
}

.exchange-items {
  display: flex;
  gap: 12px;
}

.exchange-item {
  flex: 1;
  text-align: center;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s;
}

.exchange-item:hover {
  background: #e9ecef;
}

.exchange-count {
  display: block;
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}

.exchange-label {
  font-size: 12px;
  color: #999;
}
</style>