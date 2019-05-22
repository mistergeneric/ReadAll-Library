package com.library.service.impl;

import com.library.domain.UserPayment;
import com.library.repository.UserPaymentRepository;
import com.library.service.UserPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserPaymentServiceImpl implements UserPaymentService {

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Override
    public UserPayment findById(long Id) {
        Optional<UserPayment> userPaymentOptional = userPaymentRepository.findById(Id);
        List<UserPayment> collect = userPaymentOptional.stream().collect(Collectors.toList());
        return collect.get(0);
    }

    @Override
    public void removeById(long Id) {
         userPaymentRepository.removeByPaymentId(Id);
    }
}
