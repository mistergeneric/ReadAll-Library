package com.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.library.domain.security.UserRole;
import org.hibernate.engine.profile.Fetch;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Loan {

    @Id
    @Column(name="loan_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
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


    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

}
