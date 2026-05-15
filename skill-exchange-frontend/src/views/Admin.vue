<template>
  <div class="admin-container">
    <Header />

    <div class="admin-content">
      <el-row :gutter="20">
        <el-col :span="6" v-for="stat in stats" :key="stat.title">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-icon" :style="{ background: stat.color }">
              <el-icon :size="30"><component :is="stat.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stat.value }}</h3>
              <p>{{ stat.title }}</p>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-tabs v-model="activeTab" style="margin-top: 20px">
        <el-tab-pane label="用户管理" name="users">
          <el-table :data="users" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="username" label="用户名" />
            <el-table-column prop="nickname" label="昵称" />
            <el-table-column prop="timeCoin" label="时间币" width="100" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                  {{ row.status === 1 ? '正常' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button link @click="handleToggleStatus(row)">
                  {{ row.status === 1 ? '禁用' : '启用' }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="举报处理" name="reports">
          <el-table :data="reports" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column label="举报人" width="120">
              <template #default="{ row }">
                {{ row.reporter?.nickname }}
              </template>
            </el-table-column>
            <el-table-column prop="targetType" label="举报类型" width="100" />
            <el-table-column prop="reason" label="举报原因" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'pending' ? 'warning' : 'success'">
                  {{ row.status === 'pending' ? '待处理' : '已处理' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <!-- 修复点：v-if 从 template 标签移到了内部的 div -->
              <template #default="{ row }">
                <div v-if="row.status === 'pending'">
                  <el-button link type="primary" @click="submitReport(row.id, 'approved')">
                    通过
                  </el-button>
                  <el-button link type="primary" @click="submitReport(row.id, 'rejected')">
                    驳回
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="意见反馈" name="feedback">
          <el-table :data="feedbacks" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column label="用户" width="120">
              <template #default="{ row }">
                {{ row.user?.nickname }}
              </template>
            </el-table-column>
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="content" label="内容" show-overflow-tooltip />
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button link @click="replyDialogVisible = true; replyingFeedback = row">
                  回复
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog v-model="replyDialogVisible" title="回复反馈" width="500px">
      <h4>{{ replyingFeedback?.title }}</h4>
      <p>{{ replyingFeedback?.content }}</p>
      <el-divider />
      <el-input v-model="replyContent" type="textarea" :rows="4" placeholder="请输入回复内容" />
      <template #footer>
        <el-button @click="replyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleReply">发送回复</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '../components/Header.vue'
import { getDashboardStats, updateUserStatus, getReports, getFeedbacks, handleReport, replyFeedback } from '../api/admin'
import { ElMessage } from 'element-plus'

const activeTab = ref('users')
const stats = ref([
  { title: '用户数', value: 0, color: '#409eff', icon: 'User' },
  { title: '技能数', value: 0, color: '#67c23a', icon: 'Tickets' },
  { title: '交换数', value: 0, color: '#e6a23c', icon: 'Coin' },
  { title: '待处理举报', value: 0, color: '#f56c6c', icon: 'Warning' }
])
const users = ref([])
const reports = ref([])
const feedbacks = ref([])
const replyDialogVisible = ref(false)
const replyingFeedback = ref(null)
const replyContent = ref('')

const loadStats = async () => {
  try {
    const res = await getDashboardStats()
    stats.value[0].value = res.data.userCount || 0
    stats.value[1].value = res.data.skillCount || 0
    stats.value[2].value = res.data.exchangeCount || 0
    stats.value[3].value = res.data.pendingReportCount || 0
  } catch (err) {
    console.error(err)
  }
}

const loadUsers = async () => {
  try {
    const res = await getDashboardStats()
    // 这里需要完善，先使用模拟数据
    users.value = []
  } catch (err) {
    console.error(err)
  }
}

const loadReports = async () => {
  try {
    const res = await getReports()
    reports.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const loadFeedbacks = async () => {
  try {
    const res = await getFeedbacks()
    feedbacks.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const handleToggleStatus = async (user) => {
  try {
    const newStatus = user.status === 1 ? 0 : 1
    await updateUserStatus(user.id, newStatus)
    user.status = newStatus
    ElMessage.success('操作成功')
  } catch (err) {
    console.error(err)
  }
}

// 修复点：将函数名从 handleReport 改为 submitReport
const submitReport = async (id, status) => {
  try {
    // 这里调用的是从 API 导入的 handleReport 函数
    await handleReport({ id, status, handleResult: '' })
    ElMessage.success('处理成功')
    await loadReports()
  } catch (err) {
    console.error(err)
  }
}

const handleReply = async () => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  try {
    await replyFeedback({ id: replyingFeedback.value.id, reply: replyContent.value })
    ElMessage.success('回复成功')
    replyDialogVisible.value = false
    replyContent.value = ''
  } catch (err) {
    console.error(err)
  }
}

onMounted(() => {
  loadStats()
  loadReports()
  loadFeedbacks()
})
</script>

<style scoped>
.admin-content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.stat-info h3 {
  font-size: 28px;
  margin-bottom: 5px;
}

.stat-info p {
  color: #999;
  margin: 0;
}
</style>