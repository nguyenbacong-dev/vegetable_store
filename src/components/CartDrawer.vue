<template>
  <aside class="drawer" :class="{ open: cart.open }" @keydown.esc="cart.toggle(false)">
    <div class="head">
      <b>Giỏ hàng ({{ cart.count }})</b>
      <button class="btn" style="background:#ef4444" @click="cart.toggle(false)">Đóng</button>
    </div>

    <div class="body">
      <div v-if="!cart.items.length" style="color:#6b7280">Giỏ hàng còn trống.</div>

      <div v-for="i in cart.items" :key="i.id" class="card" style="display:flex;gap:10px;padding:8px">
        <img :src="imageUrl(i.image)" alt="" style="width:64px;height:64px;object-fit:cover;border-radius:8px" />
        <div style="flex:1">
          <div style="font-weight:600">{{ i.name }}</div>
          <div style="display:flex;align-items:center;gap:8px;margin-top:6px">
            <input type="number" min="1" :value="i.qty" @input="cart.update(i.id, +$event.target.value)" style="width:64px" />
            <div style="margin-left:auto">{{ currency(i.price*i.qty) }}</div>
          </div>
        </div>
        <button class="btn" style="background:#ef4444" @click="cart.remove(i.id)">Xóa</button>
      </div>
    </div>

    <div class="foot" v-if="cart.items.length">
      <div style="display:flex;justify-content:space-between;margin-bottom:10px">
        <span>Tạm tính</span>
        <b>{{ currency(cart.subtotal) }}</b>
      </div>
      <router-link class="btn" style="width:100%;text-align:center" to="/checkout" @click="cart.toggle(false)">
        Thanh toán
      </router-link>
    </div>
  </aside>
</template>

<script setup>
import { useCartStore } from '@/stores/cart'
const cart = useCartStore()
const currency = (v) => v.toLocaleString('vi-VN') + ' đ'
const imageUrl = (img) => '/images/' + img
</script>
