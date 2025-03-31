package com.example.pubsub.service;

import com.example.pubsub.domain.Reservation;
import com.example.pubsub.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> findByExternalId(Long externalId) {
        return reservationRepository.findByClientId(externalId);
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> findByTransactionId(String transactionId) {
        return reservationRepository.findByTransactionId(transactionId);
    }

    @Override
    public List<Reservation> findByClientId(Long clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    @Override
    public List<Reservation> findByReservationDateBetween(String startDate, String endDate) {
        return reservationRepository.findByReservationDateBetween(startDate, endDate);
    }

    @Override
    public void deleteByTransactionId(String transactionId) {
        reservationRepository.deleteByTransactionId(transactionId);
    }
}