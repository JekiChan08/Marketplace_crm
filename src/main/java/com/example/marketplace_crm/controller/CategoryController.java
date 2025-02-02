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
@Controller
@Data
@RequestMapping("/categories")
public class CategoryController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public CategoryController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

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