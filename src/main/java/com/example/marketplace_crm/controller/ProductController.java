package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Comment;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Service.CategoryService;
import com.example.marketplace_crm.Service.CommentService;
import com.example.marketplace_crm.Service.ProductService;
import com.example.marketplace_crm.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Tag(name = "Product Controller", description = "Управление продуктами")
@Controller
@Data
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final CommentService commentsService;
    private final UserService userService;


    public ProductController(ProductService productService, CategoryService categoryService, CommentService commentsService, UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.commentsService = commentsService;
        this.userService = userService;
    }

    @Operation(
            summary = "Получить продукт по ID",
            description = "Возвращает страницу с информацией о продукте и его комментариями",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Продукт найден"),
                    @ApiResponse(responseCode = "404", description = "Продукт не найден")
            }
    )
    @GetMapping("/{id}")
    public String getById(
            @Parameter(description = "ID продукта", required = true) @PathVariable String id,
            Model model,
            HttpSession session
    ) {
        Product product = productService.findById(id);
        List<Comment> comments = commentsService.findCommentByProduct(product);

        session.setAttribute("id_product", id);

        Comment comment = new Comment();

        model.addAttribute("new_comment", comment);
        model.addAttribute("product", product);

        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("isComment", true);
            model.addAttribute("comments", comments);
        } else {
            model.addAttribute("isComment", false);
            model.addAttribute("comments", null);
        }

        return "product";
    }



    @Operation(
            summary = "Создать комментарий",
            description = "Создает новый комментарий для продукта",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий успешно создан"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные")
            }
    )
    @PostMapping("/create_comment")
    public String createComment(
            @Parameter(description = "Данные комментария", required = true) @ModelAttribute("new_comment") Comment comment,
            HttpSession session
    ) {
        String product_id = (String) session.getAttribute("id_product");
        Product product = productService.findById(product_id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByLogin(currentPrincipalName);

        comment.setProduct(product);
        comment.setUser(user);
        if(comment.getText() != null && !comment.getText().isEmpty()) {
            commentsService.saveComment(comment);
        }

        return "redirect:/products/" + product_id;
    }

    @Operation(
            summary = "Получить все продукты",
            description = "Возвращает страницу со списком всех продуктов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Продукты найдены"),
                    @ApiResponse(responseCode = "404", description = "Продукты не найдены")
            }
    )
    @GetMapping("/list")
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProduct();
        if (!products.isEmpty()) {
            model.addAttribute("isProduct", true);
            model.addAttribute("products", products);
            model.addAttribute("all_categories", categoryService.getAllCategory());
        } else {
            model.addAttribute("isProduct", false);
            model.addAttribute("product", null);
            model.addAttribute("all_categories", categoryService.getAllCategory());
        }
        return "products";
    }

    @Operation(
            summary = "Поиск продуктов по названию",
            description = "Возвращает страницу с продуктами, найденными по названию",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Продукты найдены"),
                    @ApiResponse(responseCode = "404", description = "Продукты не найдены")
            }
    )
    @GetMapping("/list/search")
    public String findByNameContaining(
            @Parameter(description = "Поисковый запрос", required = true) @RequestParam(required = false) String query,
            Model model
    ) {
        if (query != null && !query.isEmpty()) {
            List<Product> products = productService.findByNameContaining(query);
            if (!products.isEmpty()) {
                model.addAttribute("isProduct", true);
                model.addAttribute("products", products);
            } else {
                model.addAttribute("isProduct", false);
                model.addAttribute("products", null);
            }
        }
        return "products";
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
        Product product = productService.findById(id);
        if (product != null && product.getImage() != null) {
            String base64Image = product.getImage();
            return new ResponseEntity<>(base64Image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}