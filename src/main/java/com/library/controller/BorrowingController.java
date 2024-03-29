package com.library.controller;

import com.library.domain.*;
import com.library.service.*;
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

/**
 * The borrowing controller controls the borrowing acions on items that the user may borrow from the library
 */
@Controller
public class BorrowingController {


    /**
     * This bean allows the system to send us e-mails to users
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * This bean constructs e-mails for the system
     */
    @Autowired
    private MailConstructor mailConstructor;

    /**
     * This bean is the user service
     */
    @Autowired
    private UserService userService;
    /**
     * This bean is the book service
     */
    @Autowired
    private BookService bookService;
    /**
     * This bean is the loan service
     */
    @Autowired
    private LoanService loanService;
    /**
     * This bean is the user security service
     */
    @Autowired
    private UserSecurityService userSecurityService;
    /**
     * This bean is the item service
     */
    @Autowired
    private ItemService itemService;
    /**
     * This bean is the loan item service
     */
    @Autowired
    private LoanItemService loanItemService;
    /**
     * This bean is the reservation service
     */
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private BasketService basketService;


    @Autowired
    private BasketItemService basketItemService;
    /**
     * This method controls the borrowing item functionality.
     *
     * @param model     the html model
     * @param bookRef   the id of the book
     * @param principal the user currently logged in
     * @return
     */

    @RequestMapping("/borrowItem")
    public void borrowItem(Model model, @RequestParam("bookRef") int bookRef, Principal principal) {

        /*Stupidly, I didn't realise I could define my own searches through Crud Repo. Therefore, I use find all to pull out
        every record from loanitem, loan, item and book and iterate through them all to find what the app needs to find
        to process a loan.
        */
        ArrayList<LoanItem> loanItems = loanItemService.findAll();
        ArrayList<Loan> loans = loanService.findAll();
        //if the user is not null
        if (principal != null) {

            String username = principal.getName();
            User user = userService.findByUsername(username);
            Book book = bookService.findOne(bookRef);
            if (book.getStockLevel() == 0) {
                //if the book is not in stock, it calls the reserve book method from within the current controller
                reserveBook(bookRef, model, principal);
            }
            //new item
            Item item = new Item();

            //if the user is still got loans available to him (defined by the account level, then this fires off. (else it redirects)
            if (user.getNumberOfLoans() > 0) {
                ArrayList<Item> allItems = itemService.findAll();


                //Andrew, in the future yuu may be tempted to 'fix' this. DO NOT TOUCH for it works now and you
                //may break it


                //this is where things get wild, iterating through every item in db, to find a copy of the book which is free to be loaned
                for (Item itemi : allItems) {
                    //if the book is free to be loaned
                    if (itemi.getBook().getBookRef() == bookRef && !itemi.isLoaned()) {
                        item = itemi;
                    }
                }
                //this iteration is to check whether the user has borrowed the book already!
                item = bookService.checkBorrowed(item, user, book);
                ArrayList<Book> userBooks = bookService.getUserBooks(item, user, book);


                //if the user already has it borrowed
                if (userBooks.contains(book)) {
                    alreadyBorrowed(model);
                }
                //below we are just adding everything everything and saving it to the basis as it has passed the crazy checks above
                Loan loan = loanService.makeLoan(item, user, book);

                //adding loan and user to the model for the next html page
                loanService.confirmation(loan, user);
                model.addAttribute("loan", loan);
                model.addAttribute("user", user);
                //returning this page if the book is already borrowed
            } else {
                alreadyBorrowed(model);
            }
        }



    }


    /**
     * Simple method returning the browse library page
     * @param model the html model
     * @return browseLibrary html page
     */
    @RequestMapping("/browse")
    public String browseLibrary(Model model) {
        //loading all the books from database and attaching it to the model
        List<Book> bookList = bookService.findAll();
        model.addAttribute("bookList", bookList);
        model.addAttribute("activeAll", true);
        return "library/browseLibrary";
    }


    /**
     * This method loads the book detail page when a book is selected in the book list page. I.e. The specific information
     * about the book selected.
     * @param bookRef the int value representing the book reference number of the book chosen
     * @param model the html model
     * @param principal the logged in user
     * @return an html page for the book detail
     */
    @RequestMapping("/bookDetail")
    public String bookDetail(@PathParam("bookRef") int bookRef, Model model, Principal principal) {
        //if the user is not null
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        //finding the book selected
        Book book = bookService.findOne(bookRef);

        //adding it to the html model
        model.addAttribute("book", book);
        //returning the html page
        return "library/bookDetail";

    }

    /**
     * Loading the already borrowed html page
     * @param model - the html model
     * @return the alreadyBorrowed html page
     */
    @RequestMapping("/alreadyBorrowed")
    public String alreadyBorrowed(Model model) {
        return "alreadyBorrowed";
    }

