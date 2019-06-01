package com.library;

import com.library.controller.BorrowingController;
import com.library.domain.*;
import com.library.domain.security.UserRole;
import com.library.service.*;
import com.library.utility.MailConstructor;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private ItemService itemService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BorrowingController borrowingController;

    @Autowired
    private BookService bookService;

    @Autowired
    private LoanItemService loanItemService;


    @Autowired
    private UserService userService;

    @Scheduled(fixedRate = 43200000)
    public void checkDueDates() {
        ArrayList<Loan> allLoans = loanService.findAll();

        Date today = Calendar.getInstance().getTime();
        for(Loan l : allLoans)
        {
            if (l.getDueDate().before(today) && l.getReturnedOn() == null)
            {
                mailSender.send(mailConstructor.lateBookEmail(l.getUser(), l));

            }
        }
    }




    @Scheduled(fixedRate = 43200000)
    public void takePayment() {
        List<User> userList = userService.findAll();
        Date today = Calendar.getInstance().getTime();
        GregorianCalendar cal = new GregorianCalendar();
        //Number of Days to add


        for(User u : userList)
        {
            Reservation reservation = new Reservation();

            //customer due to pay
            Date nextPaymentDate = reservation.addDays(u.getLastPaymentDate(), 30);


            if(u.getLastPaymentDate() == null || nextPaymentDate.equals(today))
            {
                Stripe.apiKey = "sk_test_og8QKDHctfA8NbLeqm6wN5l700cToRw9Sb";

                UserPayment userPayment = new UserPayment();
                for(UserPayment up : u.getUserPaymentList() )
                {
                    if(up.isDefaultPayment())
                    {
                        userPayment = up;
                    }
                }
                Map<String, Object> tokenParams = new HashMap<String, Object>();
                Map<String, Object> cardParams = new HashMap<String, Object>();

                try {
                    cardParams.put("name", u.getFirstName() + " " + u.getLastName());
                    cardParams.put("number", userPayment.getCardNumber());
                    cardParams.put("exp_month", userPayment.getExpiryMonth());
                    cardParams.put("exp_year", userPayment.getExpiryYear());
                    cardParams.put("cvc", userPayment.getCvc());
                    tokenParams.put("card", cardParams);
                }
                catch (Exception ex)
                {
                    mailSender.send(mailConstructor.noDefaultPayment(u));
                }

                Map<String, Object> params = new HashMap<>();
                double amount = 0.00;
                for(UserRole ur : u.getUserRoles())
                {
                    if(ur.getRole().getName().equals("ROLE_STANDARD"))
                    {
                        amount = 5.99;
                    }
                    if (ur.getRole().getName().equals("ROLE_PREMIUM"))
                    {
                        amount = 9.99;
                    }
                }
                params.put("amount", amount);
                params.put("currency", "gbp");
                params.put("description", "ReadAll Library Charge");
                try {
                    params.put("source", Token.create(tokenParams));
                    Charge charge = Charge.create(params);
                    u.setLastPaymentDate(today);
                    userService.save(u);

                }
                catch (Exception ex)
                {
                    mailSender.send(mailConstructor.failedPayment(u));
                }

            }
        }
    }


    @Scheduled(fixedRate = 43200000)
    public void reservationChecker()
    {
        ArrayList<Reservation> allReservations = reservationService.findAll();
        ArrayList<Item> allItems = itemService.findAll();
        Date today = Calendar.getInstance().getTime();

        for(Reservation r : allReservations)
        {
            if(r.getReservedFor().equals(today) && !r.isComplete() && !r.getItem().isLoaned())
            {
                User user = r.getReservedBy();
                Book book = bookService.findOne(r.getItem().getBook().getBookRef());
                if (book.getStockLevel() == 0) {
                    //if the book is not in stock

                    mailSender.send(mailConstructor.noStock(book, user));
                    r.setComplete(true);
                    reservationService.save(r);
                }
                //new item
                else{
                Item item = new Item();

                //if the user is still got loans available to him (defined by the account level, then this fires off. (else it redirects)
                if (user.getNumberOfLoans() > 0) {
                    ArrayList<Book> userBooks = new ArrayList<>();
                    ArrayList<Loan> allLoans = loanService.findAll();
                    ArrayList<LoanItem> loanItems = loanItemService.findAll();

                    //Andrew, in the future yuu may be tempted to 'fix' this. DO NOT TOUCH for it works now and you
                    //may break it


                    //this is where things get wild, iterating through every item in db, to find a copy of the book which is free to be loaned
                    for (Item itemi : allItems) {
                        //if the book is free to be loaned
                        if (itemi.getBook().getBookRef() == r.getItem().getBook().getBookRef() && !itemi.isLoaned()) {
                            item = itemi;
                        }
                    }
                    item = bookService.checkBorrowed(item, user, book);

                    r.setComplete(true);
                    reservationService.save(r);
                    Loan loan = loanService.makeLoan(item, user, book);

                    loanService.confirmation(loan, user);
                }
                else{
                    mailSender.send(mailConstructor.noStock(book, user));
                    r.setComplete(true);
                    reservationService.save(r);
                }
                }
            }



        }

    }


}
