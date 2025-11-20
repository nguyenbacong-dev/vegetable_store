<template>
  <div class="nav">
    <div class="container wrap">
      <router-link to="/">TRANG CHỦ</router-link>
      <a href="#">KHUYẾN MÃI</a>
      <a href="#">SẢN PHẨM</a>
      <a href="#">SẢN PHẨM NỔI BẬT</a>
      <a href="#">GIỚI THIỆU</a>
      <a href="#">TIN TỨC</a>
      <a href="#">LIÊN HỆ</a>
              <!--Link lịch sử đơn hàng -->
            <router-link to="/orders/history" class="btn" 
                 style="background:#2b7a0b; display:flex;align-items:center;gap:6px">
          📜 Lịch sử
           </router-link>
    </div>
  </div>
</template>
