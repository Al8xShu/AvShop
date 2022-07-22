package com.avs.shop.service;

import com.avs.shop.domain.Order;
import com.avs.shop.dto.OrderDTO;
import com.avs.shop.dto.OrderIntegrationDto;

import java.util.List;

public interface OrderService {

    void saveOrder(Order order);

    List<OrderDTO> getAll();

    Order getOrder(Long id);

}
