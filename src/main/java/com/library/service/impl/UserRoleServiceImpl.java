package com.library.service.impl;

import com.library.domain.User;
import com.library.domain.security.UserRole;
import com.library.repository.UserRoleRepository;
import com.library.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserRole findByUser(User user) {
        return userRoleRepository.findByUser(user);
    }

    @Override
    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }
}
