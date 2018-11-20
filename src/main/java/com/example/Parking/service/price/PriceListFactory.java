package com.example.Parking.service.price;

import java.math.BigDecimal;

public class PriceListFactory {
    public static PriceList getRegularPriceListPLN() {
        BigDecimal[] constants = new BigDecimal[]{
                new BigDecimal("1"),
                new BigDecimal("2")};
        BigDecimal multiplier = new BigDecimal("1.5");
        String currencyCode = "PLN";

        return new PriceList(constants, multiplier, currencyCode);
    }

    public static PriceList getDisabledPriceListPLN() {
        BigDecimal[] constants = new BigDecimal[]{
                new BigDecimal("0"),
                new BigDecimal("2")};
        BigDecimal multiplier = new BigDecimal("1.2");
        String currencyCode = "PLN";

        return new PriceList(constants, multiplier, currencyCode);
    }
}
