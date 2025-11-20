<template>
  <div class="admin-orders-container">
    <h2 class="title">Quản lý đơn hàng</h2>

    <!-- Bảng hiển thị danh sách đơn -->
    <table class="order-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Tên khách hàng</th>
          <th>SĐT</th>
          <th>Ngày đặt</th>
          <th>Ngày giao</th>
          <th>Chi tiết</th>
          <th>Giá (VNĐ)</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="o in orders" :key="o.id">
          <td>{{ o.id }}</td>
          <td>{{ o.customerName }}</td>
          <td>{{ o.phone }}</td>
          <td>{{ o.orderDate }}</td>
          <td>{{ o.deliveryDate }}</td>
          <td>{{ o.itemDetails }}</td>
          <td>{{ o.price.toLocaleString() }}</td>
          <td>
            <button class="edit-btn" @click="openEditForm(o)">Sửa</button>
            <button class="delete-btn" @click="deleteOrder(o.id)">Xóa</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Popup sửa -->
    <div v-if="showEditForm" class="popup-overlay" @click.self="closeForm">
      <div class="popup-content">
        <h3>Sửa thông tin đơn hàng</h3>
        <form @submit.prevent="updateOrder">
          <label>Tên khách hàng</label>
          <input v-model="selectedOrder.customerName" type="text" />

          <label>Số điện thoại</label>
          <input v-model="selectedOrder.phone" type="text" />

          <label>Ngày đặt</label>
          <input v-model="selectedOrder.orderDate" type="date" />

          <label>Ngày giao</label>
          <input v-model="selectedOrder.deliveryDate" type="date" />

          <label>Chi tiết hàng</label>
          <textarea v-model="selectedOrder.itemDetails" rows="3"></textarea>

          <label>Giá (VNĐ)</label>
          <input v-model="selectedOrder.price" type="number" />

          <div class="btn-group">
            <button type="submit" class="save-btn">Lưu</button>
            <button type="button" class="cancel-btn" @click="closeForm">Hủy</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ManageOrders",
  data() {
    return {
      orders: [],
      showEditForm: false,
      selectedOrder: null,
    };
  },
  methods: {
    async fetchOrders() {
      const res = await axios.get("/api/orders");
      this.orders = res.data;
    },
    openEditForm(order) {
      this.selectedOrder = { ...order };
      this.showEditForm = true;
    },
    closeForm() {
      this.showEditForm = false;
      this.selectedOrder = null;
    },
    async updateOrder() {
      await axios.put(
        `/api/orders/${this.selectedOrder.id}`,
        this.selectedOrder
      );
      this.closeForm();
      this.fetchOrders();
    },
    async deleteOrder(id) {
      if (confirm("Bạn có chắc muốn xóa đơn hàng này không?")) {
        await axios.delete(`/api/orders/${id}`);
        this.fetchOrders();
      }
    },
  },
  mounted() {
    this.fetchOrders();
  },
};
</script>

<style scoped>
.admin-orders-container {
  padding: 20px;
  background: #fff;
  border-radius: 10px;
}

.title {
  text-align: center;
  margin-bottom: 20px;
  color: #2c3e50;
}

.order-table {
  width: 100%;
  border-collapse: collapse;
}

.order-table th,
.order-table td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: center;
}

.order-table th {
  background-color: #f5f5f5;
}

.edit-btn,
.delete-btn {
  padding: 6px 12px;
  margin: 0 3px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.edit-btn {
  background-color: #3498db;
  color: #fff;
}

.delete-btn {
  background-color: #e74c3c;
  color: #fff;
}

/* Overlay popup */
.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

/* Popup form */
.popup-content {
  background: #fff;
  padding: 25px;
  border-radius: 10px;
  width: 400px;
  max-width: 90%;
}

.popup-content h3 {
  text-align: center;
  margin-bottom: 20px;
}

.popup-content label {
  display: block;
  margin: 8px 0 4px;
}

.popup-content input,
.popup-content textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.btn-group {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
}

.save-btn {
  background-color: #2ecc71;
  color: #fff;
  border: none;
  padding: 8px 15px;
  border-radius: 5px;
}

.cancel-btn {
  background-color: #bdc3c7;
  border: none;
  padding: 8px 15px;
  border-radius: 5px;
}
</style>
