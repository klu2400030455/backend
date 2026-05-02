package com.tribal.crafts.dto;

import java.time.LocalDateTime;

public class ReviewResponse {
    private Long id;
    private Integer rating;
    private String comment;
    private Long productId;
    private String productName;
    private Long customerId;
    private String customerName;
    private LocalDateTime createdAt;

    public ReviewResponse(Long id, Integer rating, String comment, Long productId, String productName, Long customerId, String customerName) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.productId = productId;
        this.productName = productName;
        this.customerId = customerId;
        this.customerName = customerName;
        this.createdAt = LocalDateTime.now(); // Defaulting to now if not in entity
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
