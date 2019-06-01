package com.library.service;

import com.library.domain.Reservation;

import java.util.ArrayList;

public interface ReservationService {

    Reservation save(Reservation reservation);

    ArrayList<Reservation> findAll();


}
