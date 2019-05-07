package com.library.service.impl;

import com.library.domain.User;
import com.library.domain.security.PasswordResetToken;
import com.library.domain.security.UserRole;
import com.library.repository.PasswordResetTokenRepository;
import com.library.repository.RoleRepository;
import com.library.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;


    @Override
    public PasswordResetToken getPasswordResetToken(final String token){
        return passwordResetTokenRepository.findByToken(token);

    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token)
    {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception{
        User localUser = userRepository.findByUsername(user.getUsername());

        if(localUser != null)
        {
            LOG.info("user {} already exists, nothing will be done", user.getUsername());
        }else{
            for(UserRole ur : userRoles){
                roleRepository.save(ur.getRole());
            }
        }

        user.getUserRoles().addAll(userRoles);

        localUser = userRepository.save(user);

        return localUser;
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
