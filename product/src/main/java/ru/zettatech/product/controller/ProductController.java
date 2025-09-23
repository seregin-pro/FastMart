package ru.zettatech.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zettatech.product.filter.ProductFilter;
import ru.zettatech.product.model.Product;
import ru.zettatech.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
class ProductController {

    @Autowired
    private ProductService service;
//
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "limit", required = false) Integer limit
    ) {
        var filter = new ProductFilter(
                page,
                limit
        );

        return ResponseEntity.ok(service.findAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Product> postProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> putProduct(@PathVariable Long id, @RequestBody Product product) {
        return (service.existsById(id))
                ? new ResponseEntity<>(service.save(product),
                HttpStatus.OK)
                : new ResponseEntity<>(service.save(product),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