    /**
     * The method which performs the return book action when the user wishes to return a book. Similar to the borrow book
     * method, it features a complex unneeded for loop.
     * @param bookRef
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping("/returnBook")
    public String returnBook(@RequestParam("bookRef") int bookRef, Model model, Principal principal) {
        /*
        This method performs exactly the same functionality as the borrow book method, but in reverse, so please see that
        method for detailed commentsdunnecunnecesaryunnecesaryry and understanding for the process.
         */

        String username = principal.getName();
        User user = userService.findByUsername(username);
        Book book = bookService.findOne(bookRef);
        //as before, loading everything from the database with the findall method.
        ArrayList<Item> allItems = itemService.findAll();
        ArrayList<Book> userBooks = new ArrayList<>();
        ArrayList<LoanItem> allLoanItems = loanItemService.findAll();
        ArrayList<Loan> allLoans = loanService.findAll();
        user.setNumberOfLoans(user.getNumberOfLoans() + 1);


        //the complex nested for loop to iterate over database items
        for (LoanItem loanItem : allLoanItems) {
            if (loanItem.getBook().getBookRef() == bookRef) {

                for (Loan loan : allLoans) {
                    if (loanItem.getLoan().getLoanId() == loan.getLoanId()) {
                        Date returned = new Date();
                        loan.setReturnedOn(returned);
                        loanService.save(loan);
                        for (Item item : allItems) {
                            if (item.getBookId() == loanItem.getItem().getBookId()) {
                                item.setLoaned(false);
                                itemService.save(item);
                            }
                        }
                    }
                }
            }
        }

        for(Loan x : allLoans)
        {
            if(x.getReturnedOn() == null) {
                if (x.getUser().getId().equals(user.getId())) {
                    for (LoanItem y : allLoanItems) {
                        if (x.getLoanId() == y.getLoan().getLoanId()) {

                            if (!userBooks.contains(y.getBook())) {
                                userBooks.add(y.getBook());
                            }
                        }
                    }
                }
            }
        }

        //increasing stock level as book has been returned
        book.setStockLevel(book.getStockLevel() + 1);

        book.setActiveLoans(book.getActiveLoans() -1);
        bookService.save(book);

        userService.save(user);
        //the html model is getting attributes added
        model.addAttribute("bookList", userBooks);
        model.addAttribute("user", user);

        model.addAttribute("classActiveEdit", true);

        model.addAttribute("classActiveBooks", true);

