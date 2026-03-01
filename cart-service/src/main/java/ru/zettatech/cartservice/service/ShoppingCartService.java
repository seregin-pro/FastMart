package ru.zettatech.cartservice.service;

import org.springframework.http.ResponseEntity;

import ru.zettatech.cartservice.request.dto.AddProductToCartRequest;
import ru.zettatech.cartservice.request.dto.CheckCartPredicate;
import ru.zettatech.cartservice.request.dto.CleanCartRequest;
import ru.zettatech.cartservice.request.dto.CreateShoppingCartRequest;
import ru.zettatech.cartservice.request.dto.DecreaseProductQtyRequest;
import ru.zettatech.cartservice.request.dto.GetCartItemRequest;
import ru.zettatech.cartservice.request.dto.RemoveProductFromCartRequest;

public interface ShoppingCartService {

	ResponseEntity<?> createShoppingCart(CreateShoppingCartRequest request);

	ResponseEntity<?> addProductToCart(AddProductToCartRequest request);

	ResponseEntity<?> removeCartItemFromCart(RemoveProductFromCartRequest request);

	ResponseEntity<?> checkCartPredicate(CheckCartPredicate request);

	ResponseEntity<?> decreaseProductQuantityFromCart(DecreaseProductQtyRequest request);

	ResponseEntity<?> getAllCartItems(GetCartItemRequest request);

	ResponseEntity<?> cleanCart(CleanCartRequest request);

	
}
