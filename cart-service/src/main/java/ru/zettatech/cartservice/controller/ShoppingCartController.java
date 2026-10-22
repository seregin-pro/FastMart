package ru.zettatech.cartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zettatech.cartservice.request.dto.AddProductToCartRequest;
import ru.zettatech.cartservice.request.dto.CheckCartPredicate;
import ru.zettatech.cartservice.request.dto.CleanCartRequest;
import ru.zettatech.cartservice.request.dto.CreateShoppingCartRequest;
import ru.zettatech.cartservice.request.dto.DecreaseProductQtyRequest;
import ru.zettatech.cartservice.request.dto.GetCartItemRequest;
import ru.zettatech.cartservice.request.dto.RemoveProductFromCartRequest;
import ru.zettatech.cartservice.service.ShoppingCartService;
import ru.zettatech.common.util.ValidationUtil;

@RestController
@RequestMapping("/api/shopping_cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/check_cart_predicate")
    public ResponseEntity<?> checkCartPredicate(@RequestBody CheckCartPredicate request) {
        return shoppingCartService.checkCartPredicate(request);
    }

    @PostMapping("/create_shopping_cart")
    public ResponseEntity<?> createShoppingCart(@RequestBody CreateShoppingCartRequest request, BindingResult result) {
        ResponseEntity<?> errorResponseEntity = ValidationUtil.validateRequest(result);
        if (errorResponseEntity != null) {
            return errorResponseEntity;
        }
        return shoppingCartService.createShoppingCart(request);
    }

    @PostMapping("/add_product_to_cart")
    public ResponseEntity<?> addProductToShoppingCart(@RequestBody AddProductToCartRequest request) {
        return shoppingCartService.addProductToCart(request);
    }

    @PostMapping("/decrease_product_qty_from_cart")
    public ResponseEntity<?> decreaseProductQuantityFromCart(@RequestBody DecreaseProductQtyRequest request) {
        return shoppingCartService.decreaseProductQuantityFromCart(request);
    }

    @PostMapping("/remove_cart_item_f_cart")
    public ResponseEntity<?> removeCartItemFromCart(@RequestBody RemoveProductFromCartRequest request) {
        return shoppingCartService.removeCartItemFromCart(request);
    }

    @PostMapping("/cart_items")
    public ResponseEntity<?> getAllCartItems(@RequestBody GetCartItemRequest request) {
        return shoppingCartService.getAllCartItems(request);
    }

    @PostMapping("/clean_cart")
    public ResponseEntity<?> cleanCart(@RequestBody CleanCartRequest request) {
        return shoppingCartService.cleanCart(request);
    }
}
