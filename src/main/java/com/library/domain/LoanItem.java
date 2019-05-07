package com.library.domain;

import javax.persistence.*;

@Entity
public class LoanItem {

    @Id
    @Column(name="loanItemId", nullable = false, updatable = false)
    private int loanItemId;

    @OneToOne
    @JoinColumn(name ="book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;
}
