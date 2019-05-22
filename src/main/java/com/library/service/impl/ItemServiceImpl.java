package com.library.service.impl;

import com.library.controller.BorrowingController;
import com.library.domain.*;
import com.library.repository.ItemRepository;
import com.library.service.ItemService;
import com.library.service.LoanItemService;
import com.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanItemService loanItemService;

    @Override
    public ArrayList<Item> findAll() {
        return (ArrayList<Item>) itemRepository.findAll();
    }

    @Override
    public boolean availabilityChecker(User user, Book book) {

        ArrayList<LoanItem> loanItems = loanItemService.findAll();
        ArrayList<Loan> loans = loanService.findAll();

        if (book.getStockLevel() == 0) {
            //if the book is not in stock, it calls the reserve book method from within the current controller
            return false;

        }
        //new item
        Item item = new Item();

        //if the user is still got loans available to him (defined by the account level, then this fires off. (else it redirects)
        if (user.getNumberOfLoans() > 0) {
            user.setNumberOfLoans(user.getNumberOfLoans() - 1);
            ArrayList<Item> allItems = findAll();
            ArrayList<Book> userBooks = new ArrayList<>();


            //Andrew, in the future yuu may be tempted to 'fix' this. DO NOT TOUCH for it works now and you
            //may break it


            //this is where things get wild, iterating through every item in db, to find a copy of the book which is free to be loaned
            for (Item itemi : allItems) {
                //if the book is free to be loaned
                if (itemi.getBook().getBookRef() == book.getBookRef() && !itemi.isLoaned()) {
                    item = itemi;
                }
            }
            //this iteration is to check whether the user has borrowed the book already!
            for (LoanItem loanItem : loanItems) {
                //going through all of the loans
                for (Loan loan : loans) {
                    //going through all of the loanitems and matching it with the parent loan
                    if (loanItem.getLoan().getLoanId() == loan.getLoanId()) {
                        //has the book not been returned yet?
                        if (loan.getReturnedOn() == null) {
                            //if the loan's user = the current user
                            if (loan.getUser().getId().equals(user.getId())) {
                                for (Item itemi : allItems) {

                                    if (loan.getLoanId() == loanItem.getLoan().getLoanId()) {

                                        userBooks.add(loanItem.getBook());
                                    }
                                    //if the item's bookref matches the intended book to loan + the copy is not loaned + the user doesn't already have it loaned
                                    if (itemi.getBook().getBookRef() == book.getBookRef() && !itemi.isLoaned() && !userBooks.contains(book)) {
                                        item = itemi;
                                    }

                                }
                            }
                        }
                    }
                }
            }
            //if the user already has it borrowed
            if (userBooks.contains(book)) {
                return false;
            }
        }        return true;

    }

        @Override
        public Item findOne ( int bookId){
            Optional<Item> itemOptional = itemRepository.findById(bookId);
            List<Item> collect = itemOptional.stream().collect(Collectors.toList());
            return collect.get(0);


        }

        @Override
        public Item save (Item item){
            return itemRepository.save(item);
        }
    }

