package com.library.repository;

import com.library.domain.User;
import com.library.domain.security.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

    UserRole findByUser(User user);
    UserRole save(UserRole userRole);

}
