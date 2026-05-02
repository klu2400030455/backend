package com.tribal.crafts.controller;

import com.tribal.crafts.dto.ReviewRequest;
import com.tribal.crafts.dto.ReviewResponse;
import com.tribal.crafts.entity.Review;
import com.tribal.crafts.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByCustomer(userId));
    }

    @GetMapping("/artisan")
    public ResponseEntity<List<ReviewResponse>> getReviewsByArtisan(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(reviewService.getReviewsByArtisan(email));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> addReview(@Valid @RequestBody ReviewRequest reviewRequest, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(reviewService.addReview(reviewRequest, username));
    }
}
