package com.library.repository;

import com.library.domain.Basket;
import com.library.domain.Book;
import com.library.domain.Item;
import org.springframework.data.repository.CrudRepository;
import com.library.domain.User;

import java.util.List;

public interface BasketRepository extends CrudRepository<Basket, Long> {
    List<Basket> findByUser(User user);
    void deleteByUser(User user);
}
