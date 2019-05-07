package com.library.service.impl;

import com.library.domain.Item;
import com.library.domain.Loan;
import com.library.repository.ItemRepository;
import com.library.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements  LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }
}
