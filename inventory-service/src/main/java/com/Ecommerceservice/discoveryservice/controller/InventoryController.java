package com.Ecommerceservice.discoveryservice.controller;

import com.Ecommerceservice.discoveryservice.dto.InventoryRequest;
import com.Ecommerceservice.discoveryservice.dto.InventoryResponse;
import com.Ecommerceservice.discoveryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@AllArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

//    @GetMapping("/isInStock")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<List<InventoryResponse>> isInStock(@RequestParam("sku") List<String> skuCode ){
//        if(skuCode ==null || skuCode.isEmpty()){
//            return ResponseEntity.badRequest().build();
//        }
//        List<InventoryResponse> inventoryResponseList = inventoryService.getInventoryBySku(skuCode);
//        return
//
//    }

    @GetMapping("/getInventoryBySku")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<InventoryResponse>> getInventoryBySku(
            @RequestParam(value = "sku", required = true) List<String> skuCode){
        List<InventoryResponse> inventoryResponseList = new ArrayList<InventoryResponse>();
        inventoryResponseList = inventoryService.getInventoryBySku(skuCode);
        if(inventoryResponseList.isEmpty()){
            InventoryResponse errorResponse = new InventoryResponse();
            errorResponse.setErrorMessage("No inventory available for the provided SKU code");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Collections.singletonList(errorResponse));
        }
        return ResponseEntity.ok(inventoryResponseList);
    }
    @GetMapping("/getAllInventory")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<InventoryResponse>> getAllInventory(){
        List<InventoryResponse>inventoryResponseList = inventoryService.getAllInventory();
        if(inventoryResponseList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inventoryResponseList);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String test(){
        return " get method working";
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
