<template>
  <div class="collects-container">
    <Header />
    
    <div class="collects-content">
      <div class="page-header">
        <h2>我的收藏</h2>
        <p>共收藏 {{ collects.length }} 个技能</p>
      </div>
      
      <el-row :gutter="20">
        <el-col :span="6" v-for="skill in collects" :key="skill.id">
          <el-card class="skill-card" shadow="hover">
            <div class="skill-cover" @click="$router.push('/skill/' + skill.id)">
              <img v-if="skill.images" :src="'http://localhost:8080' + skill.images.split(',')[0]" />
              <div v-else class="no-image"><el-icon :size="40"><Picture /></el-icon></div>
            </div>
            <div class="skill-info">
              <h3 @click="$router.push('/skill/' + skill.id)">{{ skill.title }}</h3>
              <p class="skill-desc">{{ skill.description }}</p>
              <div class="skill-footer">
                <span class="price">{{ skill.price }} 时间币</span>
                <div class="skill-stats">
                  <span><el-icon :size="14"><Eye /></el-icon>{{ skill.viewCount }}</span>
                  <span><el-icon :size="14"><Heart /></el-icon>{{ skill.likeCount }}</span>
                  <span><el-icon :size="14"><Star /></el-icon>{{ skill.collectCount }}</span>
                </div>
              </div>
              <div class="skill-actions">
                <el-button 
                  size="mini" 
                  type="warning" 
                  @click="handleUncollect(skill.id)"
                  :loading="uncollectingId === skill.id"
                >
                  取消收藏
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-empty v-if="collects.length === 0" description="暂无收藏的技能" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '../components/Header.vue'
import { getUserCollects, collectSkill } from '../api/skill'
import { ElMessage } from 'element-plus'

const collects = ref([])
const uncollectingId = ref(null)

const loadCollects = async () => {
  try {
    const res = await getUserCollects()
    collects.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const handleUncollect = async (skillId) => {
  if (uncollectingId.value === skillId) return
  
  uncollectingId.value = skillId
  
  try {
    await collectSkill(skillId)
    collects.value = collects.value.filter(s => s.id !== skillId)
    ElMessage.success('取消收藏成功')
  } catch (err) {
    console.error(err)
    ElMessage.error(err.message || '取消收藏失败')
  } finally {
    uncollectingId.value = null
  }
}

onMounted(() => {
  loadCollects()
})
</script>

<style scoped>
.collects-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.collects-content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 24px;
  color: #333;
  margin: 0 0 8px 0;
}

.page-header p {
  color: #999;
  margin: 0;
}

.skill-card {
  margin-bottom: 20px;
}

.skill-cover {
  height: 150px;
  background: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  cursor: pointer;
}

.skill-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-image {
  color: #ccc;
}

.skill-info {
  padding: 16px;
}

.skill-info h3 {
  font-size: 16px;
  margin-bottom: 8px;
  color: #333;
  cursor: pointer;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.skill-desc {
  font-size: 13px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 12px;
}

.skill-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.price {
  color: #409eff;
  font-weight: bold;
}

.skill-stats {
  display: flex;
  gap: 8px;
  font-size: 12px;
  color: #999;
}

.skill-actions {
  display: flex;
  justify-content: flex-end;
}
</style>