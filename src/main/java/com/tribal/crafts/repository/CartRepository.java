package com.tribal.crafts.repository;

import com.tribal.crafts.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCustomerId(Long customerId);
    List<Cart> findByCustomerIdAndProductId(Long customerId, Long productId);
}
