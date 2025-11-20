package vnua.fita.tthieu.springboot.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vnua.fita.tthieu.springboot.entity.Order;
import vnua.fita.tthieu.springboot.entity.OrderDetail;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    // Lấy danh sách chi tiết đơn hàng theo mã đơn
    List<OrderDetail> findByOrder(Order order);
}
