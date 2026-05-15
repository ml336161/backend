<template>
  <div class="admin-container">
    <Header />

    <div class="admin-content">
      <el-row :gutter="20" class="stats-row">
        <el-col :span="6" v-for="stat in stats" :key="stat.title">
          <el-card class="stat-card" shadow="hover" @click="handleStatClick(stat)">
            <div class="stat-icon" :style="{ background: stat.color }">
              <el-icon :size="32"><component :is="stat.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <h3 class="stat-value">{{ stat.value }}</h3>
              <p class="stat-title">{{ stat.title }}</p>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-tabs v-model="activeTab" class="admin-tabs" type="border-card">
        <el-tab-pane label="用户管理" name="users">
          <div class="tab-header">
            <span class="tab-title">用户列表</span>
            <el-input
              v-model="userSearch"
              placeholder="搜索用户名或昵称"
              prefix-icon="Search"
              style="width: 300px"
            />
            <el-button type="primary" @click="loadUsers">
              <el-icon><Refresh /></el-icon> 刷新
            </el-button>
          </div>
          <el-table :data="filteredUsers" border stripe class="data-table">
            <el-table-column type="index" label="#" width="60" />
            <el-table-column prop="id" label="ID" width="80" sortable />
            <el-table-column label="头像" width="80">
              <template #default="{ row }">
                <el-avatar :src="getFullUrl(row.avatar)" :size="40">
                  {{ row.nickname?.charAt(0) }}
                </el-avatar>
              </template>
            </el-table-column>
            <el-table-column prop="username" label="用户名" min-width="120" />
            <el-table-column prop="nickname" label="昵称" min-width="120" />
            <el-table-column prop="email" label="邮箱" min-width="180" />
            <el-table-column prop="timeCoin" label="时间币" width="100" sortable>
              <template #default="{ row }">
                <span class="coin-badge">{{ row.timeCoin }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="creditScore" label="信用分" width="100" sortable>
              <template #default="{ row }">
                <el-tag :type="getCreditTagType(row.creditScore)">{{ row.creditScore }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                  {{ row.status === 1 ? '正常' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="注册时间" width="180" sortable>
              <template #default="{ row }">
                {{ formatDate(row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" size="small" link @click="viewUserDetail(row)">
                  查看
                </el-button>
                <el-button :type="row.status === 1 ? 'danger' : 'success'" size="small" link @click="handleToggleStatus(row)">
                  {{ row.status === 1 ? '禁用' : '启用' }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="技能管理" name="skills">
          <div class="tab-header">
            <span class="tab-title">技能列表</span>
            <el-input
              v-model="skillSearch"
              placeholder="搜索技能名称"
              prefix-icon="Search"
              style="width: 300px"
            />
            <el-button type="primary" @click="loadSkills">
              <el-icon><Refresh /></el-icon> 刷新
            </el-button>
          </div>
          <el-table :data="filteredSkills" border stripe class="data-table">
            <el-table-column type="index" label="#" width="60" />
            <el-table-column prop="id" label="ID" width="80" sortable />
            <el-table-column label="封面" width="100">
              <template #default="{ row }">
                <div class="skill-cover" :style="{ backgroundImage: `url(${getFirstImage(row.images)})` }"></div>
              </template>
            </el-table-column>
            <el-table-column prop="title" label="技能名称" min-width="180" />
            <el-table-column prop="typeName" label="分类" width="120" />
            <el-table-column label="发布者" width="120">
              <template #default="{ row }">
                {{ row.user?.nickname || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="price" label="价格" width="100">
              <template #default="{ row }">
                <span class="price-tag">{{ row.price }} 币</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getSkillStatusTagType(row.status)">
                  {{ getSkillStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="发布时间" width="180" sortable>
              <template #default="{ row }">
                {{ formatDate(row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" size="small" link @click="viewSkillDetail(row)">
                  查看
                </el-button>
                <el-popconfirm
                  title="确定要删除这个技能吗？"
                  @confirm="handleDeleteSkill(row.id)"
                >
                  <template #reference>
                    <el-button type="danger" size="small" link>删除</el-button>
                  </template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="技能交换" name="exchanges">
          <div class="tab-header">
            <span class="tab-title">交换记录</span>
            <el-select v-model="exchangeStatusFilter" placeholder="状态筛选" clearable style="width: 150px">
              <el-option label="全部" value="" />
              <el-option label="待确认" value="pending" />
              <el-option label="进行中" value="in_progress" />
              <el-option label="已完成" value="completed" />
              <el-option label="已取消" value="cancelled" />
            </el-select>
            <el-button type="primary" @click="loadExchanges">
              <el-icon><Refresh /></el-icon> 刷新
            </el-button>
          </div>
          <el-table :data="filteredExchanges" border stripe class="data-table">
            <el-table-column type="index" label="#" width="60" />
            <el-table-column prop="id" label="ID" width="80" sortable />
            <el-table-column label="发起方" width="150">
              <template #default="{ row }">
                <div class="user-mini">
                  <el-avatar :size="32" :src="getFullUrl(row.requester?.avatar)">{{ row.requester?.nickname?.charAt(0) }}</el-avatar>
                  <span>{{ row.requester?.nickname }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="接受方" width="150">
              <template #default="{ row }">
                <div class="user-mini">
                  <el-avatar :size="32" :src="getFullUrl(row.provider?.avatar)">{{ row.provider?.nickname?.charAt(0) }}</el-avatar>
                  <span>{{ row.provider?.nickname }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="技能信息" min-width="200">
              <template #default="{ row }">
                <div class="exchange-skill">
                  <span class="skill-name">{{ row.skill?.title }}</span>
                  <span class="price">{{ row.price }} 币</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getExchangeStatusTagType(row.status)">
                  {{ getExchangeStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="appointmentTime" label="预约时间" width="180">
              <template #default="{ row }">
                {{ formatDate(row.appointmentTime) }}
              </template>
            </el-table-column>
            <el-table-column label="创建时间" width="180" sortable>
              <template #default="{ row }">
                {{ formatDate(row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" size="small" link @click="viewExchangeDetail(row)">
                  详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="举报处理" name="reports">
          <div class="tab-header">
            <span class="tab-title">举报列表</span>
            <el-select v-model="reportStatusFilter" placeholder="状态筛选" clearable style="width: 150px">
              <el-option label="全部" value="" />
              <el-option label="待处理" value="pending" />
              <el-option label="已处理" value="approved" />
              <el-option label="已驳回" value="rejected" />
            </el-select>
            <el-button type="primary" @click="loadReports">
              <el-icon><Refresh /></el-icon> 刷新
            </el-button>
          </div>
          <el-table :data="filteredReports" border stripe class="data-table">
            <el-table-column type="index" label="#" width="60" />
            <el-table-column prop="id" label="ID" width="80" sortable />
            <el-table-column label="举报人" width="150">
              <template #default="{ row }">
                <div class="user-mini">
                  <el-avatar :size="32" :src="getFullUrl(row.reporter?.avatar)">{{ row.reporter?.nickname?.charAt(0) }}</el-avatar>
                  <span>{{ row.reporter?.nickname }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="targetType" label="举报类型" width="100">
              <template #default="{ row }">
                <el-tag :type="row.targetType === 'user' ? 'info' : 'warning'">{{ row.targetType === 'user' ? '用户' : '技能' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="被举报对象" min-width="200">
              <template #default="{ row }">
                <div v-if="row.targetType === 'user'" class="user-mini">
                  <el-avatar :size="32" :src="getFullUrl(row.targetUser?.avatar)">{{ row.targetUser?.nickname?.charAt(0) }}</el-avatar>
                  <span>{{ row.targetUser?.nickname }}</span>
                </div>
                <div v-else-if="row.targetType === 'skill'" class="skill-mini">
                  <el-image 
                    v-if="row.targetSkill?.images"
                    :src="getFirstImage(row.targetSkill.images)" 
                    :preview-src-list="row.targetSkill.images.split(',').map(img => getFullUrl(img))"
                    fit="cover"
                    style="width: 32px; height: 32px; border-radius: 4px; flex-shrink: 0;"
                  />
                  <div class="skill-title-scroll">
                    <el-link 
                      type="primary" 
                      style="cursor: pointer; white-space: nowrap;"
                      @click="viewSkillDetail(row.targetSkill)"
                    >
                      {{ row.targetSkill?.title }}
                    </el-link>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="reason" label="举报原因" min-width="200" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getReportStatusTagType(row.status)">
                  {{ getReportStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="举报时间" width="180" sortable>
              <template #default="{ row }">
                {{ formatDate(row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <div v-if="row.status === 'pending'">
                  <el-button type="success" size="small" link @click="submitReport(row.id, 'approved')">
                    通过
                  </el-button>
                  <el-button type="danger" size="small" link @click="submitReport(row.id, 'rejected')">
                    驳回
                  </el-button>
                </div>
                <el-tag type="info" v-else>已处理</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="意见反馈" name="feedback">
          <div class="tab-header">
            <span class="tab-title">反馈列表</span>
            <el-button type="primary" @click="loadFeedbacks">
              <el-icon><Refresh /></el-icon> 刷新
            </el-button>
          </div>
          <el-table :data="feedbacks" border stripe class="data-table">
            <el-table-column type="index" label="#" width="60" />
            <el-table-column prop="id" label="ID" width="80" sortable />
            <el-table-column label="用户" width="150">
              <template #default="{ row }">
                <div class="user-mini">
                  <el-avatar :size="32" :src="getFullUrl(row.user?.avatar)">{{ row.user?.nickname?.charAt(0) }}</el-avatar>
                  <span>{{ row.user?.nickname }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="title" label="标题" min-width="150" />
            <el-table-column prop="content" label="内容" min-width="250" show-overflow-tooltip />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.reply ? 'success' : 'warning'">
                  {{ row.reply ? '已回复' : '待回复' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="反馈时间" width="180" sortable>
              <template #default="{ row }">
                {{ formatDate(row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" size="small" link @click="openReplyDialog(row)">
                  {{ row.reply ? '查看回复' : '回复' }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog v-model="userDetailDialogVisible" title="用户详情" width="600px">
      <div v-if="currentUser" class="detail-content">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="用户ID">{{ currentUser.id }}</el-descriptions-item>
          <el-descriptions-item label="用户名">{{ currentUser.username }}</el-descriptions-item>
          <el-descriptions-item label="昵称">{{ currentUser.nickname }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ currentUser.email }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ currentUser.phone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="时间币">
            <span class="coin-badge">{{ currentUser.timeCoin }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="信用分">
            <el-tag :type="getCreditTagType(currentUser.creditScore)">{{ currentUser.creditScore }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="currentUser.status === 1 ? 'success' : 'danger'">
              {{ currentUser.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="角色">{{ currentUser.role === 'admin' ? '管理员' : '普通用户' }}</el-descriptions-item>
          <el-descriptions-item label="注册时间">{{ formatDate(currentUser.createTime) }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="userDetailDialogVisible = false">关闭</el-button>
        <el-button :type="currentUser?.status === 1 ? 'danger' : 'success'" @click="handleToggleStatus(currentUser)">
          {{ currentUser?.status === 1 ? '禁用用户' : '启用用户' }}
        </el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="replyDialogVisible" title="回复反馈" width="550px">
      <div class="reply-content">
        <div class="feedback-preview">
          <h4>{{ replyingFeedback?.title }}</h4>
          <p class="feedback-text">{{ replyingFeedback?.content }}</p>
          <div v-if="replyingFeedback?.reply" class="reply-history">
            <el-divider content-position="left">已回复</el-divider>
            <p class="reply-text">{{ replyingFeedback?.reply }}</p>
          </div>
        </div>
        <el-divider />
        <el-input
          v-if="!replyingFeedback?.reply"
          v-model="replyContent"
          type="textarea"
          :rows="5"
          placeholder="请输入回复内容"
        />
      </div>
      <template #footer>
        <el-button @click="replyDialogVisible = false">取消</el-button>
        <el-button v-if="!replyingFeedback?.reply" type="primary" @click="handleReply">发送回复</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import Header from '../components/Header.vue'
import { getDashboardStats, updateUserStatus, getReports, getFeedbacks, handleReport, replyFeedback, getUsers, getSkills, getExchanges, deleteSkill } from '../api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Tickets, Coin, Warning, Document, Refresh } from '@element-plus/icons-vue'

const getFullUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http://') || url.startsWith('https://')) return url
  if (url.startsWith('/')) return 'http://localhost:8080' + url
  return 'http://localhost:8080/' + url
}

const getFirstImage = (images) => {
  if (!images) return ''
  const firstImage = images.split(',')[0]
  return getFullUrl(firstImage)
}

const activeTab = ref('users')
const userSearch = ref('')
const skillSearch = ref('')
const reportStatusFilter = ref('')
const exchangeStatusFilter = ref('')
const stats = ref([
  { title: '用户数', value: 0, color: '#409eff', icon: 'User', tab: 'users' },
  { title: '技能数', value: 0, color: '#67c23a', icon: 'Tickets', tab: 'skills' },
  { title: '交换数', value: 0, color: '#e6a23c', icon: 'Coin', tab: 'exchanges' },
  { title: '待处理举报', value: 0, color: '#f56c6c', icon: 'Warning', tab: 'reports' },
  { title: '待回复反馈', value: 0, color: '#909399', icon: 'Document', tab: 'feedback' }
])
const users = ref([])
const skills = ref([])
const exchanges = ref([])
const reports = ref([])
const feedbacks = ref([])

const userDetailDialogVisible = ref(false)
const replyDialogVisible = ref(false)
const currentUser = ref(null)
const replyingFeedback = ref(null)
const replyContent = ref('')

const filteredUsers = computed(() => {
  if (!userSearch.value) return users.value
  const search = userSearch.value.toLowerCase()
  return users.value.filter(u => 
    u.username?.toLowerCase().includes(search) || 
    u.nickname?.toLowerCase().includes(search)
  )
})

const filteredSkills = computed(() => {
  if (!skillSearch.value) return skills.value
  const search = skillSearch.value.toLowerCase()
  return skills.value.filter(s => s.title?.toLowerCase().includes(search))
})

const filteredReports = computed(() => {
  if (!reportStatusFilter.value) return reports.value
  return reports.value.filter(r => r.status === reportStatusFilter.value)
})

const filteredExchanges = computed(() => {
  if (!exchangeStatusFilter.value) return exchanges.value
  return exchanges.value.filter(e => e.status === exchangeStatusFilter.value)
})

const loadStats = async () => {
  try {
    const res = await getDashboardStats()
    stats.value[0].value = res.data.userCount || 0
    stats.value[1].value = res.data.skillCount || 0
    stats.value[2].value = res.data.exchangeCount || 0
    stats.value[3].value = res.data.pendingReportCount || 0
    stats.value[4].value = res.data.pendingFeedbackCount || 0
  } catch (err) {
    console.error(err)
  }
}

const loadUsers = async () => {
  try {
    const res = await getUsers()
    users.value = res.data || []
  } catch (err) {
    console.error(err)
    ElMessage.error('加载用户列表失败')
  }
}

const loadSkills = async () => {
  try {
    const res = await getSkills()
    skills.value = res.data || []
  } catch (err) {
    console.error(err)
    ElMessage.error('加载技能列表失败')
  }
}

const loadExchanges = async () => {
  try {
    const res = await getExchanges()
    exchanges.value = res.data || []
  } catch (err) {
    console.error(err)
    ElMessage.error('加载交换记录失败')
  }
}

const loadReports = async () => {
  try {
    const res = await getReports()
    reports.value = res.data || []
  } catch (err) {
    console.error(err)
    ElMessage.error('加载举报列表失败')
  }
}

const loadFeedbacks = async () => {
  try {
    const res = await getFeedbacks()
    feedbacks.value = res.data || []
    stats.value[4].value = feedbacks.value.filter(f => !f.reply).length
  } catch (err) {
    console.error(err)
    ElMessage.error('加载反馈列表失败')
  }
}

const handleStatClick = (stat) => {
  if (stat.tab) {
    activeTab.value = stat.tab
  }
}

const viewUserDetail = (user) => {
  currentUser.value = { ...user }
  userDetailDialogVisible.value = true
}

const viewSkillDetail = (skill) => {
  ElMessageBox.alert(
    `<div style="text-align: left">
      <h4>${skill.title}</h4>
      <p><strong>分类：</strong>${skill.typeName || '-'}</p>
      <p><strong>价格：</strong>${skill.price} 时间币</p>
      <p><strong>状态：</strong>${getSkillStatusText(skill.status)}</p>
      <p><strong>描述：</strong>${skill.description || '-'}</p>
      <p><strong>发布时间：</strong>${formatDate(skill.createTime)}</p>
    </div>`,
    '技能详情',
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: '关闭'
    }
  )
}

const goToSkillDetail = (skillId) => {
  const skill = skills.value.find(s => s.id === skillId)
  if (skill) {
    viewSkillDetail(skill)
  }
}

const viewExchangeDetail = (exchange) => {
  ElMessageBox.alert(
    `<div style="text-align: left">
      <h4>交换记录 #${exchange.id}</h4>
      <p><strong>状态：</strong>${getExchangeStatusText(exchange.status)}</p>
      <p><strong>技能：</strong>${exchange.skill?.title || '-'}</p>
      <p><strong>价格：</strong>${exchange.price} 时间币</p>
      <p><strong>预约时间：</strong>${formatDate(exchange.appointmentTime)}</p>
      <p><strong>备注：</strong>${exchange.note || '-'}</p>
    </div>`,
    '交换详情',
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: '关闭'
    }
  )
}

const handleToggleStatus = async (user) => {
  try {
    const newStatus = user.status === 1 ? 0 : 1
    await updateUserStatus(user.id, newStatus)
    user.status = newStatus
    if (currentUser.value && currentUser.value.id === user.id) {
      currentUser.value.status = newStatus
    }
    ElMessage.success('操作成功')
  } catch (err) {
    console.error(err)
    ElMessage.error('操作失败')
  }
}

const handleDeleteSkill = async (id) => {
  try {
    await deleteSkill(id)
    skills.value = skills.value.filter(s => s.id !== id)
    ElMessage.success('删除成功')
  } catch (err) {
    console.error(err)
    ElMessage.error('删除失败')
  }
}

const submitReport = async (id, status) => {
  try {
    await handleReport({ id, status, handleResult: '' })
    ElMessage.success('处理成功')
    await loadReports()
    await loadStats()
  } catch (err) {
    console.error(err)
    ElMessage.error('处理失败')
  }
}

const openReplyDialog = (feedback) => {
  replyingFeedback.value = feedback
  replyContent.value = feedback.reply || ''
  replyDialogVisible.value = true
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
    await loadFeedbacks()
  } catch (err) {
    console.error(err)
    ElMessage.error('回复失败')
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

const getCreditTagType = (score) => {
  if (score >= 80) return 'success'
  if (score >= 60) return 'warning'
  return 'danger'
}

const getSkillStatusTagType = (status) => {
  const map = { 'active': 'success', 'inactive': 'info', 'banned': 'danger' }
  return map[status] || 'info'
}

const getSkillStatusText = (status) => {
  const map = { 'active': '正常', 'inactive': '下架', 'banned': '封禁' }
  return map[status] || status
}

const getExchangeStatusTagType = (status) => {
  const map = { 'pending': 'warning', 'in_progress': 'primary', 'completed': 'success', 'cancelled': 'info' }
  return map[status] || 'info'
}

const getExchangeStatusText = (status) => {
  const map = { 'pending': '待确认', 'in_progress': '进行中', 'completed': '已完成', 'cancelled': '已取消' }
  return map[status] || status
}

const getReportStatusTagType = (status) => {
  const map = { 'pending': 'warning', 'approved': 'success', 'rejected': 'info' }
  return map[status] || 'info'
}

const getReportStatusText = (status) => {
  const map = { 'pending': '待处理', 'approved': '通过', 'rejected': '驳回' }
  return map[status] || status
}

onMounted(() => {
  loadStats()
  loadUsers()
  loadSkills()
  loadExchanges()
  loadReports()
  loadFeedbacks()
})
</script>

<style scoped>
.admin-content {
  max-width: 1400px;
  margin: 20px auto;
  padding: 0 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
  cursor: pointer;
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-3px);
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  margin: 0 0 8px 0;
  color: #333;
}

.stat-title {
  color: #999;
  margin: 0;
  font-size: 14px;
}

.admin-tabs {
  margin-top: 20px;
}

.tab-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.tab-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.data-table {
  margin-top: 16px;
}

.user-mini {
  display: flex;
  align-items: center;
  gap: 8px;
}

.skill-cover {
  width: 60px;
  height: 60px;
  border-radius: 6px;
  background-size: cover;
  background-position: center;
  background-color: #f5f7fa;
}

.exchange-skill {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.skill-name {
  font-weight: 500;
}

.price {
  color: #e6a23c;
  font-size: 12px;
}

.coin-badge {
  display: inline-block;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 2px 10px;
  border-radius: 12px;
  font-weight: 500;
}

.price-tag {
  color: #e6a23c;
  font-weight: 500;
}

.skill-mini {
  display: flex;
  align-items: center;
  gap: 8px;
  overflow: hidden;
}

.skill-title-scroll {
  overflow-x: auto;
  overflow-y: hidden;
  max-width: 160px;
  scrollbar-width: thin;
  scrollbar-color: #c0c4cc #f5f7fa;
}

.skill-title-scroll::-webkit-scrollbar {
  height: 4px;
}

.skill-title-scroll::-webkit-scrollbar-track {
  background: #f5f7fa;
  border-radius: 2px;
}

.skill-title-scroll::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 2px;
}

.skill-title-scroll::-webkit-scrollbar-thumb:hover {
  background: #909399;
}

.detail-content {
  padding: 10px 0;
}

.feedback-preview {
  background: #f5f7fa;
  padding: 16px;
  border-radius: 8px;
}

.feedback-preview h4 {
  margin: 0 0 12px 0;
  color: #333;
}

.feedback-text {
  margin: 0;
  color: #666;
  line-height: 1.6;
}

.reply-history {
  margin-top: 16px;
}

.reply-text {
  margin: 0;
  color: #409eff;
  line-height: 1.6;
}
</style>
