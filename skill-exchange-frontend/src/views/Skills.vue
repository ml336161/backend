<template>
  <div class="skills-container">
    <Header />
    
    <div class="search-bar">
      <div class="search-content">
        <el-input
          v-model="keyword"
          placeholder="搜索技能..."
          size="large"
          @keyup.enter="handleSearch"
          style="width: 300px"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch" />
          </template>
        </el-input>
        
        <div class="price-filter-inline">
          <el-input-number v-model="minPrice" :min="0" :max="999" size="large" style="width: 140px" />
          <span class="price-separator">-</span>
          <el-input-number v-model="maxPrice" :min="0" :max="999" size="large" style="width: 140px" />
          <el-button size="large" type="primary" @click="handlePriceFilter">筛选</el-button>
          <el-button size="large" @click="resetPriceFilter">重置</el-button>
        </div>
        
        <el-button type="primary" size="large" @click="$router.push('/publish')">
          发布技能
        </el-button>
      </div>
      
      <div class="quick-filter">
        <span class="filter-label">快速筛选：</span>
        <el-tag v-for="range in priceRanges" :key="range.label" 
          :class="{ active: selectedPriceRange === range.label }"
          @click="selectPriceRange(range)">
          {{ range.label }}
        </el-tag>
      </div>
    </div>
    
    <div class="main-content">
      <div class="type-sidebar">
        <h3>技能分类</h3>
        <el-menu :default-active="String(selectedType)" @select="handleTypeSelect">
          <el-menu-item :index="'0'">全部</el-menu-item>
          <el-menu-item v-for="type in types" :key="type.id" :index="String(type.id)">
            {{ type.name }}
          </el-menu-item>
        </el-menu>
      </div>
      
      <div class="skills-list">
        <el-row :gutter="20">
          <el-col :span="8" v-for="skill in skills" :key="skill.id">
            <el-card class="skill-card" shadow="hover" @click="$router.push('/skill/' + skill.id)">
              <div class="skill-cover">
                <img v-if="skill.images" :src="'http://localhost:8080' + skill.images.split(',')[0]" />
                <div v-else class="no-image">
                  <el-icon :size="48"><Picture /></el-icon>
                </div>
              </div>
              <div class="skill-info">
                <h3>{{ skill.title }}</h3>
                <p class="description">{{ skill.description }}</p>
                <div class="skill-meta">
                  <span class="type-tag">{{ skill.typeName }}</span>
                  <span class="price">{{ skill.price }} 时间币</span>
                </div>
                <div class="skill-footer">
                  <div class="user-info">
                    <el-avatar :size="24" :src="skill.user?.avatar">
                      {{ skill.user?.nickname?.charAt(0) }}
                    </el-avatar>
                    <span>{{ skill.user?.nickname }}</span>
                  </div>
                  <div class="stats">
                    <span><el-icon><View /></el-icon> {{ skill.viewCount }}</span>
                    <span><el-icon><Star /></el-icon> {{ skill.likeCount }}</span>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <el-empty v-if="skills.length === 0" description="暂无技能" />
        
        <div class="pagination" v-if="total > 0">
          <el-pagination
            v-model:current-page="pageNum"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="loadSkills"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '../components/Header.vue'
import { getSkillList } from '../api/skill'
import { getActiveTypes } from '../api/skillType'
import { Search } from '@element-plus/icons-vue'

const skills = ref([])
const types = ref([])
const keyword = ref('')
const selectedType = ref(0)
const pageNum = ref(1)
const pageSize = ref(12)
const total = ref(0)
const minPrice = ref(null)
const maxPrice = ref(null)
const selectedPriceRange = ref('')

const priceRanges = [
  { label: '0-5', min: 0, max: 5 },
  { label: '5-10', min: 5, max: 10 },
  { label: '10-20', min: 10, max: 20 },
  { label: '20+', min: 20, max: null }
]

const loadSkills = async () => {
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      keyword: keyword.value || null,
      typeId: selectedType.value === 0 ? null : selectedType.value,
      minPrice: minPrice.value,
      maxPrice: maxPrice.value
    }
    const res = await getSkillList(params)
    skills.value = res.data.list || []
    total.value = res.data.total || 0
  } catch (err) {
    console.error(err)
  }
}

const handlePriceFilter = () => {
  pageNum.value = 1
  selectedPriceRange.value = ''
  loadSkills()
}

const resetPriceFilter = () => {
  minPrice.value = null
  maxPrice.value = null
  selectedPriceRange.value = ''
  pageNum.value = 1
  loadSkills()
}

const selectPriceRange = (range) => {
  minPrice.value = range.min
  maxPrice.value = range.max
  selectedPriceRange.value = range.label
  pageNum.value = 1
  loadSkills()
}

const loadTypes = async () => {
  try {
    const res = await getActiveTypes()
    types.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const handleSearch = () => {
  pageNum.value = 1
  loadSkills()
}

const handleTypeSelect = (index) => {
  selectedType.value = parseInt(index)
  pageNum.value = 1
  loadSkills()
}

onMounted(() => {
  loadSkills()
  loadTypes()
})
</script>

<style scoped>
.search-bar {
  background: #f5f7fa;
  padding: 20px 0;
}

.search-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  gap: 16px;
  flex-wrap: wrap;
  align-items: center;
}

.price-filter-inline {
  display: flex;
  align-items: center;
  gap: 8px;
}

.price-separator {
  color: #999;
  font-weight: bold;
}

.quick-filter {
  max-width: 1400px;
  margin: 12px auto 0;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 20px;
}

.filter-label {
  color: #666;
  font-size: 14px;
}

.quick-filter .el-tag {
  cursor: pointer;
  transition: all 0.2s;
}

.quick-filter .el-tag:hover {
  transform: scale(1.05);
}

.quick-filter .el-tag.active {
  background: #409eff;
  color: #fff;
}

.main-content {
  max-width: 1400px;
  margin: 20px auto;
  display: flex;
  gap: 20px;
  padding: 0 20px;
}

.type-sidebar {
  width: 200px;
  flex-shrink: 0;
}

.type-sidebar h3 {
  padding: 10px 0;
  color: #333;
  font-size: 14px;
}

.skills-list {
  flex: 1;
}

.skill-card {
  cursor: pointer;
  margin-bottom: 20px;
}

.skill-cover {
  height: 180px;
  background: #f5f7fa;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.skill-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-image {
  color: #ccc;
}

.skill-info h3 {
  font-size: 16px;
  margin-bottom: 8px;
  color: #333;
}

.description {
  font-size: 13px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 10px;
}

.skill-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.type-tag {
  background: #e6f7ff;
  color: #1890ff;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.price {
  color: #ff6b00;
  font-weight: bold;
}

.skill-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid #f0f0f0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #666;
}

.stats {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #999;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}
</style>