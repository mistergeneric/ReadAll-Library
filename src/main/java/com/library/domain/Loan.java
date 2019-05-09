package com.library.domain;

import org.hibernate.engine.profile.Fetch;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Loan {

    @Id
    @Column(name="loan_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int loanId;

    @OneToOne
    @JoinColumn(name="item_id")
    private Item item;

    private Date dueDate;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;


    private Date returnedOn;

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Date getReturnedOn() {
        return returnedOn;
    }

    public void setReturnedOn(Date returnedOn) {
        this.returnedOn = returnedOn;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }
}
