package com.library.controller;

import com.library.domain.Book;
import com.library.domain.Item;
import com.library.service.impl.BookService;
import com.library.service.impl.BookServiceImpl;
import com.library.service.impl.ItemService;
import com.library.service.impl.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Controller
@RequestMapping("/")
public class AdminController {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private ItemServiceImpl itemService;

    @RequestMapping(value="/add", method = RequestMethod.GET)
    public String addBook(Model model){
        Book book = new Book();
        model.addAttribute("book", book);
        return "admin/addBook";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String addBookPost(@ModelAttribute("book") Book book, HttpServletRequest request){
        bookService.save(book);
        for(int i=0; book.getStockLevel() > i; i++)
        {
            Item item = new Item();
            item.setBookRef(book.getBookRef());
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


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/admin" )
    public String admin(Model model){

        return "admin/adminHome";
    }

    @RequestMapping("/bookList")
    public String bookList(Model model){
        List<Book> bookList = bookService.findAll();
        model.addAttribute("bookList", bookList);
        return "admin/bookList";
    }
}
