package com.library.service.impl;

import com.library.domain.Book;

import java.util.List;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();

}
