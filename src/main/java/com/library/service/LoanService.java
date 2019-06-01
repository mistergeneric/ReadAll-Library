package com.library.service;

import com.library.domain.Book;
import com.library.domain.Item;
import com.library.domain.Loan;
import com.library.domain.User;

import java.util.ArrayList;

public interface LoanService {

    Loan save(Loan loan);

    void confirmation(Loan loan, User user);

    ArrayList<Loan> findAll();

    Loan makeLoan(Item item, User user, Book book);
}
