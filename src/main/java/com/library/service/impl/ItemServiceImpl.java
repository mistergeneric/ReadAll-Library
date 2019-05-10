package com.library.service.impl;

import com.library.domain.Item;
import com.library.repository.ItemRepository;
import com.library.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public ArrayList<Item> findAll() {
        return (ArrayList<Item>) itemRepository.findAll();
    }

    @Override
    public Item findOne(int bookId) {
       Optional<Item> itemOptional = itemRepository.findById(bookId);
        List<Item> collect = itemOptional.stream().collect(Collectors.toList());
        return collect.get(0);


    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }
}
