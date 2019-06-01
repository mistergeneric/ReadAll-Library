package com.library.repository;

import com.library.domain.Book;
import com.library.domain.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Integer> {
    void deleteByBook(Book book);
}
