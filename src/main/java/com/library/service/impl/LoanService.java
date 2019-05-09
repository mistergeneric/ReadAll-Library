package com.library.service.impl;

import com.library.domain.Loan;
import com.library.domain.User;

public interface LoanService {

    Loan save(Loan loan);

    void confirmation(Loan loan, User user);

}
