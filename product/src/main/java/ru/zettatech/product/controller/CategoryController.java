package ru.zettatech.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zettatech.product.model.Category;
import ru.zettatech.product.model.Product;
import ru.zettatech.product.repositary.CategoryRepository;
import ru.zettatech.product.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
class CategoryController {

    @Autowired
    private CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Category> postCategory(@RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> putCategory(@PathVariable Long id, @RequestBody Category category) {
        return (service.existsById(id))
                ? new ResponseEntity<>(service.save(category),
                HttpStatus.OK)
                : new ResponseEntity<>(service.save(category),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {

        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}