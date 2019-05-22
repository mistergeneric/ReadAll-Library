package com.library.domain;

import javax.persistence.*;

@Entity
public class BasketItem  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long basketItemId;
    @ManyToOne
    @JoinColumn(name = "basket_id")
    private Basket basket;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public long getBasketItemId() {
        return basketItemId;
    }

    public void setBasketItemId(long basketItemId) {
        this.basketItemId = basketItemId;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
