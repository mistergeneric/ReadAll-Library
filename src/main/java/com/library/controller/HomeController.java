package com.library.controller;

import com.library.domain.*;
import com.library.domain.security.PasswordResetToken;
import com.library.domain.security.Role;
import com.library.domain.security.UserRole;
import com.library.service.*;
import com.library.service.impl.*;
import com.library.utility.MailConstructor;
import com.library.utility.SecurityUtility;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

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

    @Autowired
    private UserPaymentService userPaymentService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }


    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("classActiveLogin", true);
        return "account/myAccount";
    }

    @RequestMapping("/registerDetails")
    public String registerDetails(
            Model model) {
        model.addAttribute("classActiveNewAccount", true);
        return "account/myAccount";
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
                return "account/myAccount";
            }
            if (userService.findByEmail(userEmail) != null) {
                model.addAttribute("emailExists", true);

                return "account/myAccount";
            }

            User user = new User();
            user.setUsername(username);
            user.setEmail(userEmail);

            String password = SecurityUtility.randomPassword();

            String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);

            user.setPassword(encryptedPassword);

            user.setNumberOfLoans(5);
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

            return "account/myAccount";
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
            return "account/myAccount";
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


        return "account/myAccount";
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
        return "account/myProfile";
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


        model.addAttribute("userPaymentList", user.getUserPaymentList());

        model.addAttribute("bookList", customerBooks);
        model.addAttribute("user", user);

        model.addAttribute("classActiveAccountType", true);
        model.addAttribute("classActiveEdit", true);
        model.addAttribute("classActiveBooks", true);

        model.addAttribute("classActiveBilling", true);
        return "account/myProfile";

    }


    @RequestMapping("/aboutus")
    public String aboutUs(Model model){

        return "aboutus";
    }

    @RequestMapping("/contactus")
    public String contactUs(Model model){

        return "contactus";
    }


    @RequestMapping("/listOfCurrentPayments")
    public String listOfCurrentPayments(
            Model model, Principal principal, HttpServletRequest request
    ) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        /*model.addAttribute("orderList", user.orderList());*/

        model.addAttribute("listOfCurrentPayments", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        return "account/myProfile";
    }

    @RequestMapping("/addPaymentOption")
    public String addNewCreditCard(
            Model model, Principal principal
    ){
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("addPaymentOption", true);
        model.addAttribute("classActiveBilling", true);

        UserBilling userBilling = new UserBilling();
        UserPayment userPayment = new UserPayment();


        model.addAttribute("userBilling", userBilling);
        model.addAttribute("userPayment", userPayment);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        /*model.addAttribute("orderList", user.orderList());*/

        return "account/myProfile";
    }

    @RequestMapping(value="/addPaymentOption", method = RequestMethod.POST)
    public String addPaymentOptionPost(@ModelAttribute("userPayment") UserPayment userPayment,
                                       @ModelAttribute("userBilling") UserBilling userBilling,
                                       Principal principal, Model model)
    {
        User user = userService.findByUsername(principal.getName());
        userService.updateUserBilling(userBilling, userPayment, user);

        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("listOfCurrentPayments", true);
        model.addAttribute("classActiveBilling", true);

        return "account/myProfile";

    }

    @RequestMapping("/removeCreditCard")
    public String removeCreditCard(
            @ModelAttribute("id") Long creditCardId, Principal principal, Model model
    ){
        User user = userService.findByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(creditCardId);

        if(!user.getId().equals(userPayment.getUser().getId())) {
            return "badRequestPage";
        } else {
            model.addAttribute("user", user);
            userPaymentService.removeById(creditCardId);

            model.addAttribute("listOfCurrentPayments", true);
            model.addAttribute("classActiveBilling", true);
            model.addAttribute("listOfShippingAddresses", true);

            model.addAttribute("userPaymentList", user.getUserPaymentList());

            return "account/myProfile";
        }
    }
    @RequestMapping(value="/setDefaultPayment", method=RequestMethod.POST)
    public String setDefaultPayment(
            @ModelAttribute("defaultUserPaymentId") Long defaultPaymentId, Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        userService.setDefaultPayment(defaultPaymentId, user);

        model.addAttribute("user", user);
        model.addAttribute("listOfCurrentPayments", true);
        model.addAttribute("classActiveBilling", true);

        model.addAttribute("userPaymentList", user.getUserPaymentList());

        return "account/myProfile";
    }





}
