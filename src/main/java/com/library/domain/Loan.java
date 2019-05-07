package com.library.domain;

import org.hibernate.engine.profile.Fetch;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Loan {

    @Id
    @Column(name="loan_id", nullable = false, updatable = false)
    private int loanId;

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
}
