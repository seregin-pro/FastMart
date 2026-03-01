package ru.zettatech.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.zettatech.cartservice.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	CartItem findByCartIdAndProductId(Long cartId, Long productId);

	void deleteByCartIdAndProductId(Long cartId, Long productId);

}
