package com.library.service;
import com.library.domain.User;
import com.library.domain.UserBilling;
import com.library.domain.UserPayment;
import com.library.domain.security.PasswordResetToken;
import com.library.domain.security.UserRole;

import java.util.List;
import java.util.Set;

public interface UserService {
PasswordResetToken getPasswordResetToken(final String token);

void createPasswordResetTokenForUser(final User user, final String token);

User findByUsername(String username);

User findByEmail (String email);

User createUser (User user, Set<UserRole> userRoles) throws Exception;

List<User> findAll();

User save(User user);

void updateUserBilling(UserBilling userBilling, UserPayment userPayment,User user);

void setDefaultPayment(long DefaultPaymentId, User user);

}
