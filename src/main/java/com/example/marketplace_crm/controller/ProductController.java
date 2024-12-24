package com.example.marketplace_crm.controller;


import com.example.marketplace_crm.Model.Comment;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Service.CategoryService;
import com.example.marketplace_crm.Service.CommentService;
import com.example.marketplace_crm.Service.ProductService;
import com.example.marketplace_crm.Service.UserService;
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

@RequiredArgsConstructor
@Controller
@Data
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private final ProductService productService;
    @Autowired
    private final CategoryService categoryService;
    @Autowired
    private final CommentService commentsService;
    @Autowired
    private final UserService userService;

    @GetMapping("/{id}")
    public String getById(@PathVariable String id, Model model, HttpSession session) {
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

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("new_product", new Product());
        model.addAttribute("all_categories", categoryService.getAllCategory());
        return "create-product";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product product,
                              @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            // Преобразуем файл в строку Base64
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            product.setImage(encodedImage);  // Сохраняем изображение как строку Base64
        }
        productService.saveProduct(product);

        return "redirect:/products/create";
    }
    @PostMapping("/create_comment")
    public String createComment(Model model, @ModelAttribute("new_comment") Comment comment, HttpSession session) {
        String product_id = (String) session.getAttribute("id_product");
        Product product = productService.findById(product_id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByLogin(currentPrincipalName);

        comment.setProduct(product);
        comment.setUser(user);

        commentsService.saveComment(comment);

        return "redirect:/products/" + product_id;
    }

    @GetMapping("/list")
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProduct();
        if (products != null) {
            model.addAttribute("isProduct", true);
            model.addAttribute("products", products);
            model.addAttribute("all_categories", categoryService.getAllCategory());
        }else {
            model.addAttribute("isProduct", false);
            model.addAttribute("product", null);
            model.addAttribute("all_categories", categoryService.getAllCategory());
        }
        return "products";
    }
    @GetMapping("/list/search")
    public String findByNameContaining(Model model, @RequestParam(required = false) String query) {
        if (query != null && !query.isEmpty()) {
            List<Product> products = productService.findByNameContaining(query);
            if (products != null && !products.isEmpty()) {
                model.addAttribute("isProduct", true);
                model.addAttribute("products", products);
            } else {
                model.addAttribute("isProduct", false);
                model.addAttribute("products", null);
            }
        }
        return "products";
    }


    @GetMapping("/{id}/image")
    public ResponseEntity<String> getProductImage(@PathVariable String id) {
        Product product = productService.findById(id);
        if (product != null && product.getImage() != null) {
            String base64Image = product.getImage();
            return new ResponseEntity<>(base64Image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
