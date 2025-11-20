import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import './style.css'

//  Cấu hình Axios mặc định
axios.defaults.baseURL = 'http://localhost:8088' // địa chỉ backend Spring Boot
axios.defaults.headers.common['Content-Type'] = 'application/json'

//  Tạo app Vue
const app = createApp(App)

//  Kích hoạt Pinia (quản lý state)
app.use(createPinia())

//  Kích hoạt Vue Router
app.use(router)

//  Gắn axios vào toàn app (dễ gọi ở mọi component)
app.config.globalProperties.$axios = axios


//  Mount ứng dụng vào DOM
app.mount('#app')
