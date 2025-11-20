<template>
  <div class="payment-return">
    <div v-if="loading">🔄 Đang xử lý kết quả thanh toán...</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Swal from 'sweetalert2'

const router = useRouter()
const route = useRoute()

const loading = ref(true)

onMounted(() => {
  const responseCode = route.query.vnp_ResponseCode

  if (responseCode === '00') {
    //  Thanh toán thành công — hiện thông báo giống đăng nhập
    Swal.fire({
      icon: 'success',
      title: 'Thanh toán thành công!',
      text: 'Cảm ơn bạn đã mua hàng tại FamilyGreen 💚',
      showConfirmButton: false,
      timer: 2000
    })

    //  Sau 2 giây quay lại trang chủ
    setTimeout(() => {
      router.push('/')
    }, 2000)
  } else {
    //  Thanh toán thất bại
    Swal.fire({
      icon: 'error',
      title: 'Thanh toán thất bại!',
      text: `Mã lỗi: ${responseCode || 'Không xác định'}`,
      showConfirmButton: true
    }).then(() => {
      router.push('/')
    })
  }

  loading.value = false
})
</script>

<style scoped>
.payment-return {
  max-width: 600px;
  margin: 100px auto;
  text-align: center;
}
</style>
