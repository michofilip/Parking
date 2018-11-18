package com.example.Parking.service.price;

import java.math.BigDecimal;

public class PriceList {
    private BigDecimal[] constants;
    private BigDecimal multiplier;
    private String currencyCode;

    public PriceList(BigDecimal[] constant, BigDecimal multiplier, String currencyCode) {
        this.constants = constant;
        this.multiplier = multiplier;
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getPrice(int hours) {
        BigDecimal sum = new BigDecimal("0");
        BigDecimal current = new BigDecimal("0");
        for (int h = 1; h <= hours; h++) {
            if (h <= constants.length) {
                current = constants[h - 1];
            } else {
                current = current.multiply(multiplier);
            }
            sum = sum.add(current);
        }
        return sum;
    }
}
