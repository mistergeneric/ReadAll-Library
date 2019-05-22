package com.library.service.impl;

import com.library.domain.Basket;
import com.library.domain.Book;
import com.library.domain.Item;
import com.library.domain.User;
import com.library.repository.BasketRepository;
import com.library.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasketServiceImpl implements BasketService {

    @Autowired
    private BasketRepository basketRepository;

    @Override
    public Basket findUserBasket(User user) {
        return basketRepository.findByUser(user);
    }

    @Override
    public Basket save(Basket basket) {
        return basketRepository.save(basket);
    }
}
