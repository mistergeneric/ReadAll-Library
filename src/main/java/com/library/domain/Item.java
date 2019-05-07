package com.library.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Item {

    @Id
    @Column(name="book_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bookId;
    private int bookRef;




    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBookRef() {
        return bookRef;
    }

    public void setBookRef(int bookRef) {
        this.bookRef = bookRef;
    }


}
