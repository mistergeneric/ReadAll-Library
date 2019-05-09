package com.library.service.impl;

import com.library.domain.Book;
import com.library.domain.Item;

import java.util.ArrayList;
import java.util.List;

public interface ItemService {

    ArrayList<Item> findAll();

    Item findOne(int bookId);
    Item save(Item item);

}
