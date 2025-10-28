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
import ru.zettatech.product.filter.ManufacturerFilter;
import ru.zettatech.product.model.Category;
import ru.zettatech.product.model.Manufacturer;
import ru.zettatech.product.repositary.ManufacturerRepository;

import java.util.List;

@Service
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository repository;

    @CachePut(value = "manufacturers", key = "#manufacturer.id")
    public Manufacturer save(Manufacturer manufacturer) {
        return repository.save(manufacturer);
    }

    public Page<Manufacturer> findAll(ManufacturerFilter filter) {
        int pageNumber = filter.pageNumber() != null ? filter.pageNumber() : 0;
        int pageSize = filter.pageSize() != null ? filter.pageSize() : 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(pageable);
    }

    @Cacheable(value = "manufacturers", key = "#id")
    public Manufacturer findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found manufacturer by id = " + id));
    }

    @CacheEvict(value = "manufacturers", key = "#id")
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}