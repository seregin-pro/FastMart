package ru.zettatech.productservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.zettatech.productservice.filter.CategoryFilter;
import ru.zettatech.productservice.model.Category;
import ru.zettatech.productservice.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    @CachePut(value = "categories", key = "#category.id")
    public Category save(Category category) {
        return repository.save(category);
    }

    public List<Category> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Page<Category> findAll(CategoryFilter filter) {
        Sort sort = Sort.by(
                Sort.Direction.fromString(filter.sortOrder() != null ? filter.sortOrder() : "asc"),
                filter.sortBy() != null ? filter.sortBy() : "name"
        );
        Pageable pageable = PageRequest.of(
                filter.pageNumber() != null ? filter.pageNumber() : 0,
                filter.pageSize() != null ? filter.pageSize() : 10,
                sort
        );
        return repository.findAll(pageable);
    }

    @Cacheable(value = "categories", key = "#id")
    public Category findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found category by id = " + id));
    }

    @CacheEvict(value = "categories", key = "#id")
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
