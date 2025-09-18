package ru.zettatech.product.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zettatech.product.model.Manufacturer;
import ru.zettatech.product.repositary.ManufacturerRepository;

import java.util.List;

@Service
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository repository;

    public Manufacturer save(Manufacturer manufacturer) {
        return repository.save(manufacturer);
    }

    public List<Manufacturer> findAll() {
        return repository.findAll();
    }

    public Manufacturer findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found manufacturer by id = " + id));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}