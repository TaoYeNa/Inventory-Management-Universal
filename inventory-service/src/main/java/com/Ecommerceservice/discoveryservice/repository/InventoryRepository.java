package com.Ecommerceservice.discoveryservice.repository;

import com.Ecommerceservice.discoveryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByskuCode(String skuCode);
    List<Inventory> findBySkuCodeIn(List<String> skuCode);


}
