<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2>Đăng ký tài khoản</h2>

      <form @submit.prevent="onSubmit">
        <!-- Họ và tên -->
        <div class="form-group">
          <label>Họ và tên</label>
          <input type="text" v-model="user.fullname" placeholder="Nhập họ tên" required />
        </div>

        <!-- Email -->
        <div class="form-group">
          <label>Email</label>
          <input type="email" v-model="user.email" placeholder="Nhập email" required />
        </div>

        <!-- Mật khẩu -->
        <div class="form-group">
          <label>Mật khẩu</label>
          <input type="password" v-model="user.password" placeholder="Nhập mật khẩu" required />
        </div>

        <!-- Xác nhận mật khẩu -->
        <div class="form-group">
          <label>Xác nhận mật khẩu</label>
          <input
            type="password"
            v-model="confirmPassword"
            placeholder="Nhập lại mật khẩu"
            required
          />
        </div>

        <!-- Số điện thoại -->
        <div class="form-group">
          <label>Số điện thoại</label>
          <input
            type="tel"
            v-model="user.phone"
            placeholder="Nhập số điện thoại"
            pattern="[0-9]{10,11}"
            required
          />
        </div>

        <!-- Địa chỉ -->
        <div class="form-group">
          <label>Địa chỉ</label>
          <input v-model="user.adress" placeholder="Nhập địa chỉ của bạn" required />
        </div>

        <!-- Nút đăng ký -->
        <button type="submit" class="btn">Đăng ký</button>

        <p class="switch-text">
          Đã có tài khoản?
          <router-link to="/login">Đăng nhập ngay</router-link>
        </p>

      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'

const router = useRouter()

// Biến lưu thông tin người dùng
const user = ref({
  fullname: '',
  email: '',
  password: '',
  phone: '',
  adress: ''
})

const confirmPassword = ref('')
const message = ref('')
const success = ref(false)

//  Kiểm tra dữ liệu form
const handleRegister = () => {
  if (user.value.password !== confirmPassword.value) {
    Swal.fire({
      icon: 'error',
      title: 'Mật khẩu xác nhận không khớp!',
      showConfirmButton: true
    })
    return false
  }
  return true
}

//  Gọi API đăng ký người dùng
const registerUser = async () => {
  try {
    await axios.post('/api/auth/register', user.value)

    //  Nếu thành công:
    Swal.fire({
      icon: 'success',
      title: 'Đăng ký thành công!',
      showConfirmButton: false,
      timer: 1500
    })

    // Chuyển hướng sau 1.5s
    setTimeout(() => router.push('/login'), 1500)
  } catch (err) {
    //  Nếu thất bại:
    Swal.fire({
      icon: 'error',
      title: err.response?.data?.message || 'Đăng ký thất bại!',
      showConfirmButton: true
    })
  }
}

// Hàm tổng — kiểm tra rồi gọi API
const onSubmit = async () => {
  const valid = handleRegister()
  if (!valid) return
  await registerUser()
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  background-color: #f5f9f5;
}

.auth-card {
  background: white;
  padding: 30px;
  border-radius: 16px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  width: 400px;
}

h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #2b7a0b;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  font-weight: 500;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.btn {
  width: 100%;
  background-color: #2b7a0b;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.btn:hover {
  background-color: #3ea312;
}

.switch-text {
  text-align: center;
  margin-top: 15px;
}

.switch-text a {
  color: #2b7a0b;
  font-weight: 600;
}
</style>
