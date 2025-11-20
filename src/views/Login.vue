<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2>Đăng nhập</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>Email</label>
          <input type="email" v-model="email" placeholder="Nhập email"  />
        </div>

        <div class="form-group">
          <label>Mật khẩu</label>
          <input type="password" v-model="password" placeholder="Nhập mật khẩu"  />
        </div>

        <button type="submit" class="btn">Đăng nhập</button>

        <p class="switch-text">
          Chưa có tài khoản?
          <router-link to="/register">Đăng ký ngay</router-link>
        </p>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import Swal from 'sweetalert2'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const email = ref('')
const password = ref('')
const router = useRouter()


const handleLogin = async () => {
  if (!email.value || !password.value) {
    Swal.fire({
      icon: 'warning',
      title: 'Vui lòng nhập đầy đủ thông tin!',
      showConfirmButton: true
    })
    return
  }

  try {
    //  Gọi API login
    const res = await axios.post('/api/auth/login', {
      email: email.value,
      password: password.value
    })

    //  Backend trả về: token, email, fullname, roles
    const { token, email: userEmail, fullname, roles, id } = res.data

    // Lưu token & user info vào localStorage
    const userData = { token, email: userEmail, fullname, roles ,id}
    localStorage.setItem('user', JSON.stringify(userData))

    // Cập nhật store Pinia
    userStore.setUser(userData)

    // Set token mặc định cho axios (để gọi các API cần xác thực)
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`

    // Thông báo thành công
    Swal.fire({
      icon: 'success',
      title: 'Đăng nhập thành công!',
      showConfirmButton: false,
      timer: 1500
    })

if (roles.includes('ROLE_ADMIN')) {
  router.push('/admin')
} else {
  router.push('/')
}

  } catch (err) {
    //  Nếu đăng nhập thất bại
    Swal.fire({
      icon: 'error',
      title: err.response?.data || 'Email hoặc mật khẩu không đúng!',
      showConfirmButton: true
    })
  }
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
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
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
