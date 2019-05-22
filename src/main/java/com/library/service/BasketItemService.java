package com.library.service;


import com.library.domain.Basket;
import com.library.domain.BasketItem;

import java.util.List;

public interface BasketItemService {
    List<BasketItem> findByBasket(Basket basket);
    BasketItem save(BasketItem basketItem);
}
