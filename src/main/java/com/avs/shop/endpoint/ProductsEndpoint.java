package com.avs.shop.endpoint;

import com.avs.shop.dto.ProductDTO;
import com.avs.shop.service.ProductService;
import com.avs.shop.ws.products.GetProductsRequest;
import com.avs.shop.ws.products.GetProductsResponse;
import com.avs.shop.ws.products.ProductsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;

@Endpoint
public class ProductsEndpoint {

    public static final String NAMESPACE_URL = "http://avs.com/avsshop/ws/products";
    private final ProductService productService;

    @Autowired
    public ProductsEndpoint(ProductService productService) {
        this.productService = productService;
    }

    @PayloadRoot(namespace = NAMESPACE_URL, localPart = "getProductsRequest")
    @ResponsePayload
    public GetProductsResponse getGreeting(@RequestPayload GetProductsRequest request)
            throws DatatypeConfigurationException {
        GetProductsResponse response = new GetProductsResponse();
        productService.getALL()
                .forEach(dto -> response.getProducts().add(createProductWS(dto)));
        return response;
    }

    private ProductsWS createProductWS(ProductDTO dto) {
        ProductsWS ws = new ProductsWS();
        ws.setId(dto.getId());
        ws.setPrice(dto.getPrice());
        ws.setTitle(dto.getTitle());
        return ws;
    }

}
