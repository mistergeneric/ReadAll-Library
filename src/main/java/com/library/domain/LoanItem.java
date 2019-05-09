package com.library.domain;

import javax.persistence.*;

@Entity
public class LoanItem {

    @Id
    @Column(name="loanItemId", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int loanItemId;

    @OneToOne
    @JoinColumn(name ="book_ref")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    public void setBook(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public int getLoanItemId() {
        return loanItemId;
    }

    public void setLoanItemId(int loanItemId) {
        this.loanItemId = loanItemId;
    }
}
