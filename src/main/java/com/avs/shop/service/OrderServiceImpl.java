package com.avs.shop.service;

import com.avs.shop.dao.OrderRepository;
import com.avs.shop.domain.Order;
import com.avs.shop.dto.OrderDTO;
import com.avs.shop.dto.OrderIntegrationDto;
import com.avs.shop.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper mapper = OrderMapper.ORDER_MAPPER;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public void saveOrder(Order order) {
        Order saveOrder = orderRepository.save(order);
    }

    @Override
    public List<OrderDTO> getAll() {
        return mapper.fromOrderList(orderRepository.findAll());
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

}
