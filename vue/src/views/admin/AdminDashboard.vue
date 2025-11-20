<template>
  <div class="admin-layout">
    <!-- Sidebar -->
    <aside class="sidebar">
      <h2>🌿 Admin Panel</h2>

      <!-- Thông tin admin -->
      <div class="admin-info">
        <p>👋 Xin chào, <strong>{{ user.fullname }}</strong></p>
        <button class="logout-btn" @click="logout">Đăng xuất</button>
      </div>

      <!-- 📋 Menu quản lý -->
      <nav>
        <router-link to="/admin/users">👥 Quản lý User</router-link>
        <router-link to="/admin/products">🥦 Quản lý Sản phẩm</router-link>
        <router-link to="/admin/orders">📦 Quản lý Đơn hàng</router-link>
      </nav>
    </aside>

    <!-- Nội dung chính -->
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import Swal from 'sweetalert2'

const router = useRouter()
const userStore = useUserStore()
const user = computed(() => userStore.user)

const logout = () => {
  Swal.fire({
    title: 'Bạn có chắc muốn đăng xuất?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Đăng xuất',
    cancelButtonText: 'Hủy'
  }).then((result) => {
    if (result.isConfirmed) {
      userStore.logout()
      Swal.fire({
        icon: 'success',
        title: 'Đăng xuất thành công!',
        showConfirmButton: false,
        timer: 1200
      })
      router.push('/login')
    }
  })
}
</script>


<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
  background: #f5f6fa;
}

/* Sidebar */
.sidebar {
  width: 240px;
  background: #2c3e50;
  color: white;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.sidebar h2 {
  font-size: 18px;
  margin-bottom: 10px;
  color: #00b894;
}

/* Thông tin admin */
.admin-info {
  background: rgba(255, 255, 255, 0.1);
  padding: 10px;
  border-radius: 8px;
  font-size: 14px;
}

.logout-btn {
  margin-top: 10px;
  background: #d63031;
  border: none;
  color: white;
  padding: 6px 10px;
  border-radius: 4px;
  cursor: pointer;
  width: 100%;
}
.logout-btn:hover {
  background: #e74c3c;
}

/* Menu link */
.sidebar a {
  color: white;
  text-decoration: none;
  padding: 6px 0;
  display: block;
  border-radius: 4px;
  transition: background 0.2s;
}
.sidebar a:hover {
  background: rgba(255, 255, 255, 0.1);
}
.sidebar a.router-link-exact-active {
  background: #00b894;
  font-weight: bold;
}

/* Nội dung chính */
.main-content {
  flex: 1;
  padding: 25px;
}
</style>
