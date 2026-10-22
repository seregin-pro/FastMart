package ru.zettatech.productservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.zettatech.productservice.filter.ProductFilter;
import ru.zettatech.productservice.model.Product;
import ru.zettatech.productservice.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository repository;

    public Page<Product> findAll(ProductFilter filter) {
        Pageable pageable = createPageable(filter);

        if (filter.query() != null && !filter.query().isBlank()) {
            return repository.searchWithFilters(
                    filter.query(),
                    filter.minPrice(),
                    filter.maxPrice(),
                    filter.categoryId(),
                    filter.manufacturerId(),
                    pageable
            );
        }

        return repository.searchWithFilters(
                null,
                filter.minPrice(),
                filter.maxPrice(),
                filter.categoryId(),
                filter.manufacturerId(),
                pageable
        );
    }

    public Page<Product> search(String query, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber != null ? pageNumber : 0,
                pageSize != null ? pageSize : 10
        );
        return repository.searchByQuery(query, pageable);
    }

    @Cacheable(value = "products", key = "#id")
    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    @CachePut(value = "products", key = "#product.id")
    public Product save(Product product) {
        return repository.save(product);
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Optional<Product> findByIdOptional(Long id) {
        return repository.findById(id);
    }

    private Pageable createPageable(ProductFilter filter) {
        Sort sort = Sort.by(
                Sort.Direction.fromString(filter.sortOrder()),
                filter.sortBy()
        );
        return PageRequest.of(filter.pageNumber(), filter.pageSize(), sort);
    }
}
