package com.ProductAPI.Productservice.controller;

import com.ProductAPI.Productservice.dto.ProductRequest;
import com.ProductAPI.Productservice.dto.ProductResponse;
import com.ProductAPI.Productservice.exception.InvalidRequestException;
import com.ProductAPI.Productservice.exception.ProductCreationException;
import com.ProductAPI.Productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing products.
 */
@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @Operation(summary = "Create a new product", description = "Endpoint to create a new product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductRequest productRequest){
        try {
            productService.createProduct(productRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest().build(); // 请求体无效
        } catch (ProductCreationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 请求处理失败
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 服务器内部错误
        }
    }


    @Operation(summary = "Get all products", description = "This endpoint retrieves all products from DB.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "product list retrieved"),
            @ApiResponse(responseCode = "404", description = "endpoint not found")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}
