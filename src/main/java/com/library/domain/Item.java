package com.library.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Item {

    @Id
    @Column(name="book_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bookId;
    @OneToOne
    @JoinColumn(name="book_ref")
    private Book book;
    private boolean isLoaned;




    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }



    public boolean isLoaned() {
        return isLoaned;
    }

    public void setLoaned(boolean loaned) {
        isLoaned = loaned;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
