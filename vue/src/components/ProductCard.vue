<!-- File: src/components/ProductCard.vue -->
<template>
  <div class="container">
    <!-- Hiển thị trạng thái loading -->
    <div v-if="loading" class="loading">Đang tải dữ liệu...</div>

    <!-- Không có kết quả -->
    <div v-else-if="!loading && cart.products.length === 0" class="loading">
      Không tìm thấy sản phẩm nào.
    </div>

    <!-- Hiển thị danh sách sản phẩm -->
    <div v-else>
      <div class="grid">
        <div v-for="p in cart.products" :key="p.id" class="prod">
          <img :src="imageUrl(p.image)" :alt="p.name" />
          <div class="body">
            <div>{{ p.name }}</div>
            <div class="price">{{ currency(p.price) }}</div>
            <div style="display:flex;gap:8px">
              <router-link
                class="btn"
                :to="{ name:'product', params:{ id:p.id } }"
                style="background:#83c74a"
              >Xem</router-link>
              <button class="btn" @click="add(p)">Thêm giỏ</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 🔥 Phân trang -->
      <div class="pagination">
        <button 
          class="page-btn" 
          @click="changePage(cart.currentPage - 1)" 
          :disabled="cart.currentPage === 0"
        >
          ← Trước
        </button>
        
        <span class="page-info">
          Trang {{ cart.currentPage + 1 }} / {{ cart.totalPages }}
        </span>
        
        <button 
          class="page-btn" 
          @click="changePage(cart.currentPage + 1)" 
          :disabled="cart.currentPage >= cart.totalPages - 1"
        >
          Sau →
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useCartStore } from '@/stores/cart'

const props = defineProps({
  selectedCategoryId: {
    type: [Number, null],
    default: null
  }
})

const loading = ref(true)
const cart = useCartStore()

// Debug: Log categoryId khi thay đổi
watch(() => props.selectedCategoryId, (newId) => {
  console.log('🔍 CategoryId mới:', newId)
  cart.currentPage = 0 // Reset trang
  fetchProducts()
})

// Gọi API khi mount
onMounted(() => {
  console.log('🔍 CategoryId ban đầu:', props.selectedCategoryId)
  fetchProducts()
})

// Hàm tải sản phẩm
const fetchProducts = async () => {
  loading.value = true
  await cart.fetchProductsPage(cart.currentPage, props.selectedCategoryId)
  loading.value = false
}

// Hàm chuyển trang
const changePage = async (newPage) => {
  if (newPage >= 0 && newPage < cart.totalPages) {
    loading.value = true
    await cart.fetchProductsPage(newPage, props.selectedCategoryId)
    loading.value = false
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

// Hàm thêm giỏ hàng
const add = (product) => {
  cart.add(product, 1)
}

// Hàm định dạng giá tiền
const currency = (v) => Number(v).toLocaleString('vi-VN') + ' đ'

// Lấy đường dẫn ảnh
const imageUrl = (img) => '/images/' + img
</script>

<style scoped>
.container {
  padding: 20px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.loading {
  font-size: 18px;
  text-align: center;
  padding: 20px;
}

.prod {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
  transition: box-shadow 0.3s;
}

.prod:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.prod img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.body {
  padding: 12px;
}

.price {
  color: #83c74a;
  font-weight: bold;
  margin: 8px 0;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background: #55b450;
  color: white;
  text-decoration: none;
  display: inline-block;
  font-size: 14px;
  transition: opacity 0.2s;
}

.btn:hover {
  opacity: 0.9;
}

/* 🎨 Style phân trang */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-top: 24px;
  padding: 20px;
}

.page-btn {
  padding: 10px 20px;
  border: 1px solid #83c74a;
  background: white;
  color: #83c74a;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s;
}

.page-btn:hover:not(:disabled) {
  background: #83c74a;
  color: white;
}

.page-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}
</style>