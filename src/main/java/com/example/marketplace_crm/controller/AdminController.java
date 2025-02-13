package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Service.CategoryService;
import com.example.marketplace_crm.Service.OrderService;
import com.example.marketplace_crm.Service.ProductService;
import com.example.marketplace_crm.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;
    private final CategoryService categoryService;

    @Operation(summary = "Получить список всех продуктов")
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @Operation(summary = "Редактировать продукт")
    @PostMapping("/products/edit/{id}")
    public ResponseEntity<Product> editProduct(@PathVariable String id,
                                               @RequestBody Product product,
                                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        Product oldProduct = productService.findById(id);
        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            product.setImage(encodedImage);
        } else {
            product.setImage(oldProduct.getImage());
        }
        product.setCategory(product.getCategory() != null ? product.getCategory() : oldProduct.getCategory());
        productService.saveProduct(product);
        return ResponseEntity.ok(product);
    }
    @Operation(summary = "Редактировать продукт")
    @PostMapping(value = "/products/edit/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Product> editProduct(
            @PathVariable String id,
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("description") String description,
            @RequestParam("id_category") String idCategory,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        Product product = productService.findById(id);
        if (name != null && !name.isEmpty()) {
            product.setName(name);
        }
        if (price != 0) {
            product.setPrice(price);
        }
        if (description != null && !description.isEmpty()) {
            product.setDescription(description);
        }
        if (idCategory != null && !idCategory.isEmpty()) {
            product.setCategory(categoryService.findById(idCategory));
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            product.setImage(encodedImage);
        }

        productService.saveProduct(product);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Создать новый продукт")
    @PostMapping("/products/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product, @RequestBody String id_category,
                                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            product.setImage(encodedImage);
        }
        product.setCategory(categoryService.findById(id_category));
        productService.saveProduct(product);
        return ResponseEntity.ok(product);
    }
    @Operation(summary = "Создать новый продукт")
    @PostMapping(value = "/products/create", consumes = {"multipart/form-data"})
    public ResponseEntity<Product> createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("description") String description,
            @RequestParam("id_category") String idCategory,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setCategory(categoryService.findById(idCategory));

        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            product.setImage(encodedImage);
        }

        productService.saveProduct(product);
        return ResponseEntity.ok(product);
    }


    @Operation(summary = "Удалить продукт")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        Product product = productService.findById(id);
        product.setDeleted(true);
        productService.saveProduct(product);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Восстановить продукт")
    @PostMapping("/products/restore/{id}")
    public ResponseEntity<Product> restoreProduct(@PathVariable String id) {
        Product product = productService.findById(id);
        product.setDeleted(false);
        productService.saveProduct(product);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Получить список всех категорий")
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @Operation(summary = "Создать новую категорию")
    @PostMapping("/categories/create")
    public ResponseEntity<Category> addCategory(@RequestBody Category category,
                                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            category.setImage(encodedImage);
        }
        categoryService.saveCategory(category);
        return ResponseEntity.ok(category);
    }
    @PostMapping(value = "/categories/create", consumes = {"multipart/form-data"})
    public ResponseEntity<Category> addCategory(
                                                @RequestParam("name") String name,
                                                @RequestParam("description") String description,
                                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        Category category = new Category();

        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            category.setImage(encodedImage);
        }
        if (name != null && !name.isEmpty()) {
            category.setName(name);
        }
        if (description != null && !description.isEmpty()) {
            category.setDescription(description);
        }
        categoryService.saveCategory(category);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Удалить категорию")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        Category category = categoryService.findById(id);
        category.setDeleted(true);
        productService.deActiveProductByCategory(category);
        categoryService.saveCategory(category);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Восстановить категорию")
    @PostMapping("/categories/restore/{id}")
    public ResponseEntity<Category> restoreCategory(@PathVariable String id) {
        Category category = categoryService.findById(id);
        category.setDeleted(false);
        categoryService.saveCategory(category);
        return ResponseEntity.ok(category);
    }
}
