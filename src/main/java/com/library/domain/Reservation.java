package com.library.domain;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class Reservation {

    @Id
    @Column(name="reservation_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int reservationId;


    private boolean isComplete;

    private Date reservedFor;

    @OneToOne
    @JoinColumn(name="user_id")
    private User reservedBy;

    @ManyToOne (cascade = {CascadeType.ALL})
    @JoinColumn(name="item_id")
    private Item Item;

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

    public com.library.domain.Item getItem() {
        return Item;
    }

    public void setItem(com.library.domain.Item item) {
        Item = item;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    public Date addDays(Date date, Integer days) {
        if(date == null)
        {
            date = Calendar.getInstance().getTime();
            addDays(date, -30);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
