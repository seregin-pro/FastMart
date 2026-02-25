package ru.zettatech.productservice.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zettatech.productservice.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {}