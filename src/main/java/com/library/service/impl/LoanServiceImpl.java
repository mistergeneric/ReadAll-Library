package com.library.service.impl;

import com.library.domain.*;
import com.library.repository.LoanRepository;
import com.library.service.*;
import com.library.utility.MailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private LoanRepository loanRepository;


    @Autowired
    private LoanItemService loanItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private ItemService itemService;


    @Override
    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    public void confirmation(Loan loan, User user) {
        try {
            MailConstructor mailConstructor = new MailConstructor();
            SimpleMailMessage email = mailConstructor.constructOrderConfirmationEmail(user, loan);
            mailSender.send(email);
        }
        catch(Exception ex)
        {
            System.out.println("Oops : EMAIL");
        }

    }

    @Override
    public ArrayList<Loan> findAll() {
        return (ArrayList<Loan>) loanRepository.findAll();
    }

    @Override
    public Loan makeLoan(Item item, User user, Book book) {
        Loan loan = new Loan();
        loan.setUser(user);

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 5);
        dt = c.getTime();
        loan.setDueDate(dt);


        save(loan);

        LoanItem loanItem = new LoanItem();
        loanItem.setBook(book);

        loanItem.setItem(item);
        loanItem.setLoan(loan);

        loanItemService.save(loanItem);

        book.setStockLevel(book.getStockLevel() - 1);

        book.setNoOfLoans(book.getNoOfLoans() + 1);

        book.setActiveLoans(book.getActiveLoans() +1);
        bookService.save(book);

        item.setLoaned(true);

        itemService.save(item);
        userService.save(user);

        return loan;
    }


}
