package com.library.service.impl;

import com.library.domain.LoanItem;
import com.library.repository.LoanItemRepository;
import com.library.service.LoanItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Service
public class LoanItemServiceImpl implements LoanItemService {

    @Autowired
    private LoanItemRepository loanItemRepository;

    @Override
    public Date dueDate(Date today) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, 5);
        return cal.getTime();
    }

    @Override
    public ArrayList<LoanItem> findAll() {
        return (ArrayList<LoanItem>) loanItemRepository.findAll();
    }

    @Override
    public LoanItem save(LoanItem loanItem) {
        return loanItemRepository.save(loanItem);
    }
}
