package com.library.service;

import com.library.domain.Basket;
import com.library.domain.Item;
import com.library.domain.User;

import java.util.List;

public interface BasketService {
    List<Basket> findUserBasket(User user);
    Basket save(Basket basket);
    void deleteByUser(User user);
}
