package com.vinylstore.order.service;

import com.vinylstore.auth.model.User;
import com.vinylstore.auth.repository.UserRepository;
import com.vinylstore.order.dto.OrderRequest;
import com.vinylstore.order.dto.OrderResponse;
import com.vinylstore.order.model.Order;
import com.vinylstore.order.repository.OrderRepository;
import com.vinylstore.product.model.Product;
import com.vinylstore.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return mapToResponse(order);
    }
    
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUser_Id(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public List<OrderResponse> getOrdersByEstado(String estado) {
        return orderRepository.findByEstado(estado).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public OrderResponse createOrder(OrderRequest request, Long userId) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Order order = new Order();
        order.setProduct(product);
        order.setUser(user);
        order.setCantidad(request.getCantidad());
        order.setPrecioUnitario(request.getPrecioUnitario());
        order.setTotal(request.getPrecioUnitario().multiply(BigDecimal.valueOf(request.getCantidad())));
        order.setEstado(request.getEstado() != null ? request.getEstado() : "PENDIENTE");
        
        order = orderRepository.save(order);
        return mapToResponse(order);
    }
    
    public OrderResponse updateOrder(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        if (request.getProductId() != null) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            order.setProduct(product);
        }
        
        if (request.getCantidad() != null) {
            order.setCantidad(request.getCantidad());
        }
        
        if (request.getPrecioUnitario() != null) {
            order.setPrecioUnitario(request.getPrecioUnitario());
        }
        
        if (request.getCantidad() != null && request.getPrecioUnitario() != null) {
            order.setTotal(request.getPrecioUnitario().multiply(BigDecimal.valueOf(request.getCantidad())));
        } else if (request.getCantidad() != null) {
            order.setTotal(order.getPrecioUnitario().multiply(BigDecimal.valueOf(request.getCantidad())));
        } else if (request.getPrecioUnitario() != null) {
            order.setTotal(request.getPrecioUnitario().multiply(BigDecimal.valueOf(order.getCantidad())));
        }
        
        if (request.getEstado() != null) {
            order.setEstado(request.getEstado());
        }
        
        order = orderRepository.save(order);
        return mapToResponse(order);
    }
    
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado");
        }
        orderRepository.deleteById(id);
    }
    
    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setProductId(order.getProduct().getId());
        response.setProductTitle(order.getProduct().getTitle());
        response.setUserId(order.getUser().getId());
        response.setUserEmail(order.getUser().getEmail());
        response.setCantidad(order.getCantidad());
        response.setPrecioUnitario(order.getPrecioUnitario());
        response.setTotal(order.getTotal());
        response.setFecha(order.getFecha());
        response.setEstado(order.getEstado());
        return response;
    }
}

