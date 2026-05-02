package com.tribal.crafts.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer stock;
    private String image;
    private Boolean approved;
    private Long artisanId;
    private String artisanName;
    private Date createdAt;
    private Date updatedAt;

    public ProductResponse() {}

    public ProductResponse(Long id, String name, String description, BigDecimal price, 
                         String category, Integer stock, String image, Boolean approved, 
                         Long artisanId, String artisanName, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.image = image;
        this.approved = approved;
        this.artisanId = artisanId;
        this.artisanName = artisanName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Boolean getApproved() { return approved; }
    public void setApproved(Boolean approved) { this.approved = approved; }

    public Long getArtisanId() { return artisanId; }
    public void setArtisanId(Long artisanId) { this.artisanId = artisanId; }

    public String getArtisanName() { return artisanName; }
    public void setArtisanName(String artisanName) { this.artisanName = artisanName; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
