package com.avs.shop.service;

import com.avs.shop.domain.Cart;
import com.avs.shop.domain.User;
import com.avs.shop.dto.CartDTO;

import java.util.List;

public interface CartService {

    Cart createCart(User user, List<Long> productId);

    void addProducts(Cart cart, List<Long> productId);

    void removeProductFromCart(Long id, String userName);

    void removeAllProductFromCart(String userName);

    CartDTO getCartByUser(String name);

    void commitCartToOrder(String userName);

}
