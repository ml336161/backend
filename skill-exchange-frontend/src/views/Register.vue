<template>
  <div class="register-container">
    <div class="register-box">
      <h2 class="register-title">创建账号</h2>
      <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-width="0">
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="确认密码"
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input
            v-model="registerForm.nickname"
            placeholder="昵称"
            prefix-icon="UserFilled"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="birthday">
          <el-date-picker
            v-model="registerForm.birthday"
            type="date"
            placeholder="出生日期"
            size="large"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item prop="email">
          <el-input
            v-model="registerForm.email"
            placeholder="邮箱（选填）"
            prefix-icon="Message"
            size="large"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" @click="handleRegister" style="width: 100%">
            注册
          </el-button>
        </el-form-item>
      </el-form>
      <div class="register-footer">
        已有账号？<span @click="$router.push('/login')">立即登录</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/user'
import { ElMessage } from 'element-plus'

const router = useRouter()

const registerFormRef = ref()
const loading = ref(false)

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  birthday: '',
  email: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次密码输入不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  birthday: [
    { required: true, message: '请选择出生日期', trigger: 'change' }
  ]
}

const handleRegister = async () => {
  try {
    await registerFormRef.value.validate()
    loading.value = true
    
    await register({
      username: registerForm.username,
      password: registerForm.password,
      confirmPassword: registerForm.confirmPassword,
      nickname: registerForm.nickname,
      birthday: registerForm.birthday,
      email: registerForm.email
    })
    
    ElMessage.success('注册成功，请登录')
    router.push('/login')
    
  } catch (err) {
    console.error(err)

    if (err.message && err.message.includes('该邮箱已被注册')) {
       ElMessage.error('该邮箱已被注册，请直接登录')
    } else if (err.message) {
       ElMessage.error(err.message)
    } else {
       ElMessage.error('注册失败，网络错误')
    }
    
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.register-title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.register-footer {
  text-align: center;
  color: #999;
  font-size: 14px;
  margin-top: 15px;
}

.register-footer span {
  color: #409eff;
  cursor: pointer;
}
</style>