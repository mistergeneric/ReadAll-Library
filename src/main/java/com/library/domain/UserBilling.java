package com.library.domain;

import javax.persistence.*;

@Entity
public class UserBilling {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long billingId;
    private String userBillingName;
    private String userBillingStreet1;
    private String getUserBillingStreet2;
    private String userBillingCity;
    private String getUserBillingPostcode;

    @OneToOne(cascade = CascadeType.ALL)
    private UserPayment userPayment;

    public Long getBillingId() {
        return billingId;
    }

    public void setBillingId(Long billingId) {
        this.billingId = billingId;
    }

    public String getUserBillingName() {
        return userBillingName;
    }

    public void setUserBillingName(String userBillingName) {
        this.userBillingName = userBillingName;
    }

    public String getUserBillingStreet1() {
        return userBillingStreet1;
    }

    public void setUserBillingStreet1(String userBillingStreet1) {
        this.userBillingStreet1 = userBillingStreet1;
    }

    public String getGetUserBillingStreet2() {
        return getUserBillingStreet2;
    }

    public void setGetUserBillingStreet2(String getUserBillingStreet2) {
        this.getUserBillingStreet2 = getUserBillingStreet2;
    }

    public String getUserBillingCity() {
        return userBillingCity;
    }

    public void setUserBillingCity(String userBillingCity) {
        this.userBillingCity = userBillingCity;
    }

    public String getGetUserBillingPostcode() {
        return getUserBillingPostcode;
    }

    public void setGetUserBillingPostcode(String getUserBillingPostcode) {
        this.getUserBillingPostcode = getUserBillingPostcode;
    }

    public UserPayment getUserPayment() {
        return userPayment;
    }

    public void setUserPayment(UserPayment userPayment) {
        this.userPayment = userPayment;
    }
}
