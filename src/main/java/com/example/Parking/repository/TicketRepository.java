package com.example.Parking.repository;

import com.example.Parking.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query(value = "select * from tickets where date(end) = :date", nativeQuery = true)
    List<Ticket> findByDate(@Param("date") LocalDate date);
}
