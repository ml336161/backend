<template>
  <div class="coin-container">
    <Header />
    
    <div class="coin-content">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="balance-card">
            <div class="balance-info">
              <h2>当前时间币</h2>
              <div class="balance-value">{{ userStore.user?.timeCoin || 0 }}</div>
              <p>个</p>
            </div>
          </el-card>
          
          <el-card class="sign-in-card">
            <h3>每日签到</h3>
            <p>连续签到 {{ consecutiveDays }} 天</p>
            <el-button 
              type="primary" 
              size="large" 
              :disabled="todaySigned" 
              @click="handleSignIn"
              :loading="signing"
            >
              {{ todaySigned ? '今日已签到' : '立即签到' }}
            </el-button>
            <p class="sign-tip">每天签到获得 1 时间币</p>
          </el-card>
        </el-col>
        
        <el-col :span="16">
          <el-card>
            <template #header>
              <span>时间币明细</span>
            </template>
            <div class="logs-list">
              <div v-for="log in logs" :key="log.id" class="log-item">
                <div class="log-icon" :class="log.type === 'income' ? 'income' : 'expense'">
                  <el-icon><Coin /></el-icon>
                </div>
                <div class="log-info">
                  <span class="desc">{{ log.description }}</span>
                  <span class="time">{{ formatTime(log.createTime) }}</span>
                </div>
                <span class="amount" :class="log.type === 'income' ? 'income' : 'expense'">
                  {{ log.type === 'income' ? '+' : '-' }}{{ log.amount }}
                </span>
              </div>
              <el-empty v-if="logs.length === 0" description="暂无记录" />
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
import { signIn, checkSigned, getConsecutiveDays, getCoinLogs } from '../api/coin'
import { formatDateTime } from '../utils/format'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const logs = ref([])
const todaySigned = ref(false)
const consecutiveDays = ref(0)

// ✅ 修复：定义 signing 状态变量
const signing = ref(false)

const loadLogs = async () => {
  try {
    const res = await getCoinLogs()
    logs.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const loadSignStatus = async () => {
  try {
    const [res1, res2] = await Promise.all([checkSigned(), getConsecutiveDays()])
    todaySigned.value = res1.data
    consecutiveDays.value = res2.data
  } catch (err) {
    console.error(err)
  }
}

const handleSignIn = async () => {
  if (todaySigned.value) {
    return;
  }
  
  signing.value = true; // 开启加载动画
  
  try {
    await signIn();
    todaySigned.value = true;
    ElMessage.success('签到成功');
    
    // 刷新数据
    await loadLogs();
    await loadSignStatus();
  } catch (err) {
    console.error(err);
    if (err.message && err.message.includes('今日已签到')) {
      todaySigned.value = true;
    }
  } finally {
    signing.value = false; // 关闭加载动画
  }
}

const formatTime = (time) => {
  return formatDateTime(time)
}

onMounted(() => {
  loadLogs()
  loadSignStatus()
})
</script>

<style scoped>
.coin-content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.balance-card {
  text-align: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.balance-info h2 {
  font-size: 16px;
  margin-bottom: 10px;
}

.balance-value {
  font-size: 60px;
  font-weight: bold;
}

.sign-in-card {
  margin-top: 20px;
  text-align: center;
}

.sign-in-card h3 {
  margin-bottom: 10px;
}

.sign-in-card p {
  color: #666;
  margin-bottom: 15px;
}

.sign-tip {
  font-size: 12px;
  color: #999;
  margin-top: 10px;
}

.logs-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.log-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.log-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.log-icon.income {
  background: #e6f7ff;
  color: #1890ff;
}

.log-icon.expense {
  background: #fff1f0;
  color: #ff4d4f;
}

.log-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.log-info .desc {
  font-weight: 500;
  margin-bottom: 4px;
}

.log-info .time {
  font-size: 12px;
  color: #999;
}

.amount {
  font-size: 20px;
  font-weight: bold;
}

.amount.income {
  color: #52c41a;
}

.amount.expense {
  color: #ff4d4f;
}
</style>