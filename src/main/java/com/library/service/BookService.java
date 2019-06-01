package com.library.service;

import com.library.domain.Book;
import com.library.domain.Item;
import com.library.domain.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();

    Book findOne(int bookRef);

    List<Book> findByCategory(String category);

    List<Book> blurrySearch(String keyword);

    Item checkBorrowed(Item item, User user, Book book);

    ArrayList<Book> getUserBooks(Item item, User user, Book book);

    void deleteByBookRef(int bookRef);

}
