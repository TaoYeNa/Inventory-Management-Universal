package com.ProductAPI.Productservice.repository;

import com.ProductAPI.Productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product,String> {


}
