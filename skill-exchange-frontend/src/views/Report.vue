<template>
  <div class="report-container">
    <Header />
    
    <div class="report-content">
      <el-card>
        <template #header>
          <span>举报</span>
        </template>
        
        <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
         <el-form-item label="举报类型" prop="targetType">
           <el-radio-group v-model="form.targetType">
             <!-- 将 :label-value 改为 :label -->
             <el-radio :value="'skill'">技能</el-radio>
             <el-radio :value="'comment'">评论</el-radio>
             <el-radio :value="'user'">用户</el-radio>
           </el-radio-group>
         </el-form-item>
          
          <el-form-item label="目标ID" prop="targetId">
            <el-input-number v-model="form.targetId" :min="1" />
          </el-form-item>
          
          <el-form-item label="举报原因" prop="reason">
            <el-select v-model="form.reason" placeholder="选择举报原因" style="width: 100%">
              <el-option label="内容违规" :value="'illegal'" />
              <el-option label="虚假信息" :value="'fake'" />
              <el-option label="骚扰辱骂" :value="'harassment'" />
              <el-option label="其他原因" :value="'other'" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="详细描述">
            <el-input
              v-model="form.description"
              type="textarea"
              :rows="5"
              placeholder="请详细描述举报内容"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" :loading="loading" @click="handleSubmit">
              提交举报
            </el-button>
            <el-button @click="$router.back()">返回</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Header from '../components/Header.vue'
import { createReport } from '../api/report'
import { ElMessage } from 'element-plus'

const route = useRoute()

const formRef = ref()
const loading = ref(false)

const form = reactive({
  targetType: '',
  targetId: null,
  reason: '',
  description: ''
})

const rules = {
  targetType: [{ required: true, message: '请选择举报类型', trigger: 'change' }],
  targetId: [{ required: true, message: '请输入目标ID', trigger: 'blur' }],
  reason: [{ required: true, message: '请选择举报原因', trigger: 'change' }]
}

const handleSubmit = async () => {
  await formRef.value.validate()
  loading.value = true
  
  try {
    await createReport(form)
    ElMessage.success('举报已提交，我们会尽快处理')
    setTimeout(() => {
      window.history.back()
    }, 1000)
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  if (route.query.type) {
    form.targetType = route.query.type
  }
  if (route.query.id) {
    form.targetId = parseInt(route.query.id)
  }
})
</script>

<style scoped>
.report-content {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}
</style>