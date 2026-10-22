package ru.zettatech.cartservice.request.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductToCartRequest {
    private Long cartId;
    private ProductItemDTO productItem;

    @Getter
    @Setter
    public static class ProductItemDTO {
        private Long productId;
        private String productName;
        private Long productQuantity;
        private java.math.BigDecimal pricePerUnit;
        private String imageUrl;
    }
}
