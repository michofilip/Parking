package com.example.Parking.controllers;

import com.example.Parking.exceptions.*;
import com.example.Parking.model.Currency;
import com.example.Parking.model.Ticket;
import com.example.Parking.repository.CurrencyFakeRepository;
import com.example.Parking.repository.TicketFakeRepository;
import com.example.Parking.requests.BeginParkingRequest;
import com.example.Parking.requests.EndParkingRequest;
import com.example.Parking.responses.BeginParkingResponse;
import com.example.Parking.responses.EndParkingResponse;
import com.example.Parking.responses.ErrorResponse;
import com.example.Parking.service.price.PriceCalculator;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/driver")
public class DriverController {

    private TicketFakeRepository ticketRepository;
    private CurrencyFakeRepository currencyRepository;
    private PriceCalculator priceCalculator;

    @PostMapping("/start")
    public BeginParkingResponse beginParking(@RequestBody BeginParkingRequest beginParkingRequest) throws IncorrectBeginParkingRequestException {
        verify(beginParkingRequest);

        LocalDateTime now = LocalDateTime.now();

        Ticket ticket = new Ticket();
        ticket.setDisabled("true".equals(beginParkingRequest.getDisabled()));
        ticket.setBegin(now);

        ticketRepository.save(ticket);

        BeginParkingResponse beginParkingResponse = new BeginParkingResponse();
        beginParkingResponse.setId(String.valueOf(ticket.getId()));
        beginParkingResponse.setDisabled(String.valueOf(ticket.getDisabled()));
        beginParkingResponse.setBegin(now.toString());

        return beginParkingResponse;
    }

    @PostMapping("/stop")
    public EndParkingResponse endParking(@RequestBody EndParkingRequest endParkingRequest) throws TicketAlreadyPaidException, UnknownTickedIdException, UnknownCurrencyException, IncorrectEndParkingRequestException {
        verify(endParkingRequest);

        LocalDateTime now = LocalDateTime.now();
        Long id = Long.parseLong(endParkingRequest.getId());
        Currency currency = currencyRepository.findByCode(endParkingRequest.getCurrencyCode());

        Ticket ticket = ticketRepository.findOne(id);
        ticket.setEnd(now);
        ticket.setCurrency(currency);
        ticket.setValue(priceCalculator.calculate(ticket));

        ticketRepository.save(ticket);

        EndParkingResponse endParkingResponse = new EndParkingResponse();
        endParkingResponse.setId(String.valueOf(ticket.getId()));
        endParkingResponse.setDisabled(String.valueOf(ticket.getDisabled()));
        endParkingResponse.setBegin(String.valueOf(ticket.getBegin()));
        endParkingResponse.setEnd(String.valueOf(ticket.getEnd()));
        endParkingResponse.setCurrencyCode(currency.getCode());
        endParkingResponse.setValue(String.valueOf(ticket.getValue()));

        return endParkingResponse;
    }

    @ExceptionHandler({
            IncorrectBeginParkingRequestException.class,
            IncorrectEndParkingRequestException.class,
            UnknownCurrencyException.class,
            UnknownTickedIdException.class,
            TicketAlreadyPaidException.class,
            Exception.class})
    public ErrorResponse handleException(Exception ex) {
        if (ex instanceof IncorrectBeginParkingRequestException) {
            return new ErrorResponse("Incorrect BeginParkingRequest format");
        }
        if (ex instanceof IncorrectEndParkingRequestException) {
            return new ErrorResponse("Incorrect EndParkingRequest format");
        }
        if (ex instanceof UnknownCurrencyException) {
            return new ErrorResponse("Unknown currency");
        }
        if (ex instanceof UnknownTickedIdException) {
            return new ErrorResponse("Unknown ticked ID");
        }
        if (ex instanceof TicketAlreadyPaidException) {
            return new ErrorResponse("Ticket with this ID has already been paid");
        }

        return new ErrorResponse("Unknown error");
    }

    private void verify(BeginParkingRequest beginParkingRequest) throws IncorrectBeginParkingRequestException {
        String disabled = beginParkingRequest.getDisabled();
        if (disabled == null || (!disabled.equals("true") && !disabled.equals("false"))) {
            throw new IncorrectBeginParkingRequestException();
        }
    }

    private void verify(EndParkingRequest endParkingRequest) throws IncorrectEndParkingRequestException,
            UnknownCurrencyException, UnknownTickedIdException, TicketAlreadyPaidException {
        String idStr = endParkingRequest.getId();
        String currencyCode = endParkingRequest.getCurrencyCode();
        Long id;

        if (idStr == null) {
            throw new IncorrectEndParkingRequestException();
        } else {
            try {
                id = Long.parseLong(idStr);
            } catch (NumberFormatException e) {
                throw new IncorrectEndParkingRequestException();
            }
        }

        if (!ticketRepository.exists(id)) {
            throw new UnknownTickedIdException();
        } else {
            Ticket ticket = ticketRepository.findOne(id);
            if (ticket.getEnd() != null) {
                throw new TicketAlreadyPaidException();
            }
        }

        if (currencyCode == null) {
            throw new IncorrectEndParkingRequestException();
        } else if (currencyRepository.findByCode(currencyCode) == null) {
            throw new UnknownCurrencyException();
        }
    }
}
