<template>
  <div class="exchanges-container">
    <Header />
    
    <div class="exchanges-content">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="我收到的申请" name="received">
          <div class="exchange-list">
            <div v-for="item in receivedExchanges" :key="item.id" class="exchange-item">
              <el-card shadow="hover">
                <div class="exchange-header">
                  <span class="skill-title">{{ item.skill?.title }}</span>
                  <el-tag :type="getStatusType(item.status)">
                    {{ getStatusText(item.status) }}
                  </el-tag>
                </div>
                <div class="exchange-info">
                  <div class="info-row">
                    <span class="label">申请人：</span>
                    <span>{{ item.requester?.nickname }}</span>
                  </div>
                  <div class="info-row">
                    <span class="label">价格：</span>
                    <span class="price">{{ item.price }} 时间币</span>
                  </div>
                  <div class="info-row" v-if="item.scheduledTime">
                    <span class="label">预约时间：</span>
                    <span>{{ formatTime(item.scheduledTime) }}</span>
                  </div>
                  <div class="info-row" v-if="item.remark">
                    <span class="label">备注：</span>
                    <span>{{ item.remark }}</span>
                  </div>
                </div>
                <div class="exchange-actions" v-if="item.status === 'pending'">
                  <el-button type="primary" @click="handleAccept(item.id)">
                    接受
                  </el-button>
                  <el-button @click="handleReject(item.id)">
                    拒绝
                  </el-button>
                </div>
              </el-card>
            </div>
            <el-empty v-if="receivedExchanges.length === 0" description="暂无收到的申请" />
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="我发出的申请" name="sent">
          <div class="exchange-list">
            <div v-for="item in sentExchanges" :key="item.id" class="exchange-item">
              <el-card shadow="hover">
                <div class="exchange-header">
                  <span class="skill-title">{{ item.skill?.title }}</span>
                  <el-tag :type="getStatusType(item.status)">
                    {{ getStatusText(item.status) }}
                  </el-tag>
                </div>
                <div class="exchange-info">
                  <div class="info-row">
                    <span class="label">提供者：</span>
                    <span>{{ item.provider?.nickname }}</span>
                  </div>
                  <div class="info-row">
                    <span class="label">价格：</span>
                    <span class="price">{{ item.price }} 时间币</span>
                  </div>
                </div>
              </el-card>
            </div>
            <el-empty v-if="sentExchanges.length === 0" description="暂无发出的申请" />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '../components/Header.vue'
import { getReceivedExchanges, getSentExchanges, handleExchange } from '../api/exchange'
import { formatDateTime } from '../utils/format'
import { ElMessage, ElMessageBox } from 'element-plus'

const activeTab = ref('received')
const receivedExchanges = ref([])
const sentExchanges = ref([])

const loadReceived = async () => {
  try {
    const res = await getReceivedExchanges()
    receivedExchanges.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const loadSent = async () => {
  try {
    const res = await getSentExchanges()
    sentExchanges.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const getStatusType = (status) => {
  switch (status) {
    case 'pending':
      return 'warning'
    case 'accepted':
      return 'primary'
    case 'completed':
      return 'success'
    case 'rejected':
      return 'danger'
    default:
      return 'info'
  }
}

const getStatusText = (status) => {
  switch (status) {
    case 'pending':
      return '待处理'
    case 'accepted':
      return '已接受'
    case 'completed':
      return '已完成'
    case 'rejected':
      return '已拒绝'
    default:
      return '未知'
  }
}

const handleAccept = async (id) => {
  try {
    await ElMessageBox.confirm('确定接受该申请吗？', '提示')
    await handleExchange({ id, action: 'accept' })
    ElMessage.success('已接受申请')
    await loadReceived()
  } catch (err) {
    console.error(err)
  }
}

const handleReject = async (id) => {
  try {
    await handleExchange({ id, action: 'reject' })
    ElMessage.success('已拒绝申请')
    await loadReceived()
  } catch (err) {
    console.error(err)
  }
}

const formatTime = (time) => {
  return formatDateTime(time)
}

onMounted(() => {
  loadReceived()
  loadSent()
})
</script>

<style scoped>
.exchanges-content {
  max-width: 1000px;
  margin: 20px auto;
  padding: 0 20px;
}

.exchange-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.exchange-item {
  margin-bottom: 0;
}

.exchange-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.skill-title {
  font-size: 16px;
  font-weight: bold;
}

.exchange-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-row {
  display: flex;
}

.label {
  color: #999;
  width: 80px;
}

.price {
  color: #ff6b00;
  font-weight: bold;
}

.exchange-actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}
</style>