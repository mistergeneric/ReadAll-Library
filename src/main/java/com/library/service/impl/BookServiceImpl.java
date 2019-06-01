package com.library.service.impl;

import com.library.domain.*;
import com.library.repository.BookRepository;
import com.library.repository.LoanItemRepository;
import com.library.repository.LoanRepository;
import com.library.service.BookService;
import com.library.service.ItemService;
import com.library.service.LoanItemService;
import com.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanItemService loanItemService;

    @Autowired
    private ItemService itemService;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);

    }

    @Override
    public List<Book> findAll() {
        return (List<Book>) bookRepository.findAll();
    }

    @Override
    public Book findOne(int bookRef){

        Optional<Book> bookOptional = bookRepository.findById(bookRef);

        List<Book> collect = bookOptional.stream().collect(Collectors.toList());

        return collect.get(0);
    }

    @Override
    public List<Book> findByCategory(String category) {
        return bookRepository.findByCategory(category);
    }

    @Override
    public List<Book> blurrySearch(String keyword){
        return bookRepository.findByBookTitleContaining(keyword);
    }

    public ArrayList<Book> getUserBooks(Item item, User user, Book book) {
        ArrayList<Book> userBooks = new ArrayList<>();

        ArrayList<LoanItem> loanItems = loanItemService.findAll();
        ArrayList<Loan> allLoans = loanService.findAll();

        ArrayList<Item> allItems = itemService.findAll();
        for (LoanItem loanItem : loanItems) {
            //going through all of the loans
            for (Loan loan : allLoans) {
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


                            }
                        }
                    }
                }
            }
        }
        return userBooks;
    }

    @Override
    @Transactional
    public void deleteByBookRef(int bookRef) {
        bookRepository.deleteByBookRef(bookRef);

    }

    @Override
    public Item checkBorrowed(Item item, User user, Book book) {
        ArrayList<Book> userBooks = new ArrayList<>();

        ArrayList<LoanItem> loanItems = loanItemService.findAll();
        ArrayList<Loan> allLoans = loanService.findAll();

        ArrayList<Item> allItems = itemService.findAll();
        for (LoanItem loanItem : loanItems) {
            //going through all of the loans
            for (Loan loan : allLoans) {
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
        return item;
    }
}
