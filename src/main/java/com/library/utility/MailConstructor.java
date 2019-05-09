package com.library.utility;

import com.library.domain.Loan;
import com.library.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MailConstructor {

    @Autowired
    private Environment env;

    public SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user, String password){
        String url = contextPath + "/newAccount?token=" + token;

        String message = "\nPlease click on this link to verify your email and edit your personal information. Your password is: \n" + password;
        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(user.getEmail());
        email.setSubject("Welcome to Readall Library");
        email.setText(url+message);
        email.setFrom(env.getProperty("support.email"));

        return email;
    }

    public SimpleMailMessage constructOrderConfirmationEmail(User user, Loan loan)
    {
        String message = "\n Your loan has been successful, your loan reference number is : \n" + loan.getLoanId() + " \n Please return it by: \n" + loan.getDueDate();
        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(user.getEmail());
        email.setSubject("Loan confirmation");
        email.setText(message);
        email.setFrom("andrewmcneill1992@gmail.com");

        return email;
    }
}
