<template>
  <div class="feedback-container">
    <Header />
    
    <div class="feedback-content">
      <el-card>
        <template #header>
          <span>意见反馈</span>
        </template>
        
        <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
          <el-form-item label="反馈类型" prop="type">
           <el-radio-group v-model="form.type">
             <el-radio :label-value="'bug'">问题反馈</el-radio>
             <el-radio :label-value="'feature'">功能建议</el-radio>
             <el-radio :label-value="'other'">其他</el-radio>
           </el-radio-group>
          </el-form-item>
          
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入标题" maxlength="100" />
          </el-form-item>
          
          <el-form-item label="详细内容" prop="content">
            <el-input
              v-model="form.content"
              type="textarea"
              :rows="6"
              placeholder="请详细描述您的问题或建议"
            />
          </el-form-item>
          
          <el-form-item label="联系方式">
            <el-input v-model="form.contact" placeholder="您的邮箱或手机号（可选）" />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" :loading="loading" @click="handleSubmit">
              提交反馈
            </el-button>
            <el-button @click="loadMyFeedbacks">查看我的反馈</el-button>
          </el-form-item>
        </el-form>
      </el-card>
      
      <el-card class="my-feedbacks" v-if="showMyFeedbacks">
        <template #header>
          <span>我的反馈</span>
        </template>
        <div v-for="item in myFeedbacks" :key="item.id" class="feedback-item">
          <div class="feedback-header">
            <span class="title">{{ item.title }}</span>
            <el-tag size="small">{{ item.status === 'pending' ? '待处理' : '已处理' }}</el-tag>
          </div>
          <p>{{ item.content }}</p>
          <div class="reply" v-if="item.reply">
            <span class="label">官方回复：</span>
            <span>{{ item.reply }}</span>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import Header from '../components/Header.vue'
import { createFeedback, getMyFeedbacks } from '../api/feedback'
import { ElMessage } from 'element-plus'

const formRef = ref()
const loading = ref(false)
const showMyFeedbacks = ref(false)
const myFeedbacks = ref([])

const form = reactive({
  type: '',
  title: '',
  content: '',
  contact: ''
})

const rules = {
  type: [{ required: true, message: '请选择反馈类型', trigger: 'change' }],
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入详细内容', trigger: 'blur' }]
}

const handleSubmit = async () => {
  await formRef.value.validate()
  loading.value = true
  
  try {
    await createFeedback(form)
    ElMessage.success('感谢您的反馈，我们会认真对待')
    Object.assign(form, { type: '', title: '', content: '', contact: '' })
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

const loadMyFeedbacks = async () => {
  try {
    const res = await getMyFeedbacks()
    myFeedbacks.value = res.data || []
    showMyFeedbacks.value = true
  } catch (err) {
    console.error(err)
  }
}
</script>

<style scoped>
.feedback-content {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}

.my-feedbacks {
  margin-top: 20px;
}

.feedback-item {
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.feedback-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.feedback-header .title {
  font-weight: bold;
}

.feedback-item p {
  color: #666;
  margin-bottom: 10px;
}

.reply {
  padding: 10px;
  background: #f0f9ff;
  border-left: 3px solid #409eff;
}

.reply .label {
  color: #409eff;
  font-weight: bold;
}
</style>