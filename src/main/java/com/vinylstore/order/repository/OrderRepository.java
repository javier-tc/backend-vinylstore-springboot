package com.vinylstore.order.repository;

import com.vinylstore.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_Id(Long userId);
    List<Order> findByProduct_Id(Long productId);
    List<Order> findByEstado(String estado);
}

