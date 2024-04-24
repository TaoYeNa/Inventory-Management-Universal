package com.Ecommerceservice.inventoryservice.controller;

import com.Ecommerceservice.inventoryservice.dto.InventoryRequest;
import com.Ecommerceservice.inventoryservice.dto.InventoryResponse;
import com.Ecommerceservice.inventoryservice.model.Inventory;
import com.Ecommerceservice.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@AllArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/isInStock")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam("sku") String skuCode ){
        return inventoryService.isInStock(skuCode);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<InventoryResponse>> getInventoryBySku(
            @RequestParam(value = "sku", required = false) String skuCode){
        List<InventoryResponse> inventoryResponseList = new ArrayList<InventoryResponse>();
        if(skuCode==null || skuCode.trim().isEmpty()){
            inventoryResponseList = inventoryService.getAllInventory();
        }
        else{
            inventoryResponseList = inventoryService.getInventoryBySku(skuCode);
        }

        if(inventoryResponseList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inventoryResponseList);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InventoryResponse> addNewInventory(@Valid @RequestBody InventoryRequest inventoryRequest){
        InventoryResponse inventoryResponse = inventoryService.addNewInventory(inventoryRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{skuCode}")
                .buildAndExpand(inventoryResponse.getSkuCode())
                .toUri();
        return ResponseEntity.created(location).body(inventoryResponse);
    }


}
