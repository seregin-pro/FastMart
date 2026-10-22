package ru.zettatech.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zettatech.productservice.model.Review;
import ru.zettatech.productservice.service.ReviewService;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(
            @PathVariable Long productId,
            @RequestBody Review review) {
        review.setProductId(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(review));
    }

    @GetMapping
    public ResponseEntity<Page<Review>> getReviews(
            @PathVariable Long productId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.findByProductId(productId, page, size));
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getReviewSummary(@PathVariable Long productId) {
        Double avgRating = reviewService.getAverageRating(productId);
        long reviewCount = reviewService.getReviewCount(productId);
        return ResponseEntity.ok(new ReviewSummary(avgRating, reviewCount));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long productId,
            @PathVariable Long reviewId) {
        reviewService.deleteById(reviewId);
        return ResponseEntity.noContent().build();
    }

    public record ReviewSummary(Double averageRating, long reviewCount) {
    }
}
