package ru.zettatech.cartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zettatech.cartservice.request.dto.*;
import ru.zettatech.cartservice.service.ShoppingCartService;
import ru.zettatech.common.util.ValidationUtil;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping
    public ResponseEntity<?> createShoppingCart(@RequestBody CreateShoppingCartRequest request, BindingResult result) {
        ResponseEntity<?> errorResponseEntity = ValidationUtil.validateRequest(result);
        if (errorResponseEntity != null) {
            return errorResponseEntity;
        }
        return shoppingCartService.createShoppingCart(request);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addProductToCart(
            @PathVariable Long cartId,
            @RequestBody AddProductToCartRequest.ProductItemDTO productItem) {
        AddProductToCartRequest request = new AddProductToCartRequest();
        request.setCartId(cartId);
        request.setProductItem(productItem);
        return shoppingCartService.addProductToCart(request);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeCartItemFromCart(
            @PathVariable Long cartId,
            @PathVariable Long productId) {
        RemoveProductFromCartRequest request = new RemoveProductFromCartRequest();
        request.setCartId(cartId);
        request.setProductId(productId);
        return shoppingCartService.removeCartItemFromCart(request);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<?> getCartItems(@PathVariable Long cartId) {
        GetCartItemRequest request = new GetCartItemRequest();
        request.setCartId(cartId);
        return shoppingCartService.getAllCartItems(request);
    }

    @PostMapping("/{cartId}/decrease")
    public ResponseEntity<?> decreaseProductQuantityFromCart(
            @PathVariable Long cartId,
            @RequestBody DecreaseProductQtyRequest request) {
        request.setCartId(cartId);
        return shoppingCartService.decreaseProductQuantityFromCart(request);
    }

    @PostMapping("/check")
    public ResponseEntity<?> checkCartPredicate(@RequestBody CheckCartPredicate request) {
        return shoppingCartService.checkCartPredicate(request);
    }

    @PostMapping("/{cartId}/clean")
    public ResponseEntity<?> cleanCart(
            @PathVariable Long cartId,
            @RequestBody CleanCartRequest request) {
        request.setCartId(cartId);
        return shoppingCartService.cleanCart(request);
    }
}
