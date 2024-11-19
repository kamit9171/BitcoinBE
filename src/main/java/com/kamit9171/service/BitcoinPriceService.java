package com.kamit9171.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamit9171.model.BitcoinPriceResponse;
import com.kamit9171.model.PriceEntry;

@Service
public class BitcoinPriceService {

	 private static final String HISTORICAL_API_URL = "https://api.coindesk.com/v1/bpi/historical/close.json";
	    private static final String SUPPORTED_CURRENCIES_API_URL = "https://api.coindesk.com/v1/bpi/supported-currencies.json";

	    private final RestTemplate restTemplate;
	    private final ObjectMapper objectMapper;
	    
        @Autowired
	    public BitcoinPriceService(RestTemplate restTemplate, ObjectMapper objectMapper) {
	        this.restTemplate = restTemplate;
	        this.objectMapper = objectMapper;
	    }

	    public BitcoinPriceResponse getBitcoinPrices(String startDate, String endDate, String currency) throws Exception {
	    	HttpHeaders headers = new HttpHeaders();
	        headers.setCacheControl("no-cache, no-store, must-revalidate");  // No cache
	        HttpEntity<String> entity = new HttpEntity<>(headers);
	        String url = String.format("%s?start=%s&end=%s", HISTORICAL_API_URL, startDate, endDate);
	        

	     
	        Map<String, Object> response = fetchApiData(url);

	       
	        Map<String, Double> prices = extractPrices(response);

	     
	        if (prices == null || prices.isEmpty()) {
	            throw new DataNotFoundException("No data available for the given date range.");
	        }

	        // Convert prices to the requested currency
	        if (!"USD".equalsIgnoreCase(currency)) {
	            double conversionRate = getConversionRate(currency);
	            prices.replaceAll((date, price) -> price * conversionRate);
	        }

	       
	        double highest = Collections.max(prices.values());
	        double lowest = Collections.min(prices.values());

	       
	        List<PriceEntry> priceEntries = new ArrayList<>();
	        for (Map.Entry<String, Double> entry : prices.entrySet()) {
	            String marker = "";
	            if (entry.getValue() == highest) marker = "high";
	            if (entry.getValue() == lowest) marker = "low";

	            priceEntries.add(new PriceEntry(entry.getKey(), entry.getValue(), marker));
	        }

	        return new BitcoinPriceResponse(currency.toUpperCase(), priceEntries);
	    }

	    private Map<String, Object> fetchApiData(String url) throws Exception {
	        try {
	            return restTemplate.getForObject(url, Map.class);
	        } catch (Exception e) {
	            throw new ApiException("Error fetching data from CoinDesk API", e);
	        }
	    }

	    private Map<String, Double> extractPrices(Map<String, Object> response) {
	        if (response == null) return null;
	        return objectMapper.convertValue(response.get("bpi"), Map.class);
	    }

	    private double getConversionRate(String currency) throws Exception {
	        List<Map<String, String>> currencies = fetchSupportedCurrencies();

	        for (Map<String, String> currencyData : currencies) {
	            if (currencyData.get("currency").equalsIgnoreCase(currency)) {
	                
	                return 0.014; 
	            }
	        }
	        throw new CurrencyNotSupportedException("Unsupported currency: " + currency);
	    }

	    private List<Map<String, String>> fetchSupportedCurrencies() throws Exception {
	        try {
	            return restTemplate.getForObject(SUPPORTED_CURRENCIES_API_URL, List.class);
	        } catch (Exception e) {
	            throw new ApiException("Error fetching supported currencies", e);
	        }
	    }

	    
	    public static class ApiException extends Exception {
	        public ApiException(String message, Throwable cause) {
	            super(message, cause);
	        }
	    }

	    public static class DataNotFoundException extends Exception {
	        public DataNotFoundException(String message) {
	            super(message);
	        }
	    }

	    public static class CurrencyNotSupportedException extends Exception {
	        public CurrencyNotSupportedException(String message) {
	            super(message);
	        }
	    }
}
