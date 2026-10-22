package ru.zettatech.cartservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.zettatech.cartservice.entity.CartItem;
import ru.zettatech.cartservice.entity.ShoppingCart;
import ru.zettatech.cartservice.repository.CartItemRepository;
import ru.zettatech.cartservice.repository.ShoppingCartRepository;
import ru.zettatech.cartservice.request.dto.*;
import ru.zettatech.cartservice.response.dto.CreateShoppingCartResponse;
import ru.zettatech.cartservice.response.dto.ServiceResponse;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public ResponseEntity<?> createShoppingCart(CreateShoppingCartRequest request) {
        try {
            ShoppingCart cart = new ShoppingCart();
            cart.setUserId(request.getCustomerId());
            ShoppingCart savedShoppingCart = shoppingCartRepository.save(cart);

            CreateShoppingCartResponse response = new CreateShoppingCartResponse();
            response.setCode("200");
            response.setMessage("Shopping cart created!");
            response.setCartId(savedShoppingCart.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error Creating Shopping Cart", e);
            ServiceResponse res = new ServiceResponse();
            res.setCode("501");
            res.setMessage("Internal Server Error");
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> addProductToCart(AddProductToCartRequest request) {
        ServiceResponse res = new ServiceResponse();

        try {
            CartItem presentCartItem = cartItemRepository.findByCartIdAndProductId(
                    request.getCartId(), request.getProductItem().getProductId());

            if (presentCartItem != null) {
                presentCartItem.setProductQuantity(presentCartItem.getProductQuantity() + 1);
                BigDecimal totalPrice = presentCartItem.getTotalPrice().add(presentCartItem.getPricePerUnit());
                presentCartItem.setTotalPrice(totalPrice);
                cartItemRepository.save(presentCartItem);
                res.setCode("200");
                res.setMessage("Products added successfully to the cart");
                return new ResponseEntity<>(res, HttpStatus.OK);
            } else {
                ShoppingCart cart = shoppingCartRepository.findById(request.getCartId()).orElse(null);
                CartItem cartItem = new CartItem();
                cartItem.setProductId(request.getProductItem().getProductId());
                cartItem.setProductName(request.getProductItem().getProductName());
                cartItem.setProductQuantity(request.getProductItem().getProductQuantity());
                BigDecimal totalPrice = request.getProductItem().getPricePerUnit()
                        .multiply(new BigDecimal(request.getProductItem().getProductQuantity()));
                cartItem.setTotalPrice(totalPrice);
                cartItem.setPricePerUnit(request.getProductItem().getPricePerUnit());
                cartItem.setProductImageUrl(request.getProductItem().getImageUrl());
                cart.addItemToCart(cartItem);
                cartItem.setCart(cart);
                shoppingCartRepository.save(cart);
                cartItemRepository.save(cartItem);
                res.setCode("200");
                res.setMessage("Products added successfully to the cart");
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error adding product to cart", e);
            res.setCode("501");
            res.setMessage("Internal Server Error!");
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> removeCartItemFromCart(RemoveProductFromCartRequest request) {
        ServiceResponse res = new ServiceResponse();

        try {
            cartItemRepository.deleteByCartIdAndProductId(request.getCartId(), request.getProductId());
            res.setCode("200");
            res.setMessage("Item removed from cart");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error removing cart item", e);
            res.setCode("500");
            res.setMessage("Internal Server Error");
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> checkCartPredicate(CheckCartPredicate request) {
        ServiceResponse res = new ServiceResponse();

        try {
            ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(request.getUserId());
            if (shoppingCart != null) {
                res.setCode("200");
                res.setMessage("User Cart Exists");
                return new ResponseEntity<>(res, HttpStatus.OK);
            } else {
                res.setCode("404");
                res.setMessage("User Cart Not Exists");
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error checking cart predicate", e);
            res.setCode("501");
            res.setMessage("Internal Server Error");
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> decreaseProductQuantityFromCart(DecreaseProductQtyRequest request) {
        ServiceResponse res = new ServiceResponse();
        BigDecimal totalPrice = BigDecimal.ZERO;

        try {
            CartItem presentCartItem = cartItemRepository.findByCartIdAndProductId(
                    request.getCartId(), request.getProductId());
            log.debug("Checking cart item: {}", presentCartItem);

            if (presentCartItem == null) {
                res.setCode("200");
                res.setMessage("Product not found in cart");
                return new ResponseEntity<>(res, HttpStatus.OK);
            }

            if (presentCartItem.getTotalPrice().equals(presentCartItem.getPricePerUnit())) {
                cartItemRepository.deleteByCartIdAndProductId(request.getCartId(), request.getProductId());
                res.setCode("200");
                res.setMessage("Product removed from the cart");
                return new ResponseEntity<>(res, HttpStatus.OK);
            } else {
                presentCartItem.setProductQuantity(presentCartItem.getProductQuantity() - 1);
                totalPrice = presentCartItem.getTotalPrice().subtract(presentCartItem.getPricePerUnit());
                presentCartItem.setTotalPrice(totalPrice);
                cartItemRepository.save(presentCartItem);
                res.setCode("200");
                res.setMessage("Products qty updated successfully in the cart");
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error decreasing product quantity", e);
            res.setCode("404");
            res.setMessage("Error updating product quantity");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> getAllCartItems(GetCartItemRequest request) {
        ShoppingCart cartItems = shoppingCartRepository.findById(request.getCartId()).orElse(null);

        if (cartItems != null) {
            return new ResponseEntity<>(cartItems, HttpStatus.OK);
        } else {
            ServiceResponse res = new ServiceResponse();
            res.setCode("200");
            res.setMessage("No Cart Found");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> cleanCart(CleanCartRequest request) {
        ServiceResponse response = new ServiceResponse();
        try {
            for (Long productId : request.getProductIds()) {
                cartItemRepository.deleteByCartIdAndProductId(request.getCartId(), productId);
            }
            response.setCode("200");
            response.setMessage("Cart Successfully cleaned");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error cleaning cart", e);
            response.setCode("500");
            response.setMessage("Internal server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
