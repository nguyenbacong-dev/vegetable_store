<template>
  <div class="container">
    <!-- Tiêu đề -->
    <h2 v-if="keyword" class="result-title">Kết quả tìm kiếm cho "{{ keyword }}"</h2>

    <!-- Trạng thái loading -->
    <div v-if="loading" class="loading">Đang tải dữ liệu...</div>

    <!-- Không có kết quả -->
    <div v-else-if="!loading && products.length === 0" class="loading">
      Không tìm thấy sản phẩm nào.
    </div>

    <!-- Hiển thị danh sách sản phẩm -->
    <div v-else>
      <div class="grid">
        <div v-for="p in products" :key="p.id" class="prod">
          <img :src="imageUrl(p.image)" :alt="p.name" />
          <div class="body">
            <div class="name">{{ p.name }}</div>
            <div class="price">{{ currency(p.price) }}</div>
            <div style="display:flex;gap:8px;justify-content:center;margin-top:8px">
              <router-link
                class="btn"
                :to="{ name: 'product', params: { id: p.id } }"
                style="background:#83c74a"
              >Xem</router-link>
              <button class="btn" @click="add(p)" style="background:#5ca82f">Thêm giỏ</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 🔥 Phân trang -->
      <div class="pagination">
        <button 
          class="page-btn" 
          @click="changePage(currentPage - 1)" 
          :disabled="currentPage === 0"
        >
          ← Trước
        </button>
        
        <span class="page-info">
          Trang {{ currentPage + 1 }} / {{ totalPages }}
        </span>
        
        <button 
          class="page-btn" 
          @click="changePage(currentPage + 1)" 
          :disabled="currentPage >= totalPages - 1"
        >
          Sau →
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import axios from 'axios'
import { useRoute } from 'vue-router'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const keyword = ref(route.query.q || '') // đọc ?q=...
const products = ref([])
const loading = ref(false)
const cart = useCartStore()

// 🔥 Thêm các biến phân trang
const currentPage = ref(0)
const pageSize = ref(12)
const totalPages = ref(0)

// Nếu bạn dùng proxy trong vite (ví dụ /api => http://localhost:8088),
// thì dùng đường dẫn tương đối '/api/products/search...'
// Nếu không có proxy, đổi thành http://localhost:8088/api/products/search
const SEARCH_API_BASE = '/api/products/search' // hoặc 'http://localhost:8088/api/products/search'

async function fetchProductsByKeyword(k, page = 0) {
  if (!k || !k.toString().trim()) {
    products.value = []
    return
  }
  loading.value = true
  try {
    const res = await axios.get(`${SEARCH_API_BASE}`, {
      params: {
        keyword: encodeURIComponent(k),
        page,
        size: pageSize.value
      }
    })
    products.value = res.data.content || []
    totalPages.value = res.data.totalPages || 1
  } catch (err) {
    console.error('Lỗi khi gọi API tìm kiếm:', err)
    products.value = []
  } finally {
    loading.value = false
  }
}

// 🔥 Hàm chuyển trang
const changePage = (newPage) => {
  if (newPage >= 0 && newPage < totalPages.value) {
    currentPage.value = newPage
    fetchProductsByKeyword(keyword.value, newPage)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

// watch khi query q thay đổi (ví dụ: search box push route)
watch(
  () => route.query.q,
  (newQ) => {
    keyword.value = newQ || ''
    currentPage.value = 0 // Reset trang khi tìm kiếm mới
    fetchProductsByKeyword(keyword.value, currentPage.value)
  }
)

// gọi khi mount lần đầu (nếu mở url /search?q=...)
onMounted(() => {
  keyword.value = route.query.q || ''
  if (keyword.value) fetchProductsByKeyword(keyword.value, currentPage.value)
})

// hàm thêm giỏ
const add = (product) => {
  cart.add(product, 1)
}

// format tiền
const currency = (v) => {
  if (!v && v !== 0) return '0 đ'
  return new Intl.NumberFormat('vi-VN').format(v) + ' đ'
}

// đường dẫn ảnh: nếu backend để ảnh ở http://localhost:8088/images/<name>
// và bạn không có proxy, đổi thành full URL
const imageUrl = (img) => {
  if (!img) return '/images/no-image.png'
  // nếu front-end có thư mục public/images, dùng '/images/<img>'
  // nếu backend serve ảnh, dùng 'http://localhost:8088/images/<img>'
  // chỉnh theo cách bạn lưu ảnh:
  return '/images/' + img
}
</script>

<style scoped>
.container { padding: 20px; background:#f7fbf6 }
.result-title { text-align:left; color:#2f7a2a; margin-bottom:16px }
.loading { text-align:center; padding:24px; color:#666 }
.grid {
  display:grid;
  grid-template-columns: repeat(auto-fill, minmax(240px,1fr));
  gap:20px;
  align-items:start;
}
.prod {
  background:#fff;
  border-radius:12px;
  padding:12px;
  text-align:center;
  box-shadow: 0 2px 6px rgba(0,0,0,0.06);
}
.prod img { width:100%; height:180px; object-fit:cover; border-radius:10px }
.body { padding-top:10px }
.name { font-size:16px; margin-bottom:6px; color:#333 }
.price { color:#3fa235; font-weight:700; margin-bottom:8px }
.btn {
  padding:8px 14px;
  border-radius:8px;
  color:white;
  border:none;
  cursor:pointer;
  text-decoration:none;
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