package com.tribal.crafts.service;

import com.tribal.crafts.dto.ReviewRequest;
import com.tribal.crafts.dto.ReviewResponse;
import com.tribal.crafts.entity.Product;
import com.tribal.crafts.entity.Review;
import com.tribal.crafts.entity.User;
import com.tribal.crafts.exception.ResourceNotFoundException;
import com.tribal.crafts.repository.ProductRepository;
import com.tribal.crafts.repository.ReviewRepository;
import com.tribal.crafts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public ReviewResponse addReview(ReviewRequest request, String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Review review = new Review();
        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        return convertToResponse(reviewRepository.save(review));
    }

    public List<ReviewResponse> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getReviewsByCustomer(Long customerId) {
        return reviewRepository.findByCustomerId(customerId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getReviewsByArtisan(String artisanEmail) {
        User artisan = userRepository.findByEmail(artisanEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Artisan not found"));
        return reviewRepository.findByProductArtisanId(artisan.getId())
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private ReviewResponse convertToResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getProduct().getId(),
                review.getProduct().getName(),
                review.getCustomer().getId(),
                review.getCustomer().getName()
        );
    }
}
