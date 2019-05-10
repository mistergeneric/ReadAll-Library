package com.library.service.impl;

import com.library.domain.Reservation;
import com.library.repository.ReservationRepository;
import com.library.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public ArrayList<Reservation> findAll() {
        return (ArrayList<Reservation>) reservationRepository.findAll();
    }

}
