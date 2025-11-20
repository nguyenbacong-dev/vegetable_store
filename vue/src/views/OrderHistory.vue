<template>
  <div class="container">
    <h2>Lịch sử đơn hàng</h2>

    <!-- Loading -->
    <div v-if="loading" class="loading">Đang tải dữ liệu...</div>

    <!-- Không có đơn hàng -->
    <div v-else-if="orders.length === 0" class="loading">
      Bạn chưa có đơn hàng nào.
    </div>

    <!-- Danh sách đơn hàng -->
    <div v-else class="order-list">
      <div v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-header">
          <span>Mã đơn: <strong>#{{ order.id }}</strong></span>
          <span>Ngày: {{ formatDate(order.orderDate) }}</span>
          <span>Trạng thái: {{ order.status }}</span>
          <span>Tổng: {{ currency(order.total) }}</span>
        </div>

        <!-- Chi tiết sản phẩm -->
        <div class="order-details">
          <div
            v-for="item in order.orderDetails"
            :key="item.id"
            class="item"
          >
            <img
              :src="imageUrl(item.product?.image)"
              :alt="item.product?.name"
            />
            <div class="info">
              <div class="name">{{ item.product?.name }}</div>
              <div>Số lượng: {{ item.quantity }}</div>
              <div>Giá: {{ currency(item.price) }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useUserStore } from '@/stores/user'
import axios from 'axios'

const userStore = useUserStore()
const user = computed(() => userStore.user)

const orders = ref([])
const loading = ref(false)


// Hàm lấy lịch sử đơn hàng
const fetchOrders = async () => {
  if (!user.value) return
  loading.value = true
  try {
    const res = await axios.get(`/api/orders/user/${user.value.id}`)
    orders.value = res.data
  } catch (err) {
    console.error('Lỗi khi tải lịch sử đơn hàng:', err)
    orders.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchOrders()
})

//  Format tiền tệ
const currency = (v) => {
  if (!v && v !== 0) return '0 đ'
  return new Intl.NumberFormat('vi-VN').format(v) + ' đ'
}

// Format ngày
const formatDate = (d) => {
  if (!d) return ''
  return new Date(d).toLocaleDateString('vi-VN', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  })
}

// Đường dẫn ảnh
const imageUrl = (img) => {
  if (!img) return '/images/no-image.png'
  return `/images/${img}`
}
</script>

<style scoped>
.container {
  padding: 20px;
  background: #f7fbf6;
}

.loading {
  text-align: center;
  padding: 24px;
  color: #666;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-card {
  background: #fff;
  padding: 16px;
  border-radius: 12px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
}

.order-header {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  margin-bottom: 12px;
  font-weight: 500;
  color: #333;
}

.order-details {
  display: flex;
  flex-direction: column;
  gap: 10px;
  border-top: 1px solid #eee;
  padding-top: 10px;
}

.item {
  display: flex;
  gap: 12px;
  align-items: center;
}

.item img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 8px;
}

.info div {
  font-size: 14px;
}

.name {
  font-weight: 600;
}
</style>
