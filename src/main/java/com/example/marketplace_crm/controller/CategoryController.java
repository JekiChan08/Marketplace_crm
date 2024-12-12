package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Service.CategoryService;
import com.example.marketplace_crm.Service.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@Data
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private final ProductService productService;
    @Autowired
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public String getById(@PathVariable String id, Model model){
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        model.addAttribute("all_products", category.getProducts());
        return "category";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("new_category", new Category());
        return "add-category";
    }
    @PostMapping("/add")
    public String save(@ModelAttribute Category category, Model model){
        categoryService.saveCategory(category);
        model.addAttribute("text", "Успешно сохронено");
        return add(model);
    }

    @GetMapping("/all_categories")
    public String getAllCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        return "categories";
    }
}
