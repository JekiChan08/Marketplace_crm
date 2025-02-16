package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Service.Impl.CategoryServiceImpl;
import com.example.marketplace_crm.Service.Impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Category Controller", description = "Управление категориями")
@RestController
@Data
@RequestMapping("/categories")
public class CategoryController {
    private final ProductServiceImpl productService;
    private final CategoryServiceImpl categoryService;

    public CategoryController(ProductServiceImpl productService, CategoryServiceImpl categoryService) {
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
    public ResponseEntity<Category> getById(
            @Parameter(description = "ID категории", required = true) @PathVariable String id
    ) {
        Category category = categoryService.getById(id);
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
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
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.findAllActive();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);

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
        Category category = categoryService.getById(id);
        if (category != null && category.getImage() != null) {
            String base64Image = category.getImage();
            return new ResponseEntity<>(base64Image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}