<template>
  <div class="admin-container">
    <h2 class="title">Quản lý người dùng</h2>

    <!-- Nút thêm -->
    <div class="action-bar">
      <button class="add-btn" @click="openAddForm">➕ Thêm người dùng</button>
    </div>

    <!-- Danh sách người dùng -->
    <table class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Họ tên</th>
          <th>Email</th>
          <th>SĐT</th>
          <th>Địa chỉ</th>
          <th>Vai trò</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="u in users" :key="u.id">
          <td>{{ u.id }}</td>
          <td>{{ u.fullname }}</td>
          <td>{{ u.email }}</td>
          <td>{{ u.phone }}</td>
          <td>{{ u.adress }}</td>
          <td>{{ u.role }}</td>
          <td>
            <button class="edit-btn" @click="openEditForm(u)">Sửa</button>
            <button class="delete-btn" @click="deleteUser(u.id)">Xóa</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Popup Sửa -->
    <div v-if="showEditForm" class="popup-overlay" @click.self="closeForm">
      <div class="popup-content">
        <h3>Sửa thông tin người dùng</h3>
        <form @submit.prevent="updateUser">
          <label>Họ tên</label>
          <input v-model="selectedUser.fullname" type="text" placeholder="Nhập họ tên..." />

          <label>Email</label>
          <input v-model="selectedUser.email" type="email" placeholder="Nhập email..." />

          <label>Số điện thoại</label>
          <input v-model="selectedUser.phone" type="text" placeholder="Nhập SĐT..." />

          <label>Địa chỉ</label>
          <input v-model="selectedUser.adress" type="text" placeholder="Nhập địa chỉ..." />

          <label>Mật khẩu</label>
          <input
            v-model="selectedUser.password"
            type="password"
            readonly
            class="readonly-input"
            placeholder="Không thể chỉnh sửa mật khẩu"
          />

          <div class="btn-group">
            <button type="submit" class="save-btn">💾 Lưu</button>
            <button type="button" class="cancel-btn" @click="closeForm">✖ Hủy</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Popup Thêm -->
    <div v-if="showAddForm" class="popup-overlay" @click.self="closeForm">
      <div class="popup-content">
        <h3>Thêm người dùng mới</h3>
        <form @submit.prevent="createUser">
          <label>Họ tên</label>
          <input v-model="newUser.fullname" type="text" placeholder="Nhập họ tên..." />

          <label>Email</label>
          <input v-model="newUser.email" type="email" placeholder="Nhập email..." />

          <label>Số điện thoại</label>
          <input v-model="newUser.phone" type="text" placeholder="Nhập SĐT..." />

          <label>Địa chỉ</label>
          <input v-model="newUser.adress" type="text" placeholder="Nhập địa chỉ..." />

          <label>Mật khẩu</label>
          <input v-model="newUser.password" type="password" placeholder="Nhập mật khẩu..." />

          <div class="btn-group">
            <button type="submit" class="save-btn">➕ Thêm</button>
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
  name: "ManageUsers",
  data() {
    return {
      users: [],
      showEditForm: false,
      showAddForm: false,
      selectedUser: null,
      newUser: {
        fullname: "",
        email: "",
        phone: "",
        adress: "",
        password: "",
        role: "",
      },
    };
  },
  methods: {
    // Lấy danh sách người dùng
    async fetchUsers() {
      const res = await axios.get("/api/auth/users");
      this.users = res.data;
    },

    // Mở popup thêm người dùng
    openAddForm() {
      this.showAddForm = true;
      this.showEditForm = false;
      this.newUser = {
        fullname: "",
        email: "",
        phone: "",
        adress: "",
        password: "",
        role: "",
      };
    },

    // Mở popup sửa
    openEditForm(user) {
      this.selectedUser = { ...user, password: "********" }; // ẩn mật khẩu thật
      this.showEditForm = true;
      this.showAddForm = false;
    },

    // Đóng tất cả popup
    closeForm() {
      this.showEditForm = false;
      this.showAddForm = false;
      this.selectedUser = null;
    },

    // Thêm người dùng mới
    async createUser() {
      const { fullname, email, phone, adress, password, role } = this.newUser;
      if (!fullname || !email || !phone || !adress || !password) {
        return Swal.fire({
          icon: "warning",
          title: "Vui lòng nhập đầy đủ thông tin!",
        });
      }

      await axios.post("/api/auth/register", this.newUser);
      this.closeForm();
      this.fetchUsers();

      Swal.fire({
        icon: "success",
        title: "Thêm người dùng thành công!",
        timer: 1500,
        showConfirmButton: false,
      });
    },

    // Cập nhật người dùng
    async updateUser() {
      const { fullname, email, phone, adress, role } = this.selectedUser;
      if (!fullname || !email || !phone || !adress || !role) {
        return Swal.fire({
          icon: "warning",
          title: "Vui lòng nhập đầy đủ thông tin!",
        });
      }

      await axios.put(`/api/auth/users/${this.selectedUser.id}`, {
        fullname,
        email,
        phone,
        adress,
        role,
      });
      this.closeForm();
      this.fetchUsers();

      Swal.fire({
        icon: "success",
        title: "Cập nhật thành công!",
        timer: 1500,
        showConfirmButton: false,
      });
    },

    // Xóa người dùng
    async deleteUser(id) {
      const confirmDelete = await Swal.fire({
        icon: "warning",
        title: "Bạn có chắc muốn xóa người dùng này không?",
        showCancelButton: true,
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy",
      });

      if (confirmDelete.isConfirmed) {
        await axios.delete(`/api/auth/users/${id}`);
        this.fetchUsers();

        Swal.fire({
          icon: "success",
          title: "Đã xóa thành công!",
          timer: 1500,
          showConfirmButton: false,
        });
      }
    },
  },
  mounted() {
    this.fetchUsers();
  },
};
</script>