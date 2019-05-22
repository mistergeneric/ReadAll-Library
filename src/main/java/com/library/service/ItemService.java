package com.library.service;

import com.library.domain.Book;
import com.library.domain.Item;
import com.library.domain.User;

import java.util.ArrayList;
import java.util.List;

public interface ItemService {

    ArrayList<Item> findAll();

    boolean availabilityChecker(User user, Book book);
    Item findOne(int bookId);
    Item save(Item item);

}
