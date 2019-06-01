package com.library.domain;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
public class Book {

    @Id
    @Column(name="book_ref", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bookRef;
    private String bookTitle;
    private String bookAuthor;
    private int stockLevel;
    private String publisher;
    private String publicationDate;
    private String language;
    private String category;
    private int noOfPages;
    private String format;
    private double cost;
    private boolean active = true;
    private int noOfLoans;

    private int activeLoans;

    public int getActiveLoans() {
        return activeLoans;
    }

    public void setActiveLoans(int activeLoans) {
        this.activeLoans = activeLoans;
    }

    public int getNoOfLoans() {
        return noOfLoans;
    }

    public void setNoOfLoans(int noOfLoans) {
        this.noOfLoans = noOfLoans;
    }

    @Column(columnDefinition = "text")
    private String description;

    @Transient
    private MultipartFile bookImage;

    public Book() {

    }

    public int getBookRef() {
        return bookRef;
    }

    public void setBookRef(int bookRef) {
        this.bookRef = bookRef;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(int noOfPages) {
        this.noOfPages = noOfPages;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getBookImage() {
        return bookImage;
    }

    public void setBookImage(MultipartFile bookImage) {
        this.bookImage = bookImage;
    }

}
