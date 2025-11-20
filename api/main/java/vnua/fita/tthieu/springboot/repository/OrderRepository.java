package vnua.fita.tthieu.springboot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vnua.fita.tthieu.springboot.entity.Order;
import vnua.fita.tthieu.springboot.entity.User;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Tìm tất cả đơn hàng của một người dùng
    List<Order> findByUser(User user);

    // Tìm đơn hàng theo trạng thái
    List<Order> findByStatus(String status);
}

