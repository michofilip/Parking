package com.example.Parking.repository;

import com.example.Parking.model.Currency;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CurrencyFakeRepository {
    private List<Currency> currencies;

    public CurrencyFakeRepository() {
        this.currencies = new ArrayList<>();
        Currency currency = new Currency();
        currency.setId((long) 1);
        currency.setName("Polskie złote");
        currency.setCode("PLN");
        currency.setRate(new BigDecimal("1"));
        currencies.add(currency);
    }

    public Currency findOne(Long id) {
        for (Currency currency : currencies) {
            if (currency.getId().equals(id)) {
                return currency;
            }
        }
        return null;
    }

    public boolean exists(Long id) {
        return findOne(id) != null;
    }

    public void delete(Long id) {
        Currency currency = findOne(id);
        if (currency != null) {
            currencies.remove(currency);
        }
    }

    public void delete(Currency ticket) {
        delete(ticket.getId());
    }

    public void save(Currency currency) {
        if (currency.getId() == null) {
            currency.setId(nextId());
            currencies.add(currency);
        } else {
            delete(currency);
            currencies.add(currency);
        }
    }

    private long nextId() {
        long id = 1;
        for (Currency ticket : currencies) {
            if (ticket.getId() >= id) {
                id = ticket.getId() + 1;
            }
        }
        return id;
    }

    public Currency findByCode(String code) {
        for (Currency currency : currencies) {
            if (currency.getCode().equals(code)) {
                return currency;
            }
        }
        return null;
    }
}
