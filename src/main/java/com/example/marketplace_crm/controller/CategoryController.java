package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
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
import java.util.Base64;
import java.util.List;

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
    public String addCategory(@ModelAttribute Category category,
                              @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            // Преобразуем файл в строку Base64
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            category.setImage(encodedImage);  // Сохраняем изображение как строку Base64
        }
        categoryService.saveCategory(category);
        return "redirect:/categories/add";
    }



    @GetMapping("/list")
    public String getCategories(Model model) {
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        return "categories";
    }
    @GetMapping("/{id}/image")
    public ResponseEntity<String> getCategoryImage(@PathVariable String id) {
        Category category = categoryService.findById(id);
        if (category != null && category.getImage() != null) {
            String base64Image = category.getImage();  // Получаем строку Base64 изображения
            return new ResponseEntity<>(base64Image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
