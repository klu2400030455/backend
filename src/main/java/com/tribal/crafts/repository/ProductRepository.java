package com.tribal.crafts.repository;

import com.tribal.crafts.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByArtisanId(Long artisanId);
    List<Product> findByCategory(String category);
}
