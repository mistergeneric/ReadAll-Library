package com.library;

import com.library.domain.Loan;
import com.library.service.LoanService;
import com.library.utility.MailConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private LoanService loanService;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private JavaMailSender mailSender;


    @Scheduled(fixedRate = 2000)
    public void checkDueDates() {
        ArrayList<Loan> allLoans = loanService.findAll();

        Date today = Calendar.getInstance().getTime();
        for(Loan l : allLoans)
        {
            if (l.getDueDate().before(today))
            {
                mailSender.send(mailConstructor.lateBookEmail(l.getUser(), l));

            }
        }
    }

    public void scheduleTaskWithFixedDelay() {}

    public void scheduleTaskWithInitialDelay() {}

    public void scheduleTaskWithCronExpression() {}

}
