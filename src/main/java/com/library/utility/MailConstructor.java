package com.library.utility;

import com.library.domain.Book;
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

    public SimpleMailMessage lateBookEmail (User user, Loan loan)
    {
        String message = "\n Your book is late! : \n" + loan.getLoanId() + " \n Please return it as soon as possible!";


        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(user.getEmail());
        email.setSubject("Loan confirmation");
        email.setText(message);
        email.setFrom("andrewmcneill1992@gmail.com");

        return email;
    }

    public SimpleMailMessage noDefaultPayment(User user)
    {
        String message = "\n I'm sorry, your payment to ReadAll Library cannot be processed, please pay now or the service will be cancelled in 5 days";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setText(message);
        email.setSubject("Unpaid Membership");
        email.setFrom("andrewmcneill1992@gmail.com");

        return email;
    }
    public SimpleMailMessage noStock(Book book, User user)
    {
        String message = "\n We're really sorry, we have not yet received stock of :" + book.getBookTitle() + "\n As such, your reservation has been cancelled. " +
                "please contact us for more details";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setText(message);
        email.setSubject("Problem With Reservation");
        email.setFrom("andrewmcneill1992@gmail.com");

        return email;
    }

    public SimpleMailMessage noLoans(Book book, User user)
    {
        String message = "\n We're really sorry, you don't have any loans available left on your membership, your loan has been cancelled";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setText(message);
        email.setSubject("Problem With Reservation");
        email.setFrom("andrewmcneill1992@gmail.com");

        return email;

    }

    public SimpleMailMessage failedPayment(User user)
    {
        String message = "\n I'm sorry, your default card is not a valid credit card, please update your card details or the service will be cancelled in 5 days";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setText(message);
        email.setSubject("Incorrect Card Details");
        email.setFrom("andrewmcneill1992@gmail.com");

        return email;
    }




}
