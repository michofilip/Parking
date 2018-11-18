package com.example.Parking.controllers;

import com.example.Parking.exceptions.IncorrectReportRequestException;
import com.example.Parking.exceptions.UnknownCurrencyException;
import com.example.Parking.exceptions.UnknownTickedIdException;
import com.example.Parking.model.Currency;
import com.example.Parking.model.Ticket;
import com.example.Parking.repository.CurrencyRepository;
import com.example.Parking.repository.TicketRepository;
import com.example.Parking.requests.ReportRequest;
import com.example.Parking.responses.ErrorResponse;
import com.example.Parking.responses.ReportResponse;
import com.example.Parking.service.report.IncomeCalculator;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/owner")
public class OwnerController {
    private TicketRepository ticketRepository;
    private CurrencyRepository currencyRepository;

    @PostMapping("/report")
    public ReportResponse report(@RequestBody ReportRequest reportRequest) throws IncorrectReportRequestException, UnknownCurrencyException {
        verify(reportRequest);

        Currency currency = currencyRepository.findByCode(reportRequest.getCurrencyCode());
        LocalDate localDate = LocalDate.parse(reportRequest.getDate());
        List<Ticket> tickets = ticketRepository.findByDate(localDate);

        ReportResponse reportResponse = new ReportResponse();
        reportResponse.setDate(localDate.toString());
        reportResponse.setCurrencyCode(currency.getCode());
        reportResponse.setIncome(IncomeCalculator.calculate(tickets, currency).toString());

        return reportResponse;
    }

    @ExceptionHandler({
            IncorrectReportRequestException.class,
            UnknownTickedIdException.class,
            Exception.class})
    public ErrorResponse handleException(Exception ex) {
        if (ex instanceof IncorrectReportRequestException) {
            return new ErrorResponse("Incorrect ReportRequest format");
        }
        if (ex instanceof UnknownCurrencyException) {
            return new ErrorResponse("Unknown currency");
        }

        return new ErrorResponse("Unknown error");
    }

    private void verify(ReportRequest reportRequest) throws IncorrectReportRequestException, UnknownCurrencyException {
        String dateStr = reportRequest.getDate();
        String currencyCode = reportRequest.getCurrencyCode();

        if (dateStr == null) {
            throw new IncorrectReportRequestException();
        } else {
            try {
                LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                throw new IncorrectReportRequestException();
            }
        }

        if (currencyCode == null) {
            throw new IncorrectReportRequestException();
        } else if (currencyRepository.findByCode(currencyCode) == null) {
            throw new UnknownCurrencyException();
        }
    }
}
