package com.library.service.impl;

import com.library.domain.Item;
import com.library.domain.LoanItem;

import java.util.ArrayList;
import java.util.Date;

public interface LoanItemService {

    Date dueDate (Date today);
    ArrayList<LoanItem> findAll();

    LoanItem save (LoanItem loanItem);
}
