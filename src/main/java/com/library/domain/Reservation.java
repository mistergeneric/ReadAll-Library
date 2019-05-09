package com.library.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Reservation {

    @Id
    @Column(name="reservation_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int reservationId;


    private Date reservedFor;

    @OneToOne
    @JoinColumn(name="user_id")
    private User reservedBy;

    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

    public User getReservedBy() {
        return reservedBy;
    }

    public void setReservedBy(User reservedBy) {
        this.reservedBy = reservedBy;
    }

    public Date getReservedFor() {
        return reservedFor;
    }

    public void setReservedFor(Date reservedFor) {
        this.reservedFor = reservedFor;
    }
}
