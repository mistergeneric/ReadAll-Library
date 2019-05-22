package com.library.repository;

import com.library.domain.Basket;
import com.library.domain.BasketItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BasketItemRepository extends CrudRepository<BasketItem, Long> {
    List<BasketItem> findByBasket(Basket basket);
}
