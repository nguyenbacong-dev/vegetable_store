<template>
  <div>
    <!--  Nếu là trang admin, chỉ hiển thị nội dung admin -->
    <router-view v-if="$route.path.startsWith('/admin')" />

    <!--  Còn lại: layout trang người dùng -->
    <div v-else>
      <TopBar />
      <HeaderBar />
      <MainNav />

      <!--  Hiển thị layout trang chủ -->
      <div v-if="$route.path === '/'">
        <div class="container layout">
          <!-- Danh mục bên trái -->
          <CategoryMenu @selectCategory="setCategory" />

          <!-- Sản phẩm bên phải -->
          <div>
            <HeroCarousel />
            <section class="section">
              <div class="chip">RAU CỦ QUẢ HỮU CƠ</div>
              <ProductCard :selectedCategoryId="selectedCategoryId" />
            </section>
          </div>
        </div>
      </div>

      <!--  Các trang khác (login, register, checkout, product...) -->
      <router-view v-else />

      <FooterBar />
      <CartDrawer />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'

import TopBar from '@/components/TopBar.vue'
import HeaderBar from '@/components/HeaderBar.vue'
import MainNav from '@/components/MainNav.vue'
import CategoryMenu from '@/components/CategoryMenu.vue'
import HeroCarousel from '@/components/HeroCarousel.vue'
import CartDrawer from '@/components/CartDrawer.vue'
import FooterBar from '@/components/FooterBar.vue'
import ProductCard from '@/components/ProductCard.vue'

const route = useRoute()

// Lưu danh mục được chọn
const selectedCategoryId = ref(null)
const setCategory = (id) => {
  selectedCategoryId.value = id
}
</script>
