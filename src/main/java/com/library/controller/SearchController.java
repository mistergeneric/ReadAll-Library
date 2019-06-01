package com.library.controller;

import com.library.domain.Book;
import com.library.domain.User;
import com.library.service.BookService;
import com.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;
    @RequestMapping("/searchByCategory")
    public String searchByCategory(@RequestParam("category") String category, Model model, Principal principal)
    {
        if(principal != null)
        {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        String classActiveCategory = "active" + category;

        classActiveCategory = classActiveCategory.replaceAll("\\s+", "");
        classActiveCategory = classActiveCategory.replaceAll("&", "");
        model.addAttribute(classActiveCategory, true);

        List<Book> bookList = bookService.findByCategory(category);

        if(bookList.isEmpty())
        {
            model.addAttribute("emptyList", true);
            return "library/browseLibrary";
        }

        model.addAttribute("bookList", bookList);

        return "library/browseLibrary";
    }

    @RequestMapping("/searchBook")
    public String searchBook(@ModelAttribute("keyword") String keyword, Principal principal, Model model)
    {
        if(principal != null)
        {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        List<Book> bookList = bookService.blurrySearch(keyword);

        if(bookList.isEmpty())
        {
            model.addAttribute("emptyList", true);
            model.addAttribute("nothingFound", true);
            return "library/browseLibrary";
        }

        model.addAttribute("somethingFound", true);
        model.addAttribute("bookList", bookList);

        return "library/browseLibrary";
    }

}
