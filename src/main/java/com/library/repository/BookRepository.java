package com.library.repository;

import com.library.domain.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {

    List<Book> findByCategory(String category);
    List<Book> findByBookTitleContaining(String keyword);

    void deleteByBookRef(int bookRef);

}
