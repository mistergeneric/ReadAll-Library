package com.library;

import com.library.domain.User;
import com.library.domain.security.Role;
import com.library.domain.security.UserRole;
import com.library.service.impl.UserService;
import com.library.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication

//main class for building the site
    public class LibraryApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

        public static void main (String[] args){
            SpringApplication.run(com.library.LibraryApplication.class, args);
        }
        @Override
        public void run (String... args) throws Exception{
            User user1 = new User();
            user1.setFirstName("Test");
            user1.setLastName("Test");
            user1.setUsername("test");
            user1.setPassword(SecurityUtility.passwordEncoder().encode("p"));
            user1.setStreet("1 Test Lane");
            user1.setTown("Test Town");
            user1.setPostcode("T");
            user1.setEmail("mcneillwins@gmail.com");
            Set<UserRole> userRoles = new HashSet<>();
            Role role1 = new Role();
            role1.setRoleId(1);
            role1.setName("ROLE_USER");
            userRoles.add(new UserRole(user1, role1));

           //userService.createUser(user1, userRoles);

        }
}
