package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Comment;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Repositories.TagRepository;
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
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Product Controller", description = "API для управления продуктами")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;
    private final CategoryServiceImpl categoryService;
    private final CommentsServiceImpl commentsService;
    private final UserServiceImpl userService;
    private final TagRepository tagRepository;

    @Operation(summary = "Получить продукт по ID", description = "Возвращает информацию о продукте и его комментарии")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Продукт найден",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(
            @Parameter(description = "ID продукта", required = true, example = "123")
            @PathVariable String id
    ) {
        Product product = productService.getById(id);
            Set<com.example.marketplace_crm. Model.Tag> tags = tagRepository.findTagsByProductId(product.getId());
            product.setTags(tags);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Comment> comments = commentsService.findCommentByProduct(product);
        ProductResponse response = new ProductResponse(product, comments);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Создать комментарий", description = "Создает новый комментарий для продукта")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Комментарий успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
    @PostMapping("/comments")
    public ResponseEntity<Void> createComment(
            @Parameter(description = "ID продукта", required = true, example = "123")
            @RequestParam("productId") String productId,
            @RequestParam("text") String text,
            @RequestParam("userId") String userId
    ) {
        Product product = productService.getById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userService.getById(userId);

        Comment comment = new Comment();
        comment.setText(text);
        comment.setProduct(product);
        comment.setUser(user);

        commentsService.save(comment);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getComments(@RequestParam("productId") String productId) {
        List<Comment> comments = commentsService.findCommentByProduct(productService.getById(productId));
        if (comments == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Получить все продукты", description = "Возвращает список всех активных продуктов")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список продуктов",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Продукты не найдены")
    })
    @GetMapping("/list")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAllActive();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Поиск продуктов по названию", description = "Возвращает список продуктов, найденных по названию")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Продукты найдены",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Продукты не найдены")
    })
    @GetMapping("/list/search")
    public ResponseEntity<List<Product>> findByNameContaining(
            @Parameter(description = "Поисковый запрос", required = true, example = "Пицца")
            @RequestParam String query
    ) {
        List<Product> products = productService.findByNameContaining(query);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Поиск продуктов по тегам", description = "Возвращает список продуктов, соответствующих указанным тегам")
    @ApiResponse(responseCode = "200", description = "Продукты найдены",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
    @GetMapping("/search/tag")
    public ResponseEntity<List<Product>> searchByTags(
            @Parameter(description = "Список тегов", required = true, example = "[\"веган\", \"без глютена\"]")
            @RequestParam Set<String> tags
    ) {
        return ResponseEntity.ok(productService.findByTags(tags));
    }

    @Operation(summary = "Получить изображение продукта", description = "Возвращает изображение продукта в формате Base64")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Изображение найдено",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "404", description = "Изображение не найдено")
    })
    @GetMapping("/{id}/image")
    public ResponseEntity<String> getProductImage(
            @Parameter(description = "ID продукта", required = true, example = "123")
            @PathVariable String id
    ) {
        Product product = productService.getById(id);
        if (product != null && product.getImage() != null) {
            return ResponseEntity.ok(product.getImage());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
