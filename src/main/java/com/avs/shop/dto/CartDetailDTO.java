package com.avs.shop.dto;

import com.avs.shop.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetailDTO {

    private String title;
    private Long productId;
    private BigDecimal price;
    private BigDecimal amount;
    private Double sum;

    public CartDetailDTO(Product product) {
        this.title = product.getTitle();
        this.productId = product.getId();
        this.price = new BigDecimal(product.getPrice());
        this.amount = new BigDecimal(1.0);
        this.sum = Double.valueOf(product.getPrice().toString());
    }

}
