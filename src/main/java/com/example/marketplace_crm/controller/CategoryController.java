package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Service.CategoryService;
import com.example.marketplace_crm.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Tag(name = "Category Controller", description = "Управление категориями")
@RequiredArgsConstructor
@Controller
@Data
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private final ProductService productService;
    @Autowired
    private final CategoryService categoryService;

    @Operation(
            summary = "Получить категорию по ID",
            description = "Возвращает страницу с информацией о категории и ее продуктами",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Категория найдена"),
                    @ApiResponse(responseCode = "404", description = "Категория не найдена")
            }
    )
    @GetMapping("/{id}")
    public String getById(
            @Parameter(description = "ID категории", required = true) @PathVariable String id,
            Model model
    ) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        model.addAttribute("all_products", category.getProducts());
        return "category";
    }

    @Operation(
            summary = "Страница создания категории",
            description = "Возвращает страницу для создания новой категории"
    )
    @GetMapping("/create")
    public String add(Model model) {
        model.addAttribute("new_category", new Category());
        return "create-category";
    }

    @Operation(
            summary = "Создать новую категорию",
            description = "Создает новую категорию с изображением",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Категория успешно создана"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные")
            }
    )
    @PostMapping("/create")
    public String addCategory(
            @Parameter(description = "Данные категории", required = true) @ModelAttribute Category category,
            @Parameter(description = "Изображение категории", required = true) @RequestParam("imageFile") MultipartFile imageFile
    ) throws IOException {
        if (!imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            category.setImage(encodedImage);
        }
        categoryService.saveCategory(category);
        return "redirect:/categories/create";
    }

    @Operation(
            summary = "Получить все категории",
            description = "Возвращает страницу со списком всех категорий",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Категории найдены"),
                    @ApiResponse(responseCode = "404", description = "Категории не найдены")
            }
    )
    @GetMapping("/list")
    public String getCategories(Model model) {
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        return "categories";
    }

    @Operation(
            summary = "Получить изображение категории",
            description = "Возвращает изображение категории в формате Base64",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Изображение найдено", content = @Content(schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "404", description = "Изображение не найдено")
            }
    )
    @GetMapping("/{id}/image")
    public ResponseEntity<String> getCategoryImage(
            @Parameter(description = "ID категории", required = true) @PathVariable String id
    ) {
        Category category = categoryService.findById(id);
        if (category != null && category.getImage() != null) {
            String base64Image = category.getImage();
            return new ResponseEntity<>(base64Image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}