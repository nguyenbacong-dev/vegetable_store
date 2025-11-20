package vnua.fita.tthieu.springboot.controller;

import vnua.fita.tthieu.springboot.dto.OrderRequest;
import vnua.fita.tthieu.springboot.entity.Order;
import vnua.fita.tthieu.springboot.entity.OrderDetail;
import vnua.fita.tthieu.springboot.entity.Product;
import vnua.fita.tthieu.springboot.entity.User;
import vnua.fita.tthieu.springboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Lấy tất cả đơn hàng
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Lấy đơn hàng theo ID
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // Lấy đơn hàng theo khách hàng
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return orderService.getOrdersByUser(user);
    }

    // Tạo đơn hàng mới
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    // Cập nhật trạng thái đơn hàng
    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestParam String status) {
        return orderService.updateStatus(id, status);
    }

    // Xóa đơn hàng
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
    
    @PostMapping("/create-with-details")
    public Order createOrderWithDetails(@RequestBody OrderRequest request) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(request.getStatus() != null ? request.getStatus() : "Đang xử lý");
        order.setTotal(request.getTotal());

        // Nếu có userId thì gán
        if (request.getUserId() != null) {
            User user = new User();
            user.setId(request.getUserId());
            order.setUser(user);
        }

        // Tạo danh sách OrderDetail
        List<OrderDetail> details = request.getItems().stream().map(item -> {
            OrderDetail od = new OrderDetail();

            Product p = new Product();
            p.setId(item.getProductId()); // chỉ cần set id là đủ, Hibernate sẽ map

            od.setProduct(p);
            od.setQuantity(item.getQuantity());
            od.setPrice(item.getPrice());
            od.setOrder(order);

            return od;
        }).toList();

        order.setOrderDetails(details);

        return orderService.createOrder(order);
    }

}
