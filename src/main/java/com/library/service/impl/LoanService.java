package com.library.service.impl;

import com.library.domain.Loan;
import com.library.domain.User;

import java.util.ArrayList;

public interface LoanService {

    Loan save(Loan loan);

    void confirmation(Loan loan, User user);

    ArrayList<Loan> findAll();
}
