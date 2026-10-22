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
import ru.zettatech.productservice.filter.ManufacturerFilter;
import ru.zettatech.productservice.model.Manufacturer;
import ru.zettatech.productservice.repository.ManufacturerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManufacturerService {

    private final ManufacturerRepository repository;

    @CachePut(value = "manufacturers", key = "#manufacturer.id")
    public Manufacturer save(Manufacturer manufacturer) {
        return repository.save(manufacturer);
    }

    public Page<Manufacturer> findAll(ManufacturerFilter filter) {
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
