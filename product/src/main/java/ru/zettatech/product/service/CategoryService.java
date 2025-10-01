package ru.zettatech.product.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.zettatech.product.filter.CategoryFilter;
import ru.zettatech.product.filter.ProductFilter;
import ru.zettatech.product.model.Category;
import ru.zettatech.product.model.Product;
import ru.zettatech.product.repositary.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    public Category save(Category category) {
        return repository.save(category);
    }

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Page<Category> findAll(CategoryFilter filter) {
        int pageNumber = filter.pageNumber() != null ? filter.pageNumber() : 0;
        int pageSize = filter.pageSize() != null ? filter.pageSize() : 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(pageable);
    }

    public Category findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found category by id = " + id));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
