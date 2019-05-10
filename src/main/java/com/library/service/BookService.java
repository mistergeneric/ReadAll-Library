package com.library.service;

import com.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();

    Book findOne(int bookRef);

}
