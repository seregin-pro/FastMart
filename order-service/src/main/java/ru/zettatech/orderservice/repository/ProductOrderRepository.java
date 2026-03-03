package ru.zettatech.orderservice.repository;

import ru.zettatech.orderservice.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long>{

	List<ProductOrder> findByCartId(Long cartId);


}
