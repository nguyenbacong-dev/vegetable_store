package vnua.fita.tthieu.springboot.controller;

import vnua.fita.tthieu.springboot.entity.OrderDetail;
import vnua.fita.tthieu.springboot.entity.Order;
import vnua.fita.tthieu.springboot.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-details")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    // Lấy chi tiết đơn hàng theo ID đơn hàng
    @GetMapping("/order/{orderId}")
    public List<OrderDetail> getDetailsByOrder(@PathVariable Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        return orderDetailService.getOrderDetailsByOrder(order);
    }

    // Thêm chi tiết đơn hàng
    @PostMapping
    public OrderDetail addOrderDetail(@RequestBody OrderDetail orderDetail) {
        return orderDetailService.addOrderDetail(orderDetail);
    }

    // Xóa chi tiết đơn hàng
    @DeleteMapping("/{id}")
    public void deleteOrderDetail(@PathVariable Long id) {
        orderDetailService.deleteOrderDetail(id);
    }
}
