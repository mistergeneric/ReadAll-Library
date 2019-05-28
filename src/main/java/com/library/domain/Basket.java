package com.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long basketId;

    @OneToMany(mappedBy="bookRef", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JsonIgnore
    private List<Book> basket;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;



    private boolean complete;

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public Long getBasketId() {
        return basketId;
    }

    public void setBasketId(Long basketId) {
        this.basketId = basketId;
    }

    public List<Book> getBasket() {
        return basket;
    }

    public void setBasket(List<Book> basket) {
        this.basket = basket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
