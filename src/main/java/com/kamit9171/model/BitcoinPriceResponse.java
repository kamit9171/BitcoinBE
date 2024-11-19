package com.kamit9171.model;


import java.util.List;

public class BitcoinPriceResponse {
    private String currency;
    private List<PriceEntry> prices;

    public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<PriceEntry> getPrices() {
		return prices;
	}

	public void setPrices(List<PriceEntry> prices) {
		this.prices = prices;
	}

	public BitcoinPriceResponse(String currency, List<PriceEntry> prices) {
        this.currency = currency;
        this.prices = prices;
    }

   
}

