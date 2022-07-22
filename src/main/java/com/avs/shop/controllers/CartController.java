package com.avs.shop.controllers;

import com.avs.shop.domain.Cart;
import com.avs.shop.dto.CartDTO;
import com.avs.shop.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String aboutCart(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("cart", new Cart());
        } else {
            CartDTO cartDTO = cartService.getCartByUser(principal.getName());
            model.addAttribute("cart", cartDTO);
        }
        return "cart";
    }

    @PostMapping("/cart")
    public String commitCart(Principal principal) {
        if (principal != null) {
            cartService.commitCartToOrder(principal.getName());
        }
        return "redirect:/cart";
    }

    @RequestMapping("/cart/remove")
    public String deleteUser(Long id, Principal principal) {
        if (principal != null && id != null) {
            cartService.removeProductFromCart(id, principal.getName());
        }
        return "redirect:/cart";
    }

    @RequestMapping("/cart/removeAll")
    public String deleteUser(Principal principal) {
        if (principal != null) {
            cartService.removeAllProductFromCart(principal.getName());
        }
        return "redirect:/cart";
    }

}
