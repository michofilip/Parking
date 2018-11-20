package com.example.Parking.service.report;

import com.example.Parking.model.Currency;
import com.example.Parking.model.Ticket;
import com.example.Parking.service.currency.CurrencyExchanger;

import java.math.BigDecimal;
import java.util.List;

public class IncomeCalculator {
    public static BigDecimal calculate(List<Ticket> tickets, Currency currency) {
        BigDecimal sum = new BigDecimal("0");
        for (Ticket ticket : tickets) {
            BigDecimal current = CurrencyExchanger.exchange(ticket.getValue(), ticket.getCurrency().getRate(), currency.getRate());
            sum = sum.add(current);
        }

        return sum.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
