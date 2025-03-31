package com.example.pubsub.service;

import com.example.pubsub.domain.Reservation;
import java.util.List;

public interface ReservationService {
    Reservation save(Reservation reservation);
    List<Reservation> findAll();
    List<Reservation> findByExternalId(Long externalId);
    List<Reservation> findByTransactionId(String transactionId);
    List<Reservation> findByClientId(Long clientId);
    List<Reservation> findByReservationDateBetween(String startDate, String endDate);
    void deleteByTransactionId(String transactionId);
}