        return "account/myProfile";
    }

    /**
     * This method is intended for reserving books for future borrowing by users
     * @param bookRef the int id of the intended book to be borrowed
     * @param model the html model
     * @param principal the logged in user
     * @return
     */
    @RequestMapping("/reserveBook")
    public String reserveBook(@RequestParam("bookRef") int bookRef, Model model, Principal principal) {
        Book book = bookService.findOne(bookRef);
        //loading loans and items from the database
        ArrayList<Item> allItems = itemService.findAll();
        ArrayList<Loan> allLoans = loanService.findAll();
        String username = principal.getName();
        ArrayList<Item> checkItems = new ArrayList<>();
        ArrayList<LoanItem> allLoanItems = loanItemService.findAll();
        User user = userService.findByUsername(username);
        Item reserveredItem = new Item();
        Date reservationDate = new Date();

        //seatching all items for copies of the book to be loaned
        for (Item item : allItems) {
            for (Loan loan : allLoans) {
                if (bookRef == item.getBook().getBookRef()) {
                    for (LoanItem loanItem : allLoanItems) {
                        if (item.getBookId() == loanItem.getItem().getBookId()) {
                            //this is used to check if there is no copies of a requested book
                            checkItems.add(item);

                        }
                    }
                }

            }
        }
        //this is for if there is no items of the book for borrowing
        if (checkItems.isEmpty()) {
            return "library/reserveNoStock";
        }
        //this is setting a reservation date variable to the most recently found due date for a copy
        for (Loan loan : allLoans) {
            for(LoanItem loanItem : allLoanItems) {
                if (loanItem.getItem().getBook().getBookRef() == book.getBookRef()) {
                    reservationDate = loan.getDueDate();
                }
            }
        }
        for (Loan loan : allLoans) {
            for (LoanItem loanItem :allLoanItems){
                if (loanItem.getItem().getBook().getBookRef() == book.getBookRef()) {
                    //finding the date for reservation which is closest to now
                    if (reservationDate.compareTo(loan.getDueDate()) > 0) {
                        reservationDate = loan.getDueDate();

                    }
                    reserveredItem = itemService.findOne(loanItem.getItem().getBookId());

                }
            }
        }

        Reservation reservation = new Reservation();
        reservation.setReservedBy(user);
        reservation.setReservedFor(reservationDate);
        for(Reservation r : reservationService.findAll())
        {
            if(r.getReservedFor().equals(reservation.getReservedFor()) && r.getItem().equals(reservation.getItem()))
            {
                reservation.setReservedFor(reservation.addDays(reservation.getReservedFor(), 7));
            }
        }
        reservation.setItem(reserveredItem);

        reservation.setComplete(false);
        //saving the reservation to the database
        reservationService.save(reservation);

        model.addAttribute("reservation", reservation);
        //returning the html reserved page
        return "library/reservedSuccess";

    }

    @RequestMapping("/basket")
    public String basket(Model model, Principal principal)
    {
        User user = userService.findByUsername(principal.getName());
        Basket basket = new Basket();
        List<Basket> baskets = basketService.findUserBasket(user);

        if(baskets.isEmpty())
        {

            model.addAttribute("emptyCart", true);
            return "basket";
        }
        for(Basket b : baskets)
        {
            if(!b.isComplete())
            {
                basket = b;

                ArrayList<BasketItem> basketItems = (ArrayList<BasketItem>) basketItemService.findByBasket(basket);
                for(BasketItem basketItem : basketItems)
                {
                    if(!basketItem.isComplete()) {
                        basket.getBasket().add(basketItem.getBook());
                    }
                }
            }
        }

        model.addAttribute("user", user);

        if(basket == null)
        {
            model.addAttribute("emptyCart", true);
            return "basket";
        }

        if(basket.getBasket() == null)
        {
            model.addAttribute("emptyCart", true);
            return "basket";
        }


        if(basket.getBasket().isEmpty())
        {
            model.addAttribute("emptyCart", true);
            return "basket";
        }

        model.addAttribute("cartItemList", basket.getBasket());

        return "basket";

    }

    @RequestMapping("/addToBasket")
    public String addToBasket(Model model, Principal principal, @RequestParam("bookRef") int bookRef)
    {
        User user = userService.findByUsername(principal.getName());
        Basket basket = new Basket();
        Book book = bookService.findOne(bookRef);

        if(itemService.availabilityChecker(user, book)) {
            for(Basket b : basketService.findUserBasket(user))
            {
                if(!b.isComplete())
                {
                    basket = b;
                    ArrayList<BasketItem> basketItems = (ArrayList<BasketItem>) basketItemService.findByBasket(basket);
                    for(BasketItem bi : basketItems)
                    {
                        if(!bi.isComplete()) {
                            if (bi.getBook() == book) {
                                return "library/alreadyBorrowed";

                            }
                        }
                    }

                }
            }




            ArrayList<Book> basketStuff = new ArrayList<>();
            basket.setBasket(basketStuff);


            BasketItem basketItem = new BasketItem();
            basketItem.setBook(book);
            basket.getBasket().add(bookService.findOne(bookRef));
            basket.setUser(user);
            model.addAttribute("user", user);
            basketItem.setBasket(basket);
            basketService.save(basket);


            basketItemService.save(basketItem);
            model.addAttribute("addedSuccess", true);

            //loading all the books from database and attaching it to the model
            List<Book> bookList = bookService.findAll();
            model.addAttribute("bookList", bookList);
            return "library/browseLibrary";
        }
        else{
            return "library/alreadyBorrowed";
        }
    }

    @RequestMapping("/borrowItems")
    public String borrowItem(Model model, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        Basket basket = new Basket();

        for(Basket b : basketService.findUserBasket(user))
        {
            if(!b.isComplete())
            {
                basket = b;
            }
        }
        ArrayList<BasketItem> basketItems = (ArrayList<BasketItem>) basketItemService.findByBasket(basket);
        for(BasketItem basketItem : basketItems)
        {
            if(!basketItem.isComplete())
            {
                basket.getBasket().add(basketItem.getBook());
            }
        }



        for(Book book : basket.getBasket())
        {
            borrowItem(model, book.getBookRef(), principal);
        }



        for(BasketItem basketItem : basketItems)
        {
            if(!basketItem.isComplete())
            {
                basketItem.setComplete(true);
                basketItemService.save(basketItem);
            }
        }

        for(Basket b : basketService.findUserBasket(user))
        {
            if(!b.isComplete())
            {
                b.setComplete(true);
                basketService.save(b);
            }
        }
        for(Loan loan : loanService.findAll())
        {
            for(LoanItem loanItem : loanItemService.findAll())
            {
                if(basket.getBasket().contains(loanItem.getBook())) {
                    if (loan.getUser() == user && loan.getReturnedOn() == null) {
                        model.addAttribute(loan);
                    }
                }
            }

        }

        //if everything is good, then this page is returned
        return "library/loanConfirmation";

    }

    @RequestMapping("/removeFromBasket")
    public String removeFromBasket(@RequestParam("bookRef") int bookRef, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Basket basket = new Basket();

        for(Basket b : basketService.findUserBasket(user))
        {
            if(!b.isComplete())
            {
                basket = b;
            }
        }
        ArrayList<BasketItem> basketItems = (ArrayList<BasketItem>) basketItemService.findByBasket(basket);
        for(BasketItem bi : basketItems)
        {
            if(!bi.isComplete() && bi.getBook() == bookService.findOne(bookRef))
            {
                bi.setComplete(true);
                basketItemService.save(bi);

            }
        }

        model.addAttribute("cartItemList", basket.getBasket());

        return "basket";
    }

}
