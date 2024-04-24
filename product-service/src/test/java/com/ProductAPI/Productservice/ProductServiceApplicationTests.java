package com.ProductAPI.Productservice;

import com.ProductAPI.Productservice.dto.ProductRequest;
import com.ProductAPI.Productservice.model.Product;
import com.ProductAPI.Productservice.repository.ProductRepository;
import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
    @Container
    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.0.10");
    @Autowired
    private MockMvc mockMvc;
    final private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ProductRepository productRepository;
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
    @BeforeEach
    void setUpData(){
        productRepository.deleteAll();
        productRepository.save(new Product("1", "iphone13", "iphone13", BigDecimal.valueOf(99.99)));
    }
    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        String productRequestJson = null;

        try {
            // 尝试将productRequest对象序列化为JSON字符串
            productRequestJson = objectMapper.writeValueAsString(productRequest);
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace(); // 在实际项目中，你可能希望使用日志记录异常，而不是打印堆栈跟踪
        }

        // 如果productRequestJson不为null，则执行POST请求
        if (productRequestJson != null) {
            try {
                mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(productRequestJson))
                        .andExpect(status().isCreated());
                Assertions.assertTrue(productRepository.findAll().size() ==2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //Testing get product
    @Test
    void shouldGetProduct() throws Exception {
        Product expectedProduct = new Product("1", "iphone13", "iphone13", BigDecimal.valueOf(99.99));
        String expectedJson = objectMapper.writeValueAsString(expectedProduct);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("iphone13")))
                .andExpect(jsonPath("$.[0].description", is("iphone13")))
                .andExpect(jsonPath("$.[0].price", is(99.99)));
    }



    private ProductRequest getProductRequest(){
        //generate a product instance and return it
        return ProductRequest.builder().name("iPhone 13").description("iPhone 13")
                .price(BigDecimal.valueOf(1200)).build();
    }

}
