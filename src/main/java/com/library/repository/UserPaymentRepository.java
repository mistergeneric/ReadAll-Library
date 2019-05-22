package com.library.repository;

import com.library.domain.UserPayment;
import org.springframework.data.repository.CrudRepository;

public interface UserPaymentRepository extends CrudRepository<UserPayment, Long> {
    void removeByPaymentId(long Id);
}
