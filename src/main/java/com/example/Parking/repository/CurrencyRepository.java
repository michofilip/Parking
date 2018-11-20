//package com.example.Parking.repository;
//
//import com.example.Parking.model.Currency;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//public interface CurrencyRepository extends JpaRepository<Currency, Long> {
//    @Query("select c from Currency c where code = :code")
//    Currency findByCode(@Param("code") String code);
//}
