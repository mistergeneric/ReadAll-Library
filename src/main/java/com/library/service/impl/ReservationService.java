package com.library.service.impl;

import com.library.domain.Reservation;

import java.util.ArrayList;

public interface ReservationService {

    Reservation save(Reservation reservation);

    ArrayList<Reservation> findAll();
}
