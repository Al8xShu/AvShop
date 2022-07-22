package com.avs.shop.service;

import com.avs.shop.dao.CartRepository;
import com.avs.shop.dao.ProductRepository;
import com.avs.shop.domain.*;
import com.avs.shop.dto.CartDTO;
import com.avs.shop.dto.CartDetailDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final OrderService orderService;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, UserService userService, OrderService orderService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userService = userService;
        this.orderService = orderService;
    }

    @Override
    @Transactional
    public Cart createCart(User user, List<Long> productId) {
        Cart cart = new Cart();
        cart.setUser(user);
        List<Product> productList = getCollectRefProductsById(productId);
        cart.setProducts(productList);
        return cartRepository.save(cart);
    }

    private List<Product> getCollectRefProductsById(List<Long> productId) {
        return productId.stream().map(productRepository::getOne).collect(Collectors.toList());
    }

    @Override
    public void addProducts(Cart cart, List<Long> productId) {
        List<Product> products = cart.getProducts();
        List<Product> newProductList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductList.addAll(getCollectRefProductsById(productId));
        cart.setProducts(newProductList);
        cartRepository.save(cart);
    }

    @Override
    public void removeProductFromCart(Long id, String userName) {
        User user = userService.findByName(userName);
        Cart cart = user.getCart();
        Product product = productRepository.getReferenceById(id);
        cart.getProducts().remove(product);
        cartRepository.save(cart);
    }

    @Override
    public void removeAllProductFromCart(String userName) {
        User user = userService.findByName(userName);
        Cart cart = user.getCart();
        cart.getProducts().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDTO getCartByUser(String name) {
        User user = userService.findByName(name);
        if (user == null || user.getCart() == null) {
            return new CartDTO();
        }
        CartDTO cartDTO = new CartDTO();
        Map<Long, CartDetailDTO> mapByProductId = new HashMap<>();
        List<Product> products = user.getCart().getProducts();
        for (Product product : products) {
            CartDetailDTO detail = mapByProductId.get(product.getId());
            if (detail == null) {
                mapByProductId.put(product.getId(), new CartDetailDTO(product));
            } else {
                detail.setAmount(detail.getAmount().add(new BigDecimal(1.0)));
                detail.setSum(detail.getSum() + Double.valueOf(product.getPrice().toString()));
            }
        }
        cartDTO.setCartDetails(new ArrayList<>(mapByProductId.values()));
        cartDTO.aggregate();
        return cartDTO;
    }

    @Override
    @Transactional
    public void commitCartToOrder(String userName) {
        User user = userService.findByName(userName);
        if (user == null) {
            throw new RuntimeException("User is not found");
        }
        Cart cart = user.getCart();
        if (cart == null || cart.getProducts().isEmpty()) {
            return;
        }
        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        order.setUser(user);
        Map<Product, Long> productWithAmount = cart.getProducts().stream()
                .collect(Collectors.groupingBy(product -> product, Collectors.counting()));
        List<OrderDetails> orderDetails = productWithAmount.entrySet().stream()
                .map(pair -> new OrderDetails(order, pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());
        BigDecimal total = new BigDecimal(orderDetails.stream()
                .map(detail -> detail.getPrice().multiply(detail.getAmount()))
                .mapToDouble(BigDecimal::doubleValue).sum());
        order.setDetails(orderDetails);
        order.setSum(total);
        order.setAddress("none");
        orderService.saveOrder(order);
        cart.getProducts().clear();
        cartRepository.save(cart);
    }

}
