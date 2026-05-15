<template>
  <div class="publish-container">
    <Header />
    
    <div class="publish-content">
      <h2>发布技能</h2>
      
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="技能类型" prop="typeId">
          <el-select v-model="form.typeId" placeholder="选择技能类型" style="width: 100%">
            <el-option v-for="type in types" :key="type.id" :label="type.name" :value="type.id" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="技能标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入技能标题" maxlength="100" show-word-limit />
        </el-form-item>
        
        <el-form-item label="技能描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="6"
            placeholder="详细描述你的技能，包括服务内容、经验等"
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="价格">
          <el-input-number v-model="form.price" :min="1" :max="100" />
          <span style="margin-left: 8px">时间币</span>
        </el-form-item>
        
        <el-form-item label="服务时长">
          <el-input v-model="form.duration" placeholder="如：1小时、半天、全天" />
        </el-form-item>
        
        <el-form-item label="服务地点">
          <el-input v-model="form.location" placeholder="如：线上、市中心咖啡厅" />
        </el-form-item>
        
        <el-form-item label="封面图片">
          <el-upload
            action="http://localhost:8080/api/upload"
            :headers="{ Authorization: 'Bearer ' + token }"
            list-type="picture-card"
            :on-success="handleUploadSuccess"
            :on-remove="handleUploadRemove"
            :on-exceed="handleExceed"
            :file-list="fileList"
            :limit="1"
          >
            <div v-if="!hasImage">
              <el-icon><Plus /></el-icon>
            </div>
          </el-upload>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" @click="handleSubmit">
            发布技能
          </el-button>
          <el-button size="large" @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import Header from '../components/Header.vue'
import { getActiveTypes } from '../api/skillType'
import { createSkill } from '../api/skill'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref()
const loading = ref(false)
const types = ref([])
const fileList = ref([])
const token = localStorage.getItem('token')

const hasImage = computed(() => {
  return fileList.value.length > 0
})

const form = reactive({
  typeId: null,
  title: '',
  description: '',
  price: 1,
  duration: '',
  location: '',
  images: ''
})

const rules = {
  typeId: [{ required: true, message: '请选择技能类型', trigger: 'change' }],
  title: [{ required: true, message: '请输入技能标题', trigger: 'blur' }],
  description: [{ required: true, message: '请输入技能描述', trigger: 'blur' }]
}

const loadTypes = async () => {
  try {
    const res = await getActiveTypes()
    types.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const handleUploadSuccess = (res) => {
  form.images = res.data
  fileList.value = [{ url: res.data }]
}

const handleUploadRemove = () => {
  form.images = ''
  fileList.value = []
}

const handleExceed = () => {
  ElMessage.warning('只能上传一张封面图片')
}

const handleSubmit = async () => {
  await formRef.value.validate()
  loading.value = true
  
  try {
    await createSkill(form)
    ElMessage.success('发布成功')
    await userStore.refreshUser()
    router.push('/skills')
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadTypes()
})
</script>

<style scoped>
.publish-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.publish-content {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
}

h2 {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #409eff;
}
</style>