package ru.zettatech.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zettatech.product.filter.ManufacturerFilter;
import ru.zettatech.product.filter.ProductFilter;
import ru.zettatech.product.model.Category;
import ru.zettatech.product.model.Manufacturer;
import ru.zettatech.product.model.Product;
import ru.zettatech.product.repositary.ManufacturerRepository;
import ru.zettatech.product.service.ManufacturerService;

import java.util.List;

@RestController
@RequestMapping("/manufacturers")
public class ManufacturerController {

    @Autowired
    private ManufacturerService service;

    public ManufacturerController(ManufacturerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<Manufacturer>> getManufacturers(
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) {
        var filter = new ManufacturerFilter(
                pageNumber,
                pageSize
        );

        return ResponseEntity.ok(service.findAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manufacturer> getManufacturerById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Manufacturer> postManufacturer(@RequestBody Manufacturer manufacturer) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(manufacturer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manufacturer> putManufacturer(
            @PathVariable Long id,
            @RequestBody Manufacturer manufacturer
    ) {
        return (service.existsById(id))
                ? new ResponseEntity<>(service.save(manufacturer), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable Long id) {

        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
