package com.example.Parking.repository;

import com.example.Parking.model.Ticket;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketFakeRepository {
    private List<Ticket> tickets;

    public TicketFakeRepository() {
        this.tickets = new ArrayList<>();
    }

    public Ticket findOne(Long id) {
        for (Ticket ticket : tickets) {
            if (ticket.getId().equals(id)) {
                return ticket;
            }
        }
        return null;
    }

    public boolean exists(Long id) {
        return findOne(id) != null;
    }

    public void delete(Long id) {
        Ticket ticket = findOne(id);
        if (ticket != null) {
            tickets.remove(ticket);
        }
    }

    public void delete(Ticket ticket) {
        delete(ticket.getId());
    }

    public void save(Ticket ticket) {
        if (ticket.getId() == null) {
            ticket.setId(nextId());
            tickets.add(ticket);
        } else {
            delete(ticket);
            tickets.add(ticket);
        }
    }

    private long nextId() {
        long id = 1;
        for (Ticket ticket : tickets) {
            if (ticket.getId() >= id) {
                id = ticket.getId() + 1;
            }
        }
        return id;
    }

    public List<Ticket> findByDate(LocalDate date) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getEnd() != null && ticket.getEnd().toLocalDate().equals(date)) {
                result.add(ticket);
            }
        }
        return result;
    }
}
