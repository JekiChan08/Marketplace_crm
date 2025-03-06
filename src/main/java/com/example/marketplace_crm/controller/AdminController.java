package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.Tag;
import com.example.marketplace_crm.Repositories.TagRepository;
import com.example.marketplace_crm.Service.Impl.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
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
@io.swagger.v3.oas.annotations.tags.Tag(name = "Admin API", description = "Управление товарами, категориями и заказами")
public class AdminController {
    private final UserServiceImpl userService;
    private final OrderServiceImpl orderService;
    private final ProductServiceImpl productService;
    private final CategoryServiceImpl categoryService;
    private final TagRepository tagRepository;

    @Operation(summary = "Получить список всех продуктов", description = "Возвращает все продукты, доступные в системе.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список продуктов успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class)))
    })
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }


    @Operation(summary = "Редактировать продукт", description = "Обновляет данные о продукте по его ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Продукт успешно обновлён"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
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
        if(name != null) {
            product.setName(name);
        }
        if (price != 0) {
            product.setPrice(price);

        }
        if (description != null) {
            product.setDescription(description);
        }
        if (idCategory != null) {
            product.setCategory(categoryService.getById(idCategory));
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            product.setImage(encodedImage);
        }

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

    @Operation(summary = "Создать новый продукт", description = "Добавляет новый продукт в систему")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Продукт успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
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
        System.out.println(product.getTags());
        productService.save(product);
        Product savedProduct = productService.getById(product.getId());
        return ResponseEntity.ok(savedProduct);
    }

    @Operation(summary = "Удалить продукт", description = "Помечает продукт как удалённый")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Продукт успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        Product product = productService.getById(id);
        product.setDeleted(true);
        productService.save(product);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Восстановить продукт", description = "Снимает статус удаления с продукта")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Продукт успешно восстановлен"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
    @PostMapping("/products/restore/{id}")
    public ResponseEntity<Product> restoreProduct(@PathVariable String id) {
        Product product = productService.getById(id);
        product.setDeleted(false);
        productService.save(product);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Получить список всех категорий", description = "Возвращает все категории товаров")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список категорий успешно получен")
    })
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAllWithProducts());
    }

    @Operation(summary = "Удалить категорию", description = "Помечает категорию как удалённую и отключает все её продукты")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Категория успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        Category category = categoryService.getById(id);
        category.setDeleted(true);
        productService.deActiveProductByCategory(category);
        categoryService.save(category);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Восстановить категорию", description = "Снимает статус удаления с категории")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория успешно восстановлена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @PostMapping("/categories/restore/{id}")
    public ResponseEntity<Category> restoreCategory(@PathVariable String id) {
        Category category = categoryService.getById(id);
        category.setDeleted(false);
        categoryService.save(category);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Создать новую категорию", description = "Добавляет новую категорию в систему")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "категорий успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    @PostMapping(value = "/categories/create", consumes = {"multipart/form-data"})
    public ResponseEntity<Category> createCategory(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        Category category = new Category();
        category.setName(name);
        category.setDescription(description);

        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            category.setImage(encodedImage);
        }

        categoryService.save(category);
        return ResponseEntity.ok(category);
    }

    @PostMapping(value = "/categories/edit/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Category> editCategory(
            @PathVariable String id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        Category category = categoryService.getById(id);
        if (name != null && !name.isEmpty()) {
            category.setName(name);
        }
        if (description != null && !description.isEmpty()) {
            category.setDescription(description);
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            category.setImage(encodedImage);
        }


        categoryService.save(category);
        return ResponseEntity.ok(category);
    }
}
