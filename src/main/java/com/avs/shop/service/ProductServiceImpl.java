package com.avs.shop.service;

import com.avs.shop.dao.ProductRepository;
import com.avs.shop.domain.Cart;
import com.avs.shop.domain.Product;
import com.avs.shop.domain.User;
import com.avs.shop.dto.ProductDTO;
import com.avs.shop.mapper.ProductMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final CartService cartService;
    private final SimpMessagingTemplate template;

    public ProductServiceImpl(ProductRepository productRepository,
                              UserService userService, CartService cartService, SimpMessagingTemplate template) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.cartService = cartService;
        this.template = template;
    }

    @Override
    public List<ProductDTO> getALL() {
        return mapper.fromProductList(productRepository.findAll());
    }

    @Override
    @Transactional
    public void addToUserCart(Long productId, String username) {
        User user = userService.findByName(username);
        if (user == null) {
            throw new RuntimeException("User not found - " + username);
        }
        Cart cart = user.getCart();
        if (cart == null) {
            Cart newCart = cartService.createCart(user, Collections.singletonList(productId));
            user.setCart(newCart);
            userService.save(user);
        } else {
            cartService.addProducts(cart, Collections.singletonList(productId));
        }
    }

    @Override
    @Transactional
    public void addProduct(ProductDTO dto) {
        Product product = mapper.toProduct(dto);
        Product saveProduct = productRepository.save(product);
        template.convertAndSend("/topic/products", ProductMapper.MAPPER.fromProduct(saveProduct));
    }

    @Override
    @Transactional
    public void deleteProductFromBase(Long id) {
        Product product = productRepository.getReferenceById(id);
        productRepository.delete(product);
    }

}
