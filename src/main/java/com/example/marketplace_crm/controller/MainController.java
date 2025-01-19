package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Service.CategoryService;
import com.example.marketplace_crm.Service.ProductService;
import com.example.marketplace_crm.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Data
@RequestMapping("/")
@Tag(name = "Main Controller", description = "Основной контроллер для страницы главной страницы")
public class MainController {
    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Operation(
            summary = "Главная страница",
            description = "Возвращает главную страницу с продуктами и категориями"
    )
    @GetMapping("/")
    public String MainPage(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "main";
    }
}
