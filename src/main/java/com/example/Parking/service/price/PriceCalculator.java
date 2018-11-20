package com.example.Parking.service.price;

import com.example.Parking.model.Ticket;
import com.example.Parking.repository.CurrencyFakeRepository;
import com.example.Parking.service.currency.CurrencyExchanger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;

@Component
@AllArgsConstructor
public class PriceCalculator {

    private CurrencyFakeRepository currencyRepository;

    public BigDecimal calculate(Ticket ticket) {
        int hours = calculateHours(ticket);
        PriceList priceList;

        if (ticket.getDisabled()) {
            priceList = PriceListFactory.getDisabledPriceListPLN();
        } else {
            priceList = PriceListFactory.getRegularPriceListPLN();
        }
        BigDecimal priceListCurrencyRate = currencyRepository.findByCode(priceList.getCurrencyCode()).getRate();

        BigDecimal valueOrg = priceList.getPrice(hours);
        BigDecimal valueExc = CurrencyExchanger.exchange(valueOrg, priceListCurrencyRate, ticket.getCurrency().getRate());

        return valueExc;
    }

    private int calculateHours(Ticket ticket) {
        Duration between = Duration.between(ticket.getBegin(), ticket.getEnd());
        long sec = between.toMillis() / 1000;
        return (int) Math.ceil(sec / 3600.0);
    }

}
