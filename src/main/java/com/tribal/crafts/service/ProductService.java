package com.tribal.crafts.service;

import com.tribal.crafts.dto.ProductRequest;
import com.tribal.crafts.dto.ProductResponse;
import com.tribal.crafts.entity.Product;
import com.tribal.crafts.entity.User;
import com.tribal.crafts.exception.ResourceNotFoundException;
import com.tribal.crafts.repository.OrdersRepository;
import com.tribal.crafts.repository.ProductRepository;
import com.tribal.crafts.repository.ReviewRepository;
import com.tribal.crafts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<ProductResponse> getApprovedProducts() {
        return productRepository.findAll()
                .stream()
                .filter(Product::getApproved)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getUnapprovedProducts() {
        return productRepository.findAll()
                .stream()
                .filter(p -> !p.getApproved())
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse approveProduct(Long id) {
        Product product = getProductById(id);
        product.setApproved(true);
        return convertToResponse(productRepository.save(product));
    }

    @Transactional
    public void rejectProduct(Long id) {
        Product product = getProductById(id);
        product.setApproved(false);
        productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public ProductResponse getProductByIdResponse(Long id) {
        Product product = getProductById(id);
        return convertToResponse(product);
    }

    @Transactional
    public Product createProduct(ProductRequest request, String artisanEmail) {
        User artisan = userRepository.findByEmail(artisanEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Artisan not found"));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setStock(request.getStock());
        product.setImage(request.getImage());
        product.setArtisan(artisan);
        product.setApproved(false); // Default false, needs consultant approval

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, ProductRequest request) {
        Product product = getProductById(id);
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setStock(request.getStock());
        // Reset approval status on any update
        product.setApproved(false);
        if (request.getImage() != null) {
            product.setImage(request.getImage());
        }

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        
        // Delete related reviews and orders to avoid foreign key violations
        reviewRepository.deleteByProductId(id);
        ordersRepository.deleteByProductId(id);
        
        productRepository.delete(product);
    }

    public List<ProductResponse> getProductsByArtisan(Long artisanId) {
        return productRepository.findByArtisanId(artisanId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private static final String BACKEND_BASE_URL = "http://localhost:8080";

    public ProductResponse convertToResponse(Product product) {
        // Artisan is LAZY — must be called within an active transaction (@Transactional on class)
        User artisan = product.getArtisan();
        String artisanName = (artisan != null) ? artisan.getName() : "Unknown";
        Long artisanId   = (artisan != null) ? artisan.getId()   : 0L;

        // If the stored image is a local relative path (starts with /uploads/),
        // prepend the backend base URL so the frontend can load it directly.
        String image = product.getImage();
        if (image != null && image.startsWith("/uploads/")) {
            image = BACKEND_BASE_URL + image;
        }

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getStock(),
                image,
                product.getApproved(),
                artisanId,
                artisanName,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}

