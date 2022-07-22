package com.avs.shop.service;

import com.avs.shop.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getALL();

    void addToUserCart(Long productId, String username);

    void addProduct(ProductDTO dto);

    void deleteProductFromBase(Long id);

}
