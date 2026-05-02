package com.tribal.crafts.dto;

import java.math.BigDecimal;
import java.util.Date;

public class OrderResponse {

    private Long id;
    private Integer quantity;
    private String status;
    private Long customerId;
    private String customerName;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Date createdAt;
    private Date updatedAt;

    public OrderResponse() {}

    public OrderResponse(Long id, Integer quantity, String status, Long customerId, String customerName,
                        Long productId, String productName, BigDecimal price, Date createdAt, Date updatedAt) {
        this.id = id;
        this.quantity = quantity;
        this.status = status;
        this.customerId = customerId;
        this.customerName = customerName;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
