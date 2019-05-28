package com.library.controller;

import com.library.domain.Book;
import com.library.domain.Item;
import com.library.domain.User;
import com.library.domain.security.Role;
import com.library.domain.security.UserRole;
import com.library.service.RoleService;
import com.library.service.UserRoleService;
import com.library.service.UserService;
import com.library.service.impl.BookServiceImpl;
import com.library.service.impl.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


/**
 * This controller controls the admin pages for the web application
 */
@Controller
public class AdminController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    /**
     * autowired annotation allows spring framework to resolve and use beans outwith the current bean
     * in this case the book service bean as a private property
     */
    @Autowired
    private BookServiceImpl bookService;


    /**
     * item service bean as private property
     */
    @Autowired
    private ItemServiceImpl itemService;

    /**
     * This is the controller for loading the admin's add book html page
     * @param model the model allows us to alter the html page for the client server side
     * @return returns the valid html page with valid attributes for the html
     */
    @RequestMapping(value="/add", method = RequestMethod.GET)
    public String addBook(Model model){
        Book book = new Book();
        model.addAttribute("book", book);
        return "admin/addBook";
    }

    /**
     * this is the post action for the add book page, if the submit button is clicked.
     * Once submit is pressed, the valid number of book copies (Items) are created and saved into the database.
     * The new book is added to the database.
     * The book image is addded to the web app using the MultipartFile object.
     * The redirect loads the admin Book list page.
     * @param book - the book to be saved to the database
     * @param request - the http request
     * @return - The html page to be loaded for the client, in this case the admin book list page.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String addBookPost(@ModelAttribute("book") Book book, HttpServletRequest request){

        bookService.save(book);
        for(int i=0; book.getStockLevel() > i; i++)
        {
            Item item = new Item();
            item.setBook(book);
            itemService.save(item);
        }

        MultipartFile bookImage = book.getBookImage();
        try{
            byte[] bytes = bookImage.getBytes();
            String name = book.getBookRef() + ".png";
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/images/book/" + name)));
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }


        return "redirect:bookList";
    }


    /**
     * This method returns the admin home page.
     * @param model The model allows us to alter the html page
     * @return The admin home html page is returned for the client.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/admin" )
    public String admin(Model model){

        return "admin/adminHome";
    }

    /**
     * This method returns the book list html page for the admin.
     * First, this method runs the find all method from book service to load all the books in the library.
     * The model instance has the list of books added to it so it can be correctly displayed on the html page
     * @param model The Model object allows the back end to add attributes and commands to the front end html page
     * @return The book list html page is returned
     */
    @RequestMapping("/bookList")
    public String bookList(Model model){
        List<Book> bookList = bookService.findAll();
        model.addAttribute("bookList", bookList);
        return "admin/bookList";
    }


    /**
     * This method loads the update book page. It uses the book reference number to find the book from the database.
     * It does this by passing the bookref variable into the book service find one method.
     * It adds the found book to the model by calling the addattribute method and using bookref as the parameter.
     * It then returns the update book html page
     * @param bookRef int variable from the previous html page
     * @param model The Model object allows the back end to add attributes and commands to the front end html page
     * @return the update book html page
     */
    @RequestMapping("/updateBook")
    public String updateBook(@RequestParam("bookRef") int bookRef, Model model)
    {
        Book book = bookService.findOne(bookRef);
        model.addAttribute("book", book);

        return "admin/updateBook";
    }


    /**
     * This method is for displaying the book info page to the user.
     * It uses the book reference number to find the book from the database.
     * It does this by passing the bookref variable into the book service find one method.
     * It adds the found book to the model by calling the addattribute method and using bookref as the parameter.
     * It then returns the book info html page
     * @param bookRef int variable from the previous html page
     * @param model The Model object allows the back end to add attributes and commands to the front end html page
     * @return the book info html page
     */
    @RequestMapping("/bookInfo")
    public String bookInfo(@PathParam("bookRef") int bookRef, Model model){

        Book book = bookService.findOne(bookRef);

        model.addAttribute("book", book);
        return "admin/bookInfo";

    }

    @RequestMapping("/manageUsers")
    public String manageUsers(Model model) {
        List<User> userList = userService.findAll();

        model.addAttribute("userList", userList);
        return "admin/manageUsers";
    }

    @RequestMapping("/suspend")
    public String suspend(Model model, @RequestParam("username") String username)
    {


        User user = userService.findByUsername(username);
        if(user.getEnabled()) {
            user.setEnabled(false);
            userService.save(user);

            List<User> userList = userService.findAll();

            UserRole userRole = userRoleService.findByUser(user);

            Role role = new Role();
            role.setRoleId(5);
            user.getUserRoles().clear();


            userRole.setRole(role);
            userRole.setUser(user);


            user.getUserRoles().add(userRole);


            roleService.save(role);
            userService.save(user);
            userRoleService.save(userRole);


            model.addAttribute("userList", userList);
            return "admin/manageUsers";
        }
        else{
            user.setEnabled(true);
            userService.save(user);

            List<User> userList = userService.findAll();

            UserRole userRole = userRoleService.findByUser(user);

            Role role = new Role();
            role.setRoleId(1);
            user.getUserRoles().clear();


            userRole.setRole(role);
            userRole.setUser(user);


            user.getUserRoles().add(userRole);


            roleService.save(role);
            userService.save(user);
            userRoleService.save(userRole);


            model.addAttribute("userList", userList);
            return "admin/manageUsers";

        }
    }

    @RequestMapping("/bookReport")
    public String bookReport(Model model){
        List<Book> bookList = bookService.findAll();
        model.addAttribute("bookList", bookList);

        return "admin/bookReport";
    }





}
