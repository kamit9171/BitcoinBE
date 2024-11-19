package com.kamit9171.model;

public class PriceEntry {
    private String date;
    private double price;
    private String marker; 

    public PriceEntry(String date, double price, String marker) {
        this.date = date;
        this.price = price;
        this.marker = marker;
    }

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

    // Getters and Setters
}

