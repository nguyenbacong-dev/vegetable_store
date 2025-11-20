<template>
  <div class="header">
    <div class="container wrap">
      <div class="logo">
        <img src="/images/logo.png" width="40" height="32" alt="">
        <div>FamilyGreen</div>
      </div>

      <div class="search">
        <input v-model="q" placeholder="Tìm sản phẩm" @keyup.enter="goSearch" />
        <button class="btn" @click="goSearch">Tìm</button>
      </div>

      <div style="display:flex;align-items:center;gap:12px">
<!-- Nếu chưa đăng nhập -->
        <template v-if="!user">
          <router-link to="/login">Đăng nhập</router-link> /
          <router-link to="/register">Đăng ký</router-link>
        </template>

        <!-- Nếu đã đăng nhập -->
        <template v-else>
          <span style="font-weight: bold;">👋 Hi, {{ user.fullname }}</span>
          <a href="#" @click.prevent="logout" style="color:red;font-weight:500">Đăng xuất</a>
        </template>

        <a href="#" class="btn" style="display:flex;align-items:center;gap:8px;background:#59a51c"
           @click.prevent="cart.toggle(true)">
          🛒 Giỏ hàng <span class="badge">{{ cart.count }}</span>
        </a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'

const router = useRouter()
const cart = useCartStore()
const userStore = useUserStore()
const user = computed(() => userStore.user)
const q = ref('')

//Xử lý tìm kiếm
const goSearch = () => {
  if (q.value.trim()) {
    router.push({ path: '/search', query: { q: q.value } })
  }
}

//Đăng xuất
const logout = () => {
  Swal.fire({
    title: 'Bạn có chắc muốn đăng xuất?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Đăng xuất',
    cancelButtonText: 'Hủy'
  }).then((result) => {
    if (result.isConfirmed) {
      userStore.logout() //dùng store để logout
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
