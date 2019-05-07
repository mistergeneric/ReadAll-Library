package com.library.service.impl;

import com.library.domain.LoanItem;
import com.library.repository.LoanItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanItemServiceImpl implements LoanItemService {

    @Autowired
    private LoanItemRepository loanItemRepository;

    @Override
    public LoanItem save(LoanItem loanItem) {
        return loanItemRepository.save(loanItem);
    }
}
