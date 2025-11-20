<template>
  <div class="checkout-container">
    <h2 class="checkout-title">Thanh toán đơn hàng</h2>

    <div class="checkout-grid">
      <!-- Form thông tin nhận hàng -->
      <section class="checkout-form">
        <h3>Thông tin nhận hàng {{ user.id }}</h3>
        <div class="form-grid">
          <input type="text" placeholder="Họ và tên" v-model.trim="form.name" />
          <input type="text" placeholder="Số điện thoại" v-model.trim="form.phone" />
          <input type="email" placeholder="Email" v-model.trim="form.email" />
          <input
            type="text"
            placeholder="Địa chỉ nhận hàng"
            v-model.trim="form.address"
            class="full-width"
          />
        </div>

        <!-- Phương thức thanh toán -->
        <div class="payment-section">
          <h3>Phương thức thanh toán</h3>
          <label class="payment-option">
            <input type="radio" value="cod" v-model="form.payment" />
            <span>💵 Tiền mặt khi nhận hàng (COD)</span>
          </label>

          <label class="payment-option">
            <input type="radio" value="vnpay" v-model="form.payment" />
            <span>💳 Thanh toán qua VNPAY (QR / Thẻ ngân hàng)</span>
          </label>

          <transition name="fade">
            <div v-if="form.payment === 'vnpay'" class="vnpay-info">
              <p>
                Bạn sẽ được chuyển đến trang thanh toán VNPAY để quét mã QR hoặc chọn ngân hàng thanh toán.
              </p>
              <p class="note">
                (Hiện tại đang chạy ở môi trường <strong>Sandbox</strong> để test demo)
              </p>
            </div>
          </transition>
        </div>
      </section>

      <!-- Tóm tắt đơn hàng -->
      <aside class="checkout-summary">
        <h3>Đơn hàng ({{ cart.count }})</h3>

        <div v-for="i in cart.items" :key="i.id" class="summary-item">
          <span>{{ i.name }} × {{ i.qty }}</span>
          <strong>{{ currency(i.price * i.qty) }}</strong>
        </div>

        <hr />

        <div class="summary-total">
          <span>Tổng cộng:</span>
          <strong>{{ currency(cart.subtotal) }}</strong>
        </div>

        <button class="btn-order" @click="placeOrder" :disabled="loading">
          <span v-if="loading">Đang xử lý...</span>
          <span v-else>
            {{ form.payment === 'vnpay' ? 'Thanh toán qua VNPAY' : 'Đặt hàng ngay' }}
          </span>
        </button>
      </aside>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from 'vue'

import { useCartStore } from '@/stores/cart'
import axios from 'axios'
import { useUserStore } from '@/stores/user'

const cart = useCartStore()
const loading = ref(false)
const userStore = useUserStore()
const user = computed(() => userStore.user)


const form = reactive({
  name: '',
  phone: '',
  email: '',
  address: '',
  payment: ''
})

const currency = (v) => v.toLocaleString('vi-VN') + ' ₫'

const placeOrder = async () => {
  if (!cart.items.length) return alert('🛒 Giỏ hàng trống!')
  if (!form.name || !form.phone || !form.address)
    return alert('⚠️ Vui lòng điền đầy đủ thông tin nhận hàng!')
  if (!form.payment)
    return alert('⚠️ Vui lòng chọn phương thức thanh toán!')

  loading.value = true

  try {
    // Dữ liệu đơn hàng gửi sang backend
    const orderData = {
      userId: user.value.id,
      status: form.payment === 'vnpay' ? 'Thanh toán thành công' : 'Đang xử lý',
      total: cart.subtotal,
      items: cart.items.map(i => ({
        productId: i.id,
        quantity: i.qty,
        price: i.price
      }))
    }

    // Nếu chọn COD
    if (form.payment === 'cod') {
      await axios.post('/api/orders/create-with-details', orderData)
      alert('Đặt hàng thành công! Cảm ơn bạn đã mua hàng 🌱')
      cart.clear()
      return
    }

    // 💳 Nếu chọn VNPAY
    if (form.payment === 'vnpay') {
      // 1️Tạo đơn hàng trước
      await axios.post('/api/orders/create-with-details', orderData)

      // 2 Gửi yêu cầu tạo thanh toán
      const orderId = Date.now().toString()
      const res = await axios.post('/api/payment/create', {
        amount: cart.subtotal,
        orderInfo: `Thanh toán đơn hàng #${orderId}`,
        bankCode: 'NCB'
      })

      const data = res.data
      console.log('Kết quả trả về từ server:', data)

      // 3️ Nếu có link thanh toán thì chuyển hướng
      if (data.paymentUrl) {
        window.location.href = data.paymentUrl
      } else {
        alert('Backend không trả về link thanh toán!')
      }
    }
  } catch (err) {
    console.error(' Lỗi khi gọi API:', err)
    alert('Không thể kết nối tới server hoặc backend chưa phản hồi.')
  } finally {
    loading.value = false
  }
}
</script>


<style scoped>

.checkout-container {
  max-width: 1100px;
  margin: 50px auto;
  padding: 0 20px;
}
.checkout-title {
  font-size: 28px;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 24px;
  text-align: center;
}
.checkout-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 28px;
}
.checkout-form,
.checkout-summary {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
}
h3 {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #333;
}
.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}
input {
  padding: 10px 14px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 15px;
  transition: all 0.2s;
}
input:focus {
  border-color: #2ecc71;
  box-shadow: 0 0 0 2px rgba(46, 204, 113, 0.2);
  outline: none;
}
.full-width {
  grid-column: 1 / 3;
}
.payment-section {
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #eee;
}
.payment-option {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
  font-size: 15px;
  cursor: pointer;
}
.payment-option input {
  accent-color: #28a745;
}
.vnpay-info {
  background: #f8f9fa;
  padding: 12px;
  border-radius: 8px;
  margin-top: 10px;
  font-size: 14px;
  color: #444;
}
.vnpay-info .note {
  color: #777;
  font-size: 13px;
  margin-top: 4px;
}
.summary-item {
  display: flex;
  justify-content: space-between;
  margin: 8px 0;
}
.summary-total {
  display: flex;
  justify-content: space-between;
  font-size: 18px;
  margin: 20px 0;
  font-weight: 600;
}
.btn-order {
  width: 100%;
  padding: 14px;
  background: linear-gradient(90deg, #27ae60, #2ecc71);
  color: #fff;
  font-weight: 600;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.2s, background 0.2s;
}
.btn-order:hover {
  transform: scale(1.02);
  background: linear-gradient(90deg, #219150, #29b764);
}
.fade-enter-active,
.fade-leave-active {
  transition: all 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
