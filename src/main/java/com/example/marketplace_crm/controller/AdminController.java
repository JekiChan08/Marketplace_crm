package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.Tag;
import com.example.marketplace_crm.Repositories.TagRepository;
import com.example.marketplace_crm.Service.Impl.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userService;
    private final OrderServiceImpl orderService;
    private final ProductServiceImpl productService;
    private final CategoryServiceImpl categoryService;
    private final TagRepository tagRepository;

    @Operation(summary = "Получить список всех продуктов")
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }

    @Operation(summary = "Редактировать продукт")
    @PostMapping("/products/edit/{id}")
    public ResponseEntity<Product> editProduct(@PathVariable String id,
                                               @RequestBody Product product,
                                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        Product oldProduct = productService.getById(id);
        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            product.setImage(encodedImage);
        } else {
            product.setImage(oldProduct.getImage());
        }
        product.setCategory(product.getCategory() != null ? product.getCategory() : oldProduct.getCategory());
        productService.save(product);
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
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "tags", required = false) List<String> tags) throws IOException {

        Product product = productService.getById(id);
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
            product.setCategory(categoryService.getById(idCategory));
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            product.setImage(encodedImage);
        }
        // Добавление тегов
        if (tags != null && !tags.isEmpty()) {
            Set<Tag> tagSet = tags.stream()
                    .map(tagName -> tagRepository.findByName(tagName).orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        return tagRepository.save(newTag);
                    }))
                    .collect(Collectors.toSet());

            product.setTags(tagSet);
        }


        productService.save(product);
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
        product.setCategory(categoryService.getById(id_category));
        productService.save(product);
        return ResponseEntity.ok(product);
    }
    @Operation(summary = "Создать новый продукт")
    @PostMapping(value = "/products/create", consumes = {"multipart/form-data"})
    public ResponseEntity<Product> createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("description") String description,
            @RequestParam("id_category") String idCategory,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "tags", required = false) List<String> tags) throws IOException {

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setCategory(categoryService.getById(idCategory));

        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            product.setImage(encodedImage);
        }

        // Добавление тегов
        if (tags != null && !tags.isEmpty()) {
            Set<Tag> tagSet = tags.stream()
                    .map(tagName -> tagRepository.findByName(tagName).orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        return tagRepository.save(newTag);
                    }))
                    .collect(Collectors.toSet());

            product.setTags(tagSet);
        }

        productService.save(product);
        return ResponseEntity.ok(product);
    }



    @Operation(summary = "Удалить продукт")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        Product product = productService.getById(id);
        product.setDeleted(true);
        productService.save(product);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Восстановить продукт")
    @PostMapping("/products/restore/{id}")
    public ResponseEntity<Product> restoreProduct(@PathVariable String id) {
        Product product = productService.getById(id);
        product.setDeleted(false);
        productService.save(product);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Получить список всех категорий")
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAll());
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
        categoryService.save(category);
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
        categoryService.save(category);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Удалить категорию")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        Category category = categoryService.getById(id);
        category.setDeleted(true);
        productService.deActiveProductByCategory(category);
        categoryService.save(category);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Восстановить категорию")
    @PostMapping("/categories/restore/{id}")
    public ResponseEntity<Category> restoreCategory(@PathVariable String id) {
        Category category = categoryService.getById(id);
        category.setDeleted(false);
        categoryService.save(category);
        return ResponseEntity.ok(category);
    }
}
