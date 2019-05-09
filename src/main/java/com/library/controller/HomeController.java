package com.library.controller;

import com.library.domain.*;
import com.library.domain.security.PasswordResetToken;
import com.library.domain.security.Role;
import com.library.domain.security.UserRole;
import com.library.service.impl.*;
import com.library.utility.MailConstructor;
import com.library.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.lang.reflect.Array;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

//this is the home controller which returns the index page
@Controller
public class HomeController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private LoanItemService loanItemService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private ItemService itemService;


    @RequestMapping("/")
    public String index() {
        return "index";
    }


    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("classActiveLogin", true);
        return "myAccount";
    }

    @RequestMapping("/registerDetails")
    public String registerDetails(
            Model model) {
        model.addAttribute("classActiveNewAccount", true);
        return "myAccount";
    }

    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public String newUserPost(HttpServletRequest request, @ModelAttribute("email") String userEmail, @ModelAttribute("username") String username, Model model)
            throws Exception {
        {
            model.addAttribute("classActiveNewAccount", true);
            model.addAttribute("email", userEmail);
            model.addAttribute("username", username);
            if (userService.findByUsername(username) != null) {
                model.addAttribute("usernameExists", true);
                return "myAccount";
            }
            if (userService.findByEmail(userEmail) != null) {
                model.addAttribute("emailExists", true);

                return "myAccount";
            }

            User user = new User();
            user.setUsername(username);
            user.setEmail(userEmail);

            String password = SecurityUtility.randomPassword();

            String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);

            user.setPassword(encryptedPassword);

            Role role = new Role();
            role.setRoleId(1);
            role.setName("ROLE_USER");
            Set<UserRole> userRoles = new HashSet<>();

            userRoles.add(new UserRole(user, role));
            userService.createUser(user, userRoles);

            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);

            String appUrl ="http://" +request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

            SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);

            mailSender.send(email);

            model.addAttribute("user", user);

            model.addAttribute("emailSent", true);

            return "myAccount";
        }
    }

    @RequestMapping("/forgetPassword")
    public String forgetPassword(
        HttpServletRequest request,
                @ModelAttribute("email") String mail,
                        Model model){
        model.addAttribute("classActiveForgotPassword", true);

        User user = userService.findByEmail(mail);

        if(user == null)
        {
            model.addAttribute("emailNotExist", true);
            return "myAccount";
        }

        String password = SecurityUtility.randomPassword();

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);

        user.setPassword(encryptedPassword);

        userService.save(user);
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String appUrl ="http://" +request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);

        mailSender.send(newEmail);


        model.addAttribute("forgetPasswordEmailSent", true);


        return "myAccount";
    }

    @RequestMapping("/newAccount")
    public String newAccount(Locale locale,
                             @RequestParam("token") String token, Model model) {
        PasswordResetToken passToken = userService.getPasswordResetToken(token);

        if (passToken == null) {
            String message = "invalid token";
            model.addAttribute("message", message);
            return "redirect:/badRequest";
        }

        User user = passToken.getUser();
        String username = user.getUsername();

        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);


        model.addAttribute("user", user);

        model.addAttribute("classActiveEdit", true);
        return "myProfile";
    }

    @RequestMapping("/myProfile")
    public String myProfile(Model model, Principal principal)
    {

        String username = principal.getName();
        User user = userService.findByUsername(username);

        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);


        ArrayList<Loan> loans = loanService.findAll();

        ArrayList<LoanItem> loanItems = loanItemService.findAll();

        ArrayList<Book> customerBooks = new ArrayList<>();

        for(Loan x : loans)
        {
            if(x.getReturnedOn() == null) {
                if (x.getUser().getId().equals(user.getId())) {
                    for (LoanItem y : loanItems) {
                        if (x.getLoanId() == y.getLoan().getLoanId()) {

                            if (!customerBooks.contains(y.getBook())) {
                                customerBooks.add(y.getBook());
                            }
                        }
                    }
                }
            }
        }





        model.addAttribute("bookList", customerBooks);
        model.addAttribute("user", user);

        model.addAttribute("classActiveEdit", true);

        model.addAttribute("classActiveBooks", true);
        return "myProfile";

    }


    @RequestMapping("/aboutus")
    public String aboutUs(Model model){

        return "aboutus";
    }

    @RequestMapping("/contactus")
    public String contactUs(Model model){

        return "contactus";
    }




}
