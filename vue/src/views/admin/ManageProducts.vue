<template>
  <div class="admin-container">
    <h2 class="title">Quản lý sản phẩm</h2>

    <!-- Nút thêm -->
    <div class="action-bar">
      <button class="add-btn" @click="openAddForm">➕ Thêm sản phẩm</button>
    </div>

    <!-- Bảng sản phẩm -->
    <table class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Tên</th>
          <th>Giá</th>
          <th>Mô tả</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="p in products" :key="p.id">
          <td>{{ p.id }}</td>
          <td>{{ p.name }}</td>
          <td>{{ p.price.toLocaleString() }}₫</td>
          <td>{{ p.description }}</td>
          <td>
            <button class="edit-btn" @click="openEditForm(p)">Sửa</button>
            <button class="delete-btn" @click="deleteProduct(p.id)">Xóa</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Popup Thêm sản phẩm -->
    <div v-if="showAddForm" class="popup-overlay" @click.self="closeForm">
      <div class="popup-content">
        <h3>Thêm sản phẩm mới</h3>
        <form @submit.prevent="createProduct">
          <label>Tên sản phẩm</label>
          <input v-model="newProduct.name" type="text" placeholder="Nhập tên sản phẩm..." />

          <label>Giá</label>
          <input v-model.number="newProduct.price" type="number" placeholder="Nhập giá..." />

          <label>Mô tả</label>
          <textarea v-model="newProduct.description" placeholder="Nhập mô tả..."></textarea>

          <div class="btn-group">
            <button type="submit" class="save-btn"> Thêm</button>
            <button type="button" class="cancel-btn" @click="closeForm"> Hủy</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Popup Sửa sản phẩm -->
    <div v-if="showEditForm" class="popup-overlay" @click.self="closeForm">
      <div class="popup-content">
        <h3>Sửa sản phẩm</h3>
        <form @submit.prevent="updateProduct">
          <label>Tên sản phẩm</label>
          <input v-model="editProduct.name" type="text" placeholder="Nhập tên sản phẩm..." />

          <label>Giá</label>
          <input v-model.number="editProduct.price" type="number" placeholder="Nhập giá..." />

          <label>Mô tả</label>
          <textarea v-model="editProduct.description" placeholder="Nhập mô tả..."></textarea>

          <div class="btn-group">
            <button type="submit" class="save-btn">💾 Lưu</button>
            <button type="button" class="cancel-btn" @click="closeForm">✖ Hủy</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Swal from "sweetalert2";
import "./table.css";

export default {
  name: "ManageProducts",
  data() {
    return {
      products: [],
      showAddForm: false,
      showEditForm: false,
      newProduct: {
        name: "",
        price: 0,
        description: "",
      },
      editProduct: { 
        id: null,
        name: "",
        price: 0,
        description: "",
      },
    };
  },
  methods: {
   
    async fetchProducts() {
      try {
        const res = await axios.get("/api/products");
        this.products = res.data;
      } catch (err) {
        console.error("Lỗi khi tải danh sách sản phẩm:", err);
      }
    },


    openAddForm() {
      this.showAddForm = true;
      this.showEditForm = false;
      this.newProduct = { name: "", price: 0, description: "" };
    },

   
    openEditForm(p) {
      this.editProduct = { ...p };
      this.showEditForm = true;
      this.showAddForm = false;
    },

   
    closeForm() {
      this.showAddForm = false;
      this.showEditForm = false;
    },

    
    async createProduct() {
      const { name, price, description } = this.newProduct;
      if (!name || !price || !description) {
        return Swal.fire({ icon: "warning", title: "Vui lòng nhập đầy đủ thông tin!" });
      }

      try {
        await axios.post("/api/products", this.newProduct);
        this.closeForm();
        this.fetchProducts();

        Swal.fire({
          icon: "success",
          title: "Thêm sản phẩm thành công!",
          timer: 1500,
          showConfirmButton: false,
        });
      } catch (err) {
        console.error("Lỗi khi thêm sản phẩm:", err);
      }
    },

   
    async updateProduct() {
      const { id, name, price, description } = this.editProduct;
      if (!name || !price || !description) {
        return Swal.fire({ icon: "warning", title: "Vui lòng nhập đầy đủ thông tin!" });
      }

      try {
        await axios.put(`/api/products/${id}`, this.editProduct);
        this.closeForm();
        this.fetchProducts();

        Swal.fire({
          icon: "success",
          title: "Cập nhật sản phẩm thành công!",
          timer: 1500,
          showConfirmButton: false,
        });
      } catch (err) {
        console.error("Lỗi khi cập nhật sản phẩm:", err);
      }
    },

    async deleteProduct(id) {
      const confirmDelete = await Swal.fire({
        icon: "warning",
        title: "Bạn có chắc muốn xóa sản phẩm này không?",
        showCancelButton: true,
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy",
      });

      if (confirmDelete.isConfirmed) {
        try {
          await axios.delete(`/api/products/${id}`);
          this.fetchProducts();

          Swal.fire({
            icon: "success",
            title: "Đã xóa thành công!",
            timer: 1500,
            showConfirmButton: false,
          });
        } catch (err) {
          console.error("Lỗi khi xóa sản phẩm:", err);
        }
      }
    },
  },
  mounted() {
    this.fetchProducts();
  },
};
</script>
