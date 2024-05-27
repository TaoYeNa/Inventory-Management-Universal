//package com.ProductAPI.Productservice.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger.web.DocExpansion;
//import springfox.documentation.swagger.web.ModelRendering;
//import springfox.documentation.swagger.web.UiConfiguration;
//import springfox.documentation.swagger.web.UiConfigurationBuilder;
//import springfox.documentation.oas.annotations.EnableOpenApi;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import static springfox.documentation.builders.PathSelectors.regex;
//
//@Configuration
//public class SwaggerDocConfig {
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
////                .apis(RequestHandlerSelectors.basePackage("com.ProductAPI.Productservice.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo())
//                .host("http://localhost:8080")
//                ;
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Product Service Api")
//                .description("API for Product service")
//                .version("1.0.0")
//                .build();
//    }
//
////    @Bean
////    public UiConfiguration uiConfig() {
////        return UiConfigurationBuilder.builder()
////                .docExpansion(DocExpansion.LIST)
////                .defaultModelRendering(ModelRendering.EXAMPLE)
////                .build();
////    }
//}
