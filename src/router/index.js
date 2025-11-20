import { createRouter, createWebHistory } from 'vue-router'

// Các trang chính
import Home from '@/views/Home.vue'
import ProductDetail from '@/views/ProductDetail.vue'
import Checkout from '@/views/Checkout.vue'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'

//  Trang kết quả thanh toán VNPAY
import VNPAYReturn from '@/views/VNPAYReturn.vue'

// Thêm các file admin
import AdminDashboard from '@/views/admin/AdminDashboard.vue'
import ManageUsers from '@/views/admin/ManageUsers.vue'
import ManageProducts from '@/views/admin/ManageProducts.vue'
import ManageOrders from '@/views/admin/ManageOrders.vue'

//  Thêm trang tìm kiếm sản phẩm
import SearchPage from '@/views/SearchPage.vue'

//  Thêm trang lịch sử đơn hàng
import OrderHistory from '@/views/OrderHistory.vue'

const routes = [
  // Trang người dùng
  { path: '/', name: 'home', component: Home },
  { path: '/product/:id', name: 'product', component: ProductDetail, props: true },
  { path: '/checkout', name: 'checkout', component: Checkout },
  { path: '/login', name: 'login', component: Login },
  { path: '/register', name: 'register', component: Register },
  { path: '/orders/history', name: 'orderHistory', component: OrderHistory }, 

  //  Trang tìm kiếm sản phẩm
  { path: '/search', name: 'SearchPage', component: SearchPage },

    //  Trang hiển thị kết quả sau khi thanh toán VNPAY
  { path: '/vnpay-return', name: 'vnpay-return', component: VNPAYReturn },

  // Khu vực admin
  {
    path: '/admin',
    component: AdminDashboard,
    children: [
      { path: 'users', component: ManageUsers },
      { path: 'products', component: ManageProducts },
      { path: 'orders', component: ManageOrders }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

export default router
