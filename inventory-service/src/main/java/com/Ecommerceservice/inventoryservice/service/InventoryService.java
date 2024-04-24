package com.Ecommerceservice.inventoryservice.service;
import com.Ecommerceservice.inventoryservice.dto.InventoryRequest;
import com.Ecommerceservice.inventoryservice.dto.InventoryResponse;
import com.Ecommerceservice.inventoryservice.model.Inventory;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.Ecommerceservice.inventoryservice.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor

public class InventoryService {
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;

    @Transactional
    public boolean isInStock(String skuCode){
        try{
            List<Inventory> SkuList= inventoryRepository.findByskuCode(skuCode);
            boolean inStock = !SkuList.isEmpty();
            log.info("Checked inventory exist in SKU {}: {}", skuCode, inStock ? "In Stock" : "Out of Stock");
            return inStock;
        }
        catch(Exception e){
            log.error("Error checking inventory exist in SKU {}: {}", skuCode, e.getMessage(), e);
            return false;
        }
    }

    @Transactional
    public List<InventoryResponse> getAllInventory(){
        try{
            List<Inventory> inventoryList = inventoryRepository.findAll();
            log.info("Checked all inventory");
            return inventoryList.stream().map(this::mapToDto).toList();
        }
        catch(Exception e){
            log.error("Error occurs Checked all inventory", e);
            throw new ServiceException("Failed to fetch all inventory from database ");
        }
    }

    @Transactional
    public List<InventoryResponse> getInventoryBySku(String skuCode){
        try{
            List<Inventory> inventoryList = inventoryRepository.findByskuCode(skuCode);
            log.info("Checked inventory List for SKU {}", skuCode);
            return inventoryList.stream().map(this::mapToDto).toList();
        }
        catch(Exception e){
            log.error("Error occurs Checked inventory List for SKU {},", skuCode, e);
            throw new ServiceException("Failed to fetch inventory for SKU" + skuCode);
        }
    }

    @Transactional
    public InventoryResponse addNewInventory(InventoryRequest inventoryRequest) {
        String skuCode = inventoryRequest.getSkuCode();
        Integer quantity = inventoryRequest.getQuantity();
        try {
            Inventory inventory = new Inventory();
            inventory.setSkuCode(skuCode);
            inventory.setQuantity(quantity);
            Inventory savedInventory = inventoryRepository.save(inventory);
            log.info("New Inventory {} has been added - Quantity {}", skuCode, quantity);

            return InventoryResponse.builder()
                    .skuCode(savedInventory.getSkuCode())
                    .quantity(savedInventory.getQuantity())
                    // 如果有更多字段，比如ID等，也可以在这里设置
                    .build();
        } catch (Exception e) {
            log.error("Error occurs when adding new Inventory {}", skuCode, e);
            // 在实际情况下，应该抛出一个自定义的异常，而不是返回null
            throw new ServiceException("Failed to add new inventory for SKU: " + skuCode);
        }
    }


    private InventoryResponse mapToDto(Inventory inventory) {
        return InventoryResponse.builder().skuCode(inventory.getSkuCode()).quantity(inventory.getQuantity()).build();
    }



}
