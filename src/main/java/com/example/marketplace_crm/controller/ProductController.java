package com.example.marketplace_crm.controller;


import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Service.CategoryService;
import com.example.marketplace_crm.Service.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

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
    public String addProduct(@ModelAttribute Product product,
                              @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            // Преобразуем файл в строку Base64
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            product.setImage(encodedImage);  // Сохраняем изображение как строку Base64
        }
        productService.saveProduct(product);
        return "redirect:/products/add";
    }

    @GetMapping("/list")
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProduct();
        if (products != null) {
            model.addAttribute("isProduct", true);
            model.addAttribute("products", products);
            model.addAttribute("all_categories", categoryService.getAllCategory());
        }else {
            model.addAttribute("isProduct", false);
            model.addAttribute("product", null);
            model.addAttribute("all_categories", categoryService.getAllCategory());
        }
        return "products";
    }
    @GetMapping("/list/search")
    public String findByNameContaining(Model model, @RequestParam(required = false) String query) {
        if (query != null && !query.isEmpty()) {
            List<Product> products = productService.findByNameContaining(query);
            if (products != null && !products.isEmpty()) {
                model.addAttribute("isProduct", true);
                model.addAttribute("products", products);
            } else {
                model.addAttribute("isProduct", false);
                model.addAttribute("products", null);
            }
        }
        return "products";
    }


    @GetMapping("/{id}/image")
    public ResponseEntity<String> getProductImage(@PathVariable String id) {
        Product product = productService.findById(id);
        if (product != null && product.getImage() != null) {
            String base64Image = product.getImage();
            return new ResponseEntity<>(base64Image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
