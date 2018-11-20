package com.example.Parking.service.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyExchanger {
    public static BigDecimal exchange(BigDecimal value, BigDecimal sourceRate, BigDecimal targetRate) {
        BigDecimal result;

        if (sourceRate.equals(targetRate)) {
            result = value;
        } else if (new BigDecimal("1").equals(sourceRate)) {
            result = value.divide(targetRate, RoundingMode.HALF_UP);
        } else if (new BigDecimal("1").equals(targetRate)) {
            result = value.multiply(sourceRate);
        } else {
            result = value.multiply(sourceRate).divide(targetRate, RoundingMode.HALF_UP);
        }

        return result.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
