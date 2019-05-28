package com.library.service;

import com.library.domain.User;
import com.library.domain.security.UserRole;

public interface UserRoleService {
    UserRole findByUser(User user);
    UserRole save(UserRole userRole);
}
