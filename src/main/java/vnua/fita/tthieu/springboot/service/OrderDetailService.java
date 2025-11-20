package vnua.fita.tthieu.springboot.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vnua.fita.tthieu.springboot.entity.Order;
import vnua.fita.tthieu.springboot.entity.OrderDetail;
import vnua.fita.tthieu.springboot.repository.OrderDetailRepository;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getOrderDetailsByOrder(Order order) {
        return orderDetailRepository.findByOrder(order);
    }

    public OrderDetail addOrderDetail(OrderDetail detail) {
        return orderDetailRepository.save(detail);
    }

    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }
}
