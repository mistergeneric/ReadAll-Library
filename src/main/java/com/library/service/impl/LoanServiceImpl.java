package com.library.service.impl;

import com.library.domain.Loan;
import com.library.domain.User;
import com.library.repository.LoanRepository;
import com.library.service.LoanService;
import com.library.utility.MailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private LoanRepository loanRepository;

    @Override
    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    public void confirmation(Loan loan, User user) {
        MailConstructor mailConstructor = new MailConstructor();
        SimpleMailMessage email = mailConstructor.constructOrderConfirmationEmail(user, loan);
        mailSender.send(email);

    }

    @Override
    public ArrayList<Loan> findAll() {
        return (ArrayList<Loan>) loanRepository.findAll();
    }


}
