package com.tribal.crafts.controller;

import com.tribal.crafts.dto.ProductRequest;
import com.tribal.crafts.dto.ProductResponse;
import com.tribal.crafts.entity.Product;
import com.tribal.crafts.service.FileStorageService;
import com.tribal.crafts.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileStorageService fileStorageService;

    // ── GET endpoints ────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getApprovedProducts());
    }

    @GetMapping("/unapproved")
    public ResponseEntity<List<ProductResponse>> getUnapprovedProducts() {
        return ResponseEntity.ok(productService.getUnapprovedProducts());
    }

    @GetMapping("/artisan/{artisanId}")
    public ResponseEntity<List<ProductResponse>> getProductsByArtisan(@PathVariable Long artisanId) {
        return ResponseEntity.ok(productService.getProductsByArtisan(artisanId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductByIdResponse(id));
    }

    // ── Image upload (standalone) ─────────────────────────────────────────────

    /**
     * POST /api/products/upload-image
     * Accepts a single image file and returns its URL.
     * Clients call this first, then pass the URL into createProduct / updateProduct.
     */
    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "No file selected"));
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Only image files are allowed"));
        }

        String imageUrl = fileStorageService.storeFile(file);
        return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
    }

    // ── Multipart create (name, description, price, category, stock + file) ──

    /**
     * POST /api/products/with-image
     * Creates a product in one request: form fields + optional image file.
     */
    @PostMapping(value = "/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> createProductWithImage(
            @RequestParam("name")        String name,
            @RequestParam("description") String description,
            @RequestParam("price")       java.math.BigDecimal price,
            @RequestParam("category")    String category,
            @RequestParam("stock")       Integer stock,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            Authentication authentication) {

        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = fileStorageService.storeFile(imageFile);
        }

        ProductRequest request = new ProductRequest();
        request.setName(name);
        request.setDescription(description);
        request.setPrice(price);
        request.setCategory(category);
        request.setStock(stock);
        request.setImage(imageUrl);

        String username = authentication.getName();
        return ResponseEntity.ok(productService.createProduct(request, username));
    }

    // ── JSON create (legacy – still works for URL-based image) ───────────────

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProduct(
            @Valid @RequestBody ProductRequest productRequest,
            Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(productService.createProduct(productRequest, username));
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest productRequest) {
        Product updated = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(productService.convertToResponse(updated));
    }

    /** Multipart update – lets you swap the image when editing a product */
    @PutMapping(value = "/{id}/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> updateProductWithImage(
            @PathVariable Long id,
            @RequestParam("name")        String name,
            @RequestParam("description") String description,
            @RequestParam("price")       java.math.BigDecimal price,
            @RequestParam("category")    String category,
            @RequestParam("stock")       Integer stock,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = fileStorageService.storeFile(imageFile);
        }

        ProductRequest request = new ProductRequest();
        request.setName(name);
        request.setDescription(description);
        request.setPrice(price);
        request.setCategory(category);
        request.setStock(stock);
        request.setImage(imageUrl);   // null means "keep existing" (handled in service)

        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    // ── Delete / approve / reject ─────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ProductResponse> approveProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.approveProduct(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectProduct(@PathVariable Long id) {
        productService.rejectProduct(id);
        return ResponseEntity.ok().build();
    }
}
