package com.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.model.PriceSlab;
import com.main.service.PriceSlabService;

@RestController
@RequestMapping("/api/priceslab")
public class PriceSlabController {
    @Autowired
    private PriceSlabService priceSlabService;

    @PostMapping
    public ResponseEntity<PriceSlab> definePriceSlab(@RequestBody PriceSlab priceSlab) {
        PriceSlab savedSlab = priceSlabService.definePriceSlab(priceSlab);
        return new ResponseEntity<>(savedSlab, HttpStatus.CREATED);
    }
}