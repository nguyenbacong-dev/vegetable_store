<template>
  <div class="carousel card">
    <img :src="slides[idx]" alt="banner" />
    <div class="ctrl">
      <button @click="prev">‹</button>
      <button @click="next">›</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'

//ảnh banner
const slides = [
  '/images/banner/banner1.png',
  '/images/banner/banner2.png',
  '/images/banner/banner3.png'
]

const idx = ref(0)
let timer

const next = () => (idx.value = (idx.value + 1) % slides.length)
const prev = () => (idx.value = (idx.value - 1 + slides.length) % slides.length)

onMounted(() => {
  // Nếu chỉ có 1 ảnh thì không cần auto chuyển
  if (slides.length > 1) {
    timer = setInterval(next, 3500)
  }
})
onBeforeUnmount(() => clearInterval(timer))
</script>
