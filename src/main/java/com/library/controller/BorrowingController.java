package com.library.controller;

import com.library.domain.*;
import com.library.service.impl.*;
import com.library.utility.MailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class BorrowingController {


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private LoanItemService loanItemService;

    @Autowired
    private ReservationService reservationService;

    @RequestMapping("/borrowItem")
    public String borrowItem(Model model, @RequestParam("bookRef") int bookRef, Principal principal)
    {

        ArrayList<LoanItem> loanItems = loanItemService.findAll();
        ArrayList<Loan> loans = loanService.findAll();
        if(principal != null)
        {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            Book book = bookService.findOne(bookRef);
            if(book.getStockLevel() == 0)
            {
                return reserveBook(bookRef, model, principal);

            }
            Item item = new Item();

            if(user.getNumberOfLoans() > 0) {
                user.setNumberOfLoans(user.getNumberOfLoans() - 1);
                ArrayList<Item> allItems = itemService.findAll();
                ArrayList<Book> userBooks = new ArrayList<>();
                for(Item itemi : allItems)
                {
                    if(itemi.getBook().getBookRef() == bookRef && !itemi.isLoaned())
                    {
                        item = itemi;
                    }
                }
                for(LoanItem loanItem: loanItems)
                {
                    for(Loan loan : loans)
                    {
                        if(loanItem.getLoan().getLoanId() == loan.getLoanId())
                        {
                            if(loan.getReturnedOn() == null)
                            {
                                if(loan.getUser().getId().equals(user.getId())){
                                    for(Item itemi : allItems)
                                    {
                                        if (loan.getLoanId() == loanItem.getLoan().getLoanId()) {

                                                userBooks.add(loanItem.getBook());
                                            }
                                            if (itemi.getBook().getBookRef() == book.getBookRef() && !itemi.isLoaned() && !userBooks.contains(book)) {
                                                item = itemi;
                                            }

                                    }
                                }
                            }
                        }
                    }
                }
                if(userBooks.contains(book)){
                    return "alreadyBorrowed";
                }

                Loan loan = new Loan();
                loan.setUser(user);

                loan.setItem(item);

                Date dt = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(dt);
                c.add(Calendar.DATE, 5);
                dt = c.getTime();
                loan.setDueDate(dt);


                loanService.save(loan);

                LoanItem loanItem = new LoanItem();
                loanItem.setBook(book);

                loanItem.setLoan(loan);

                loanItemService.save(loanItem);

                book.setStockLevel(book.getStockLevel() - 1);

                bookService.save(book);

                item.setLoaned(true);

                itemService.save(item);
                userService.save(user);

                loanService.confirmation(loan, user);
                model.addAttribute("loan", loan);
                model.addAttribute("user", user);
            }
            else{
                return"index";
            }
        }


        return "loanConfirmation";
    }


    @RequestMapping("/browse")
    public String browseLibrary(Model model){
        List<Book> bookList = bookService.findAll();
        model.addAttribute("bookList", bookList);
        return "browseLibrary";
    }


    @RequestMapping("/bookDetail")
    public String bookDetail(@PathParam("bookRef") int bookRef, Model model, Principal principal)
    {
        if(principal != null)
        {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        Book book = bookService.findOne(bookRef);


        model.addAttribute("book", book);

        return "bookDetail";

    }

    @RequestMapping("/alreadyBorrowed")
    public String alreadyBorrowed(Model model){
        return "alreadyBorrowed";
    }

    @RequestMapping("/returnBook")
    public String returnBook(@RequestParam("bookRef") int bookRef, Model model, Principal principal)
    {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        Book book = bookService.findOne(bookRef);
        ArrayList<Item> allItems = itemService.findAll();
        ArrayList<Book> userBooks = new ArrayList<>();
        ArrayList<LoanItem> allLoanItems = loanItemService.findAll();
        ArrayList<Loan> allLoans = loanService.findAll();
        user.setNumberOfLoans(user.getNumberOfLoans() + 1);


        for(LoanItem loanItem : allLoanItems)
        {
            if(loanItem.getBook().getBookRef() == bookRef){

                for(Loan loan : allLoans){
                    if(loanItem.getLoan().getLoanId() == loan.getLoanId()){
                        Date returned = new Date();
                        loan.setReturnedOn(returned);
                        loanService.save(loan);
                        for(Item item : allItems)
                        {
                            if(item.getBookId() == loan.getItem().getBookId())
                            {
                                item.setLoaned(false);
                                itemService.save(item);
                            }
                        }
                    }
                }
            }
        }
        book.setStockLevel(book.getStockLevel() +1);
        bookService.save(book);

        model.addAttribute("bookList", userBooks);
        model.addAttribute("user", user);

        model.addAttribute("classActiveEdit", true);

        model.addAttribute("classActiveBooks", true);

        return "myProfile";
    }

    @RequestMapping("/reserveBook")
    public String reserveBook(@RequestParam("bookRef") int bookRef, Model model, Principal principal)
    {
        Book book = bookService.findOne(bookRef);
        ArrayList<Item> allItems = itemService.findAll();
        ArrayList<Loan> allLoans = loanService.findAll();
        String username = principal.getName();
        ArrayList<Item> checkItems = new ArrayList<>();

        User user = userService.findByUsername(username);
        Item reserveredItem = new Item();
        Date reservationDate = new Date();

        for(Item item : allItems)
        {
            for(Loan loan: allLoans)
            {
                if(bookRef == item.getBook().getBookRef()){
                if(item.getBookId() == loan.getItem().getBookId())
                {
                    checkItems.add(item);

                }}

            }
        }
        if(checkItems.isEmpty())
        {
            return "reserveNoStock";
        }
        for(Loan loan : allLoans)
        {
            if(loan.getItem().getBook().getBookRef() == book.getBookRef())
            {
                reservationDate = loan.getDueDate();
            }
        }
        for(Loan loan : allLoans)
        {

            if(loan.getItem().getBook().getBookRef() == book.getBookRef())
            {
                if(reservationDate.compareTo(loan.getDueDate()) > 0)
                {
                    reservationDate = loan.getDueDate();

                }
                reserveredItem = itemService.findOne(loan.getItem().getBookId());

            }
        }
        Reservation reservation = new Reservation();
        reservation.setReservedBy(user);
        reservation.setReservedFor(reservationDate);

        reservation.setItem(reserveredItem);

        reservationService.save(reservation);

        model.addAttribute("reservation", reservation);
        return "reservedSuccess";

    }


}