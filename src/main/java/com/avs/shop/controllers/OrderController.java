package com.avs.shop.controllers;

import com.avs.shop.dao.OrderRepository;
import com.avs.shop.dao.UserRepository;
import com.avs.shop.domain.User;
import com.avs.shop.dto.OrderDTO;
import com.avs.shop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;

    public OrderController(OrderRepository orderRepository, UserRepository userRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @GetMapping
    public String userList(Model model, Principal principal) {
        if (principal != null) {
            User user = userRepository.findFirstByName(principal.getName());
            List<User> userList = new ArrayList<>();
            userList.add(user);
            List<OrderDTO> orderList = orderService.getAll();
            List<OrderDTO> matches = orderList.stream().filter((OrderDTO orderDTO)
                    -> userList.contains(orderDTO.getUser())).collect(Collectors.toList());
            model.addAttribute("orders", matches);
            return "orders";
        }
        return "index";
    }

}
