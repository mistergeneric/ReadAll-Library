package com.library.service;

import com.library.domain.UserPayment;

public interface UserPaymentService {
    UserPayment findById(long Id);

    void removeById(long Id);
}
