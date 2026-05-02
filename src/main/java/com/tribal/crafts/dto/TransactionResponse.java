package com.tribal.crafts.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionResponse {

    private Long id;
    private Long orderId;
    private Long customerId;
    private String customerName;
    private BigDecimal amount;
    private String status;
    private String paymentMethod;
    private Date createdAt;

    public TransactionResponse() {}

    public TransactionResponse(Long id, Long orderId, Long customerId, String customerName, 
                              BigDecimal amount, String status, String paymentMethod, Date createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
