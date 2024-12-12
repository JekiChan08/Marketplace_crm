package com.example.marketplace_crm.controller;


import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.User;
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
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private final ProductService productService;
    @Autowired
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public String getById(@PathVariable String id, Model model){
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("new_product", new Product());
        model.addAttribute("all_categories", categoryService.getAllCategory());
        return "add-product";
    }
    @PostMapping("/add")
    public String save(@ModelAttribute Product product, Model model){
        productService.saveProduct(product);
        model.addAttribute("text", "Успешно сохронено");
        return "redirect:/products/add";
    }
    @GetMapping("/all_products")
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "products";
    }
}
