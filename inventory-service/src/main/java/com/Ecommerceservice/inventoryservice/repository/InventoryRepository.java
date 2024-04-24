package com.Ecommerceservice.inventoryservice.repository;

import com.Ecommerceservice.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByskuCode(String skuCode);

}
