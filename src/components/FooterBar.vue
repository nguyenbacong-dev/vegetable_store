<template>
  <footer class="footer">
    <div class="container footer-wrap">
      <!-- Cột 1: Giới thiệu -->
      <div class="footer-col">
        <h3 class="footer-title">FamilyGreen</h3>
        <p>Chuyên cung cấp rau củ quả sạch, hữu cơ – tươi ngon mỗi ngày đến mọi gia đình Việt.</p>
        <p>Địa chỉ: Số 26 Ngõ 133 Trâu Quỳ, Hà Nội</p>
        <p>Hotline: 038 6772 205</p>
      </div>

      <!-- Cột 2: Hỗ trợ khách hàng -->
      <div class="footer-col">
        <h4 class="footer-subtitle">Hỗ trợ khách hàng</h4>
        <ul>
          <li><a href="#">Chính sách giao hàng</a></li>
          <li><a href="#">Chính sách đổi trả</a></li>
          <li><a href="#">Bảo mật thông tin</a></li>
          <li><a href="#">Liên hệ</a></li>
        </ul>
      </div>

      <!-- Cột 3: Kết nối -->
      <div class="footer-col">
        <h4 class="footer-subtitle">Kết nối với chúng tôi</h4>
        <div class="socials">
          <a href="#"><i class="fab fa-facebook"></i> Facebook</a>
          <a href="#"><i class="fab fa-instagram"></i> Instagram</a>
          <a href="#"><i class="fab fa-youtube"></i> YouTube</a>
        </div>
      </div>
    </div>

    <div class="footer-bottom">
      © {{ year }} <strong>FamilyGreen</strong> — Cửa hàng rau củ quả sạch vì sức khỏe cộng đồng.
    </div>
  </footer>
</template>

<script setup>
const year = new Date().getFullYear()
</script>

<style scoped>
.footer {
  background: #f5f9f3;
  color: #333;
  font-size: 14px;
  margin-top: 40px;
  border-top: 2px solid #59a51c;
}

.footer-wrap {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 24px;
  padding: 32px 0;
}

.footer-title {
  font-size: 20px;
  font-weight: 700;
  color: #59a51c;
  margin-bottom: 12px;
}

.footer-subtitle {
  font-size: 16px;
  font-weight: 600;
  color: #4c8c1a;
  margin-bottom: 10px;
}

.footer-col ul {
  list-style: none;
  padding: 0;
}

.footer-col ul li {
  margin-bottom: 6px;
}

.footer-col a {
  text-decoration: none;
  color: #333;
  transition: color 0.2s;
}

.footer-col a:hover {
  color: #59a51c;
}

.socials a {
  display: block;
  margin-bottom: 6px;
}

.footer-bottom {
  text-align: center;
  padding: 14px 0;
  background: #e9f3e1;
  font-size: 13px;
}
</style>
