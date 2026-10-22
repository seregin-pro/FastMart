package ru.zettatech.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.zettatech.productservice.model.Review;
import ru.zettatech.productservice.repository.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository repository;

    public Review create(Review review) {
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        return repository.save(review);
    }

    public Page<Review> findByProductId(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByProductIdOrderByCreatedAtDesc(productId, pageable);
    }

    public Double getAverageRating(Long productId) {
        return repository.findAverageRatingByProductId(productId);
    }

    public long getReviewCount(Long productId) {
        return repository.countByProductId(productId);
    }

    public List<Review> findAll() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
