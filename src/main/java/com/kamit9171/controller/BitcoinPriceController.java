package com.kamit9171.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kamit9171.model.BitcoinPriceResponse;
import com.kamit9171.service.BitcoinPriceService;

@CrossOrigin
@RestController

@RequestMapping("/api/bitcoin-prices")
public class BitcoinPriceController {
	
@Autowired
    private final BitcoinPriceService bitcoinPriceService;

    public BitcoinPriceController(BitcoinPriceService bitcoinPriceService) {
        this.bitcoinPriceService = bitcoinPriceService;
    }

    @GetMapping
    public ResponseEntity<?> getBitcoinPrices(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "USD") String currency,
            @RequestParam String timestamp) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setCacheControl("no-cache, no-store, must-revalidate");  
            headers.setPragma("no-cache");
            headers.setExpires(0);

            BitcoinPriceResponse response = bitcoinPriceService.getBitcoinPrices(startDate, endDate, currency);
            return ResponseEntity.ok().headers(headers).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    // Status
    @GetMapping("/health")
    public ResponseEntity<String> checkServiceHealth() {
        return ResponseEntity.ok("Bitcoin Price Service is up and running!");
    }
}
