package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Comment;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Service.Impl.CategoryServiceImpl;
import com.example.marketplace_crm.Service.Impl.CommentsServiceImpl;
import com.example.marketplace_crm.Service.Impl.ProductServiceImpl;
import com.example.marketplace_crm.Service.Impl.UserServiceImpl;
import com.example.marketplace_crm.controller.Requests.CommentRequest;
import com.example.marketplace_crm.controller.Responses.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product Controller", description = "Управление продуктами")
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductServiceImpl productService;
    private final CategoryServiceImpl categoryService;
    private final CommentsServiceImpl commentsService;
    private final UserServiceImpl userService;

    @Autowired
    public ProductController(ProductServiceImpl productService, CategoryServiceImpl categoryService, CommentsServiceImpl commentsService, UserServiceImpl userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.commentsService = commentsService;
        this.userService = userService;
    }

    @Operation(
            summary = "Получить продукт по ID",
            description = "Возвращает информацию о продукте и его комментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Продукт найден"),
                    @ApiResponse(responseCode = "404", description = "Продукт не найден")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(
            @Parameter(description = "ID продукта", required = true) @PathVariable String id
    ) {
        Product product = productService.getById(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Comment> comments = commentsService.findCommentByProduct(product);
        ProductResponse response = new ProductResponse(product, comments);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Создать комментарий",
            description = "Создает новый комментарий для продукта",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий успешно создан"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные")
            }
    )
    @PostMapping("/{id}/comments")
    public ResponseEntity<Void> createComment(
            @Parameter(description = "ID продукта", required = true) @PathVariable String id,
            @RequestBody CommentRequest commentRequest
    ) {
        Product product = productService.getById(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByLogin(currentPrincipalName);

        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setProduct(product);
        comment.setUser(user);

        commentsService.save(comment);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Получить все продукты",
            description = "Возвращает список всех продуктов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Продукты найдены"),
                    @ApiResponse(responseCode = "404", description = "Продукты не найдены")
            }
    )
    @GetMapping("/list")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAllActive();
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @Operation(
            summary = "Поиск продуктов по названию",
            description = "Возвращает список продуктов, найденных по названию",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Продукты найдены"),
                    @ApiResponse(responseCode = "404", description = "Продукты не найдены")
            }
    )
    @GetMapping("/list/search")
    public ResponseEntity<List<Product>> findByNameContaining(
            @Parameter(description = "Поисковый запрос", required = true) @RequestParam String query
    ) {
        List<Product> products = productService.findByNameContaining(query);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @Operation(
            summary = "Получить изображение продукта",
            description = "Возвращает изображение продукта в формате Base64",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Изображение найдено", content = @Content(schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "404", description = "Изображение не найдено")
            }
    )
    @GetMapping("/{id}/image")
    public ResponseEntity<String> getProductImage(
            @Parameter(description = "ID продукта", required = true) @PathVariable String id
    ) {
        Product product = productService.getById(id);
        if (product != null && product.getImage() != null) {
            return new ResponseEntity<>(product.getImage(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}