package ru.zettatech.product.repositary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zettatech.product.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> { }