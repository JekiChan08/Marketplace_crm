package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Service.Impl.CategoryServiceImpl;
import com.example.marketplace_crm.Service.Impl.ProductServiceImpl;
import com.example.marketplace_crm.Service.Impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@RequestMapping("/")
@Tag(name = "Main Controller", description = "Основной контроллер для страницы главной страницы(пустой)")
public class MainController {
    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;
    private final CategoryServiceImpl categoryService;


}
