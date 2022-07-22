package com.avs.shop.dto;

import com.avs.shop.domain.OrderStatus;
import com.avs.shop.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderDTO {

    private Long id;
    private User user;
    private OrderStatus status;
    private BigDecimal sum;

}
