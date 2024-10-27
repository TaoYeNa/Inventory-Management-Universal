package com.ProductAPI.Productservice;

import com.ProductAPI.Productservice.dto.ProductRequest;
import com.ProductAPI.Productservice.model.Product;
import com.ProductAPI.Productservice.repository.ProductRepository;
import com.ProductAPI.Productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct_save(){
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("1111");
        productRequest.setPrice(BigDecimal.valueOf(100.0));
        productRequest.setDescription("this is a test");

        Product expectedProduct =
                Product.builder().name(productRequest.getName()).price(productRequest.getPrice())
                        .description(productRequest.getDescription()).build();
        when(productRepository.save(any(Product.class))).thenReturn(expectedProduct);

        //assert it will not thore any exception
        assertDoesNotThrow(()-> productService.createProduct(productRequest), "Creating product should not throw any exception");

        //The .save() was only called once
        verify(productRepository, times(1)).save(any(Product.class));
    }

//    Should be checked on controller

//    @Test
//    public void testCreateProduct_EmptyName(){
//        ProductRequest productRequest = new ProductRequest();
//        productRequest.setName("");
//        productRequest.setDescription("111");
//        productRequest.setPrice(BigDecimal.valueOf(100.0));
//        assertThrows(IllegalArgumentException.class, ()-> productService.createProduct(productRequest));
//    }
//
//    @Test
//    public void testCreateProduct_NegativePrice(){
//        ProductRequest productRequest = new ProductRequest();
//        productRequest.setName("11");
//        productRequest.setDescription("111");
//        productRequest.setPrice(null);
//        assertThrows(IllegalArgumentException.class, ()-> productService.createProduct(productRequest));
//    }



}