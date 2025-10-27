package ru.zettatech.product.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.zettatech.product.filter.ProductFilter;
import ru.zettatech.product.model.Product;
import ru.zettatech.product.repositary.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @CachePut(value = "products", key = "#product.id")
    public Product save(Product product) {
        return repository.save(product);
    }

    public Page<Product> findAll(ProductFilter filter) {
        int pageNumber = filter.pageNumber() != null ? filter.pageNumber() : 0;
        int pageSize = filter.pageSize() != null ? filter.pageSize() : 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(pageable);
    }

    @Cacheable(value = "products", key = "#id")
    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found product by id = " + id));
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
