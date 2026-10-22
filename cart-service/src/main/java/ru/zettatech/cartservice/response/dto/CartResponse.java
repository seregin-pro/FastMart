package ru.zettatech.cartservice.response.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CartResponse {
    private Long cartId;
    private Long userId;
    private List<CartItemResponse> items;
    private BigDecimal totalPrice;
    private int itemCount;

    @Getter
    @Setter
    public static class CartItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private Long productQuantity;
        private BigDecimal pricePerUnit;
        private BigDecimal totalPrice;
        private String productImageUrl;
    }
}
