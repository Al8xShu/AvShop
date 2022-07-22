package com.avs.shop.mapper;

import com.avs.shop.domain.Order;
import com.avs.shop.dto.OrderDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {

    OrderMapper ORDER_MAPPER = Mappers.getMapper(OrderMapper.class);

    Order toOrder(OrderDTO dto);

    @InheritConfiguration
    OrderDTO fromOrder(Order order);

    List<Order> toOrderList(List<OrderDTO> orderDTOS);

    List<OrderDTO> fromOrderList(List<Order> orders);

}
