package com.tribal.crafts.service;

import com.tribal.crafts.dto.OrderRequest;
import com.tribal.crafts.dto.OrderResponse;
import com.tribal.crafts.entity.Orders;
import com.tribal.crafts.entity.Product;
import com.tribal.crafts.entity.User;
import com.tribal.crafts.exception.ResourceNotFoundException;
import com.tribal.crafts.repository.OrdersRepository;
import com.tribal.crafts.repository.ProductRepository;
import com.tribal.crafts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public OrderResponse placeOrder(OrderRequest request, String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Order must contain at least one item");
        }

        // Process each item in the order
        Orders lastOrder = null;
        for (OrderRequest.OrderItem item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + item.getProductId()));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // Update product stock
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            // Create order for this item
            Orders order = new Orders();
            order.setCustomer(customer);
            order.setProduct(product);
            order.setQuantity(item.getQuantity());
            order.setStatus("PLACED");

            lastOrder = ordersRepository.save(order);
        }

        // Map to DTO while still inside the transaction so lazy proxies are accessible
        return toOrderResponse(lastOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomer(String email) {
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return ordersRepository.findByCustomerId(customer.getId())
                .stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Orders order = ordersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return toOrderResponse(order);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId, String customerEmail) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("You are not authorized to cancel this order");
        }

        if (!"PLACED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Only PLACED orders can be cancelled");
        }

        // Update status
        order.setStatus("CANCELLED");
        
        // Restore stock
        Product product = order.getProduct();
        if (product != null) {
            product.setStock(product.getStock() + order.getQuantity());
            productRepository.save(product);
        }

        Orders savedOrder = ordersRepository.save(order);
        return toOrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return ordersRepository.findAll()
                .stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByArtisan(String artisanEmail) {
        User artisan = userRepository.findByEmail(artisanEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Artisan not found"));
        return ordersRepository.findByProductArtisanId(artisan.getId())
                .stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        order.setStatus(status.toUpperCase());
        Orders savedOrder = ordersRepository.save(order);
        return toOrderResponse(savedOrder);
    }

    // ---------------------------------------------------------------
    // Private helper: maps Orders entity → OrderResponse DTO.
    // Must be called while a Hibernate session is open (inside @Transactional)
    // so that LAZY-loaded customer/product proxies can be initialised safely.
    // ---------------------------------------------------------------
    private OrderResponse toOrderResponse(Orders order) {
        User customer = order.getCustomer();
        Product product = order.getProduct();
        return new OrderResponse(
                order.getId(),
                order.getQuantity(),
                order.getStatus(),
                customer != null ? customer.getId()   : null,
                customer != null ? customer.getName() : "Unknown",
                product  != null ? product.getId()    : null,
                product  != null ? product.getName()  : "Unknown",
                product  != null ? product.getPrice() : null,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}

