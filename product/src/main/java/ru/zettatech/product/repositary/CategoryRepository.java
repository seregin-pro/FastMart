package ru.zettatech.product.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zettatech.product.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {}