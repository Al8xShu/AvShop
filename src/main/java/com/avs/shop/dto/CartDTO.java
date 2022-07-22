package com.avs.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {

    private int amountProducts;
    private Double sum;
    private List<CartDetailDTO> cartDetails = new ArrayList<>();

    public void aggregate() {
        this.amountProducts = cartDetails.size();
        this.sum = cartDetails.stream().map(CartDetailDTO::getSum).mapToDouble(Double::doubleValue).sum();
    }

}
