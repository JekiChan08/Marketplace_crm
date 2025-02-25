package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Service.Impl.CategoryServiceImpl;
import com.example.marketplace_crm.Service.Impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category Controller", description = "API для управления категориями товаров")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ProductServiceImpl productService;
    private final CategoryServiceImpl categoryService;

    @Operation(
            summary = "Получить категорию по ID",
            description = "Возвращает информацию о категории и связанных продуктах"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория найдена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(
            @Parameter(description = "ID категории", required = true, example = "123")
            @PathVariable String id
    ) {
        Category category = categoryService.getById(id);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(category);
    }

    @Operation(
            summary = "Получить список всех категорий",
            description = "Возвращает список всех активных категорий"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список категорий",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Категории не найдены")
    })
    @GetMapping("/list")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.findAllActive();
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(categories);
    }

    @Operation(
            summary = "Получить изображение категории",
            description = "Возвращает изображение категории в формате Base64"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Изображение категории",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "404", description = "Изображение не найдено")
    })
    @GetMapping("/{id}/image")
    public ResponseEntity<String> getCategoryImage(
            @Parameter(description = "ID категории", required = true, example = "123")
            @PathVariable String id
    ) {
        Category category = categoryService.getById(id);
        if (category != null && category.getImage() != null) {
            return ResponseEntity.ok(category.getImage());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
