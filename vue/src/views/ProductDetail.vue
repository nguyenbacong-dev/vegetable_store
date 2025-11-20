<template>
  <div class="container" style="margin-top:24px">
    <!-- Khi đang tải -->
    <div v-if="loading" class="loading">Đang tải sản phẩm...</div>

    <!-- Khi có dữ liệu -->
    <div
      v-else-if="product"
      class="card"
      style="display:grid;grid-template-columns:420px 1fr;gap:16px;padding:16px"
    >
      <img
        :src="imageUrl(product.image)"
        :alt="product.name"
        style="width:100%;height:340px;object-fit:cover;border-radius:12px"
      />
      <div>
        <h2 style="margin:0 0 8px">{{ product.name }}</h2>
        <div class="price" style="font-size:20px">{{ currency(product.price) }}</div>
        <p style="color:#6b7280">
          Sản phẩm tươi, đạt tiêu chuẩn an toàn. Giao nhanh trong 2 giờ (10km).
        </p>

        <div style="display:flex;gap:10px;align-items:center;margin:16px 0">
          <input type="number" min="1" v-model.number="qty" style="width:80px" />
          <button class="btn" @click="add">Thêm giỏ</button>
        </div>

        <div style="font-weight:600">Mô tả</div>
        <p style="color:#6b7280">{{ product.description || 'Không có mô tả' }}</p>
      </div>
    </div>

    <!-- Khi lỗi hoặc không tìm thấy -->
    <div v-else style="text-align:center;margin-top:40px">
      <h3>Không tìm thấy sản phẩm</h3>
      <router-link to="/" class="btn" style="margin-top:12px;background:#83c74a">
        Quay lại
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const product = ref(null)
const loading = ref(true)
const qty = ref(1)
const cart = useCartStore()

onMounted(async () => {
  const id = route.params.id
  try {
    const res = await axios.get(`/api/products/${id}`, { withCredentials: true })
    product.value = res.data
  } catch (err) {
    console.error('❌ Lỗi tải sản phẩm:', err)
  } finally {
    loading.value = false
  }
})

const add = () => {
  if (product.value) {
    cart.add(product.value, qty.value)
  }
}

const currency = (v) => v?.toLocaleString('vi-VN') + ' đ'
const imageUrl = (img) => '/images/' + img
</script>

<style scoped>
.loading {
  text-align: center;
  padding: 30px;
  font-size: 18px;
}
</style>
