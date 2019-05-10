package com.library.service.impl;

import com.library.domain.Book;
import com.library.repository.BookRepository;
import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
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
}
