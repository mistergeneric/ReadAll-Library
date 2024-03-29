package com.library.service.impl;

import com.library.domain.Basket;
import com.library.domain.BasketItem;
import com.library.repository.BasketItemRepository;
import com.library.service.BasketItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BasketItemServiceImpl implements BasketItemService {
    @Autowired
    private BasketItemRepository basketItemRepository;

    @Override
    public List<BasketItem> findByBasket(Basket basket) {
        return basketItemRepository.findByBasket(basket);
    }

    @Override
    public BasketItem save(BasketItem basketItem) {
        return basketItemRepository.save(basketItem);
    }

    @Override
    public void deleteByBasket(Basket basket) {
        basketItemRepository.deleteByBasket(basket);
    }
}
