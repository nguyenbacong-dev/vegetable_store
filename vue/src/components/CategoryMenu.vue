<!-- File: src/components/CategoryMenu.vue -->
<template>
  <aside class="card cat">
    <h3>DANH MỤC</h3>
    <ul>
      <li
        v-for="c in categories"
        :key="c.id"
        @click="select(c)"
        class="cat-item"
      >
        {{ c.name }} <span>›</span>
      </li>

      <!-- Thêm dòng Xem tất cả -->
      <li @click="selectAll" class="cat-item" style="font-weight: bold;">
        Xem tất cả <span>›</span>
      </li>
    </ul>
  </aside>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const emit = defineEmits(['selectCategory'])
const categories = ref([])

onMounted(async () => {
  try {
    const res = await axios.get('/api/categories')
    console.log('📦 Danh mục nhận được:', res.data) // Debug danh mục
    categories.value = res.data.map(c => ({
      ...c,
      id: Number(c.id) // Chuyển id sang Number nếu backend trả Long
    }))
  } catch (error) {
    console.error('❌ Lỗi tải danh mục:', error)
  }
})

const select = (category) => {
  console.log('🔍 Chọn danh mục:', category.id) // Debug khi click
  emit('selectCategory', category.id)
}

const selectAll = () => {
  console.log('🔍 Chọn xem tất cả') // Debug
  emit('selectCategory', null)
}
</script>

<style scoped>
.cat-item {
  cursor: pointer;
}
.cat-item:hover {
  color: green;
}
</style>