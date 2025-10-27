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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) {
        var filter = new ProductFilter(
                pageNumber,
                pageSize
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
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
