package com.library.service.impl;

import com.library.domain.LoanItem;

import java.util.Date;

public interface LoanItemService {

    Date dueDate (Date today);
    LoanItem save (LoanItem loanItem);
}
