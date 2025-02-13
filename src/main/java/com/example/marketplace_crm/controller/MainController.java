package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Service.CategoryService;
import com.example.marketplace_crm.Service.ProductService;
import com.example.marketplace_crm.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@RequestMapping("/")
@Tag(name = "Main Controller", description = "Основной контроллер для страницы главной страницы")
public class MainController {
    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;


}
