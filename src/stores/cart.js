// File: src/stores/cart.js
import { defineStore } from 'pinia'
import axios from 'axios'

export const useCartStore = defineStore('cart', {
  state: () => ({
    open: false,
    items: [], // {id, name, price, img, qty}
    products: [], // thêm mảng lưu sản phẩm
    currentPage: 0,
    pageSize: 12,
    totalPages: 0
  }),

  getters: {
    count: (s) => s.items.reduce((t, i) => t + i.qty, 0),
    subtotal: (s) => s.items.reduce((t, i) => t + i.price * i.qty, 0)
  },

  actions: {
    toggle(val) {
      this.open = typeof val === 'boolean' ? val : !this.open
    },

    add(p, qty = 1) {
      const found = this.items.find(i => i.id === p.id)
      if (found) found.qty += qty
      else this.items.push({ id: p.id, name: p.name, price: p.price, image: p.image, qty })
      this.open = true
    },

    remove(id) { this.items = this.items.filter(i => i.id !== id) },

    update(id, qty) {
      const f = this.items.find(i => i.id === id)
      if (f) f.qty = Math.max(1, qty)
    },

    clear() { this.items = [] },

    // ✅ ===> Hàm lấy sản phẩm có phân trang và lọc category
    async fetchProductsPage(page = 0, categoryId = null) {
      try {
        this.currentPage = page
        const params = { 
          page: this.currentPage, 
          size: this.pageSize 
        }
        let url = '/api/products/page'
        if (categoryId !== null) {
          url = `/api/products/category/${categoryId}`
        }

        console.log('🔍 URL gọi API:', url) // Debug URL
        console.log('🔍 Params:', params) // Debug params
        console.log('🔍 CategoryId:', categoryId) // Debug categoryId

        const res = await axios.get(url, {
          params
        })

        console.log('📦 Dữ liệu API nhận được:', res.data) // Debug response đầy đủ

        this.products = res.data.content || []
        this.totalPages = res.data.totalPages || 1
      } catch (error) {
        console.error('Lỗi phân trang:', error)
        console.error('Chi tiết lỗi:', error.response?.data)
        this.products = [] // Reset nếu lỗi
        this.totalPages = 1
      }
    }
  }
})