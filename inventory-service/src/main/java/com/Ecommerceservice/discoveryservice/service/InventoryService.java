package com.Ecommerceservice.discoveryservice.service;
import com.Ecommerceservice.discoveryservice.dto.InventoryRequest;
import com.Ecommerceservice.discoveryservice.dto.InventoryResponse;
import com.Ecommerceservice.discoveryservice.model.Inventory;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.Ecommerceservice.discoveryservice.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class InventoryService {
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;

//    @Transactional
//    public List<InventoryResponse> isInStock(List<String> skuCode){
//        try{
//            List<Inventory> SkuList= inventoryRepository.findBySkuCodeIn(skuCode);
//            //Assume we have such Inventory(sku) in DB:
//            List<InventoryResponse> responseList = SkuList.stream().map( inventory -> InventoryResponse.builder().skuCode(inventory.getSkuCode())
//                    .quantity(inventory.getQuantity()).isInStock(inventory.getQuantity()>0).build()).toList();
//
//            log.info("Check Inventory exist in Inventory - {}" , responseList);
//            return responseList;
//        }
//        catch(Exception e){
//            throw new ServiceException("Failed to check if inventory is in stock from database");
//        }
//    }

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
    public List<InventoryResponse> getInventoryBySku(List<String> skuCode){
        try{
            List<Inventory> inventoryList = inventoryRepository.findBySkuCodeIn(skuCode);
            log.info("Checked inventory List by SKU");
            List<Inventory> SkuList= inventoryRepository.findBySkuCodeIn(skuCode);
            //Assume we have such Inventory(sku) in DB:
            List<InventoryResponse> responseList = SkuList.stream().map( inventory -> InventoryResponse.builder().skuCode(inventory.getSkuCode())
                    .quantity(inventory.getQuantity()).isInStock(inventory.getQuantity()>0).build()).toList();
            //Assume some of the skuCode not in DB :
//            skuCode.stream().map(sku -> )
            return responseList;
        }
        catch(Exception e){
            log.error("Error occurs Checked inventory List for SKU {}", e.getMessage());
            throw new ServiceException("Failed to fetch inventory for SKU");
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
