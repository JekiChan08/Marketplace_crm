package com.example.marketplace_crm.controller;


import com.example.marketplace_crm.Service.CategoryService;
import com.example.marketplace_crm.Service.ProductService;
import com.example.marketplace_crm.Service.UserService;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Data
@RequestMapping("/")
public class MainController {
    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String MainPage(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "main";
    }
}
