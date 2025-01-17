package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Service.CategoryService;
import com.example.marketplace_crm.Service.OrderService;
import com.example.marketplace_crm.Service.ProductService;
import com.example.marketplace_crm.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
@Data
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminController(UserService userService, OrderService orderService, ProductService productService, CategoryService categoryService) {
        this.userService = userService;
        this.orderService = orderService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String mainPanel(Model model) {
        return "admin/main-panel";
    }

    //Логика изменения оюъектов от Джеки чана

    @GetMapping("/products/list")
    public String editProduct(Model model) {
        model.addAttribute("products", productService.getAllProduct()
        );
        return "admin/products-list";
    }
    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable String id,
                              @Parameter(description = "Данные продукта", required = true) @ModelAttribute Product product,
                              @Parameter(description = "Изображение продукта", required = true) @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
            if (!imageFile.isEmpty()) {
                byte[] imageBytes = imageFile.getBytes();
                String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
                product.setImage(encodedImage);
            }
            Product oldProduct = productService.findById(id);
            if (product.getImage() == null) {
                product.setImage(oldProduct.getImage());
            }
            if(product.getCategory() == null) {
                product.setCategory(oldProduct.getCategory());
            }
            productService.saveProduct(product);
        return "redirect:/admin/products/list";
    }
    @GetMapping("/products/edit/{id}")
    public String editProductForm(Model model, @PathVariable String id) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("all_categories", categoryService.getAllCategory());

        return "admin/product-edit-form";
    }


    @GetMapping("/categories/list")
    public String editCategory(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory()
        );
        return "admin/categories-list";
    }
    @PostMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable String id,
                              @Parameter(description = "Данные категории", required = true) @ModelAttribute Category category,
                              @Parameter(description = "Изображение категории", required = true) @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            category.setImage(encodedImage);
        }
        Category oldCategory = categoryService.findById(id);
        if (category.getImage() == null) {
            category.setImage(oldCategory.getImage());
        }

        categoryService.saveCategory(category);
        return "redirect:/admin/categories/list";
    }
    @GetMapping("/categories/edit/{id}")
    public String editCategoryForm(Model model, @PathVariable String id) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);

        return "admin/category-edit-form";
    }

    //Логика создания объектов от Джеки чана

    @Operation(
            summary = "Страница создания продукта",
            description = "Возвращает страницу для создания нового продукта"
    )
    @GetMapping("/products/create")
    public String productCreate(Model model) {
        model.addAttribute("new_product", new Product());
        model.addAttribute("all_categories", categoryService.getAllCategory());
        return "admin/create-product";
    }

    @Operation(
            summary = "Создать новый продукт",
            description = "Создает новый продукт с изображением",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Продукт успешно создан"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные")
            }
    )
    @PostMapping("/products/create")
    public String createProduct(
            @Parameter(description = "Данные продукта", required = true) @ModelAttribute Product product,
            @Parameter(description = "Изображение продукта", required = true) @RequestParam("imageFile") MultipartFile imageFile
    ) throws IOException {
        if (!imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            product.setImage(encodedImage);
        }
        productService.saveProduct(product);
        return "redirect:/admin/products/create";
    }


    @Operation(
            summary = "Страница создания категории",
            description = "Возвращает страницу для создания новой категории"
    )
    @GetMapping("/categories/create")
    public String add(Model model) {
        model.addAttribute("new_category", new Category());
        return "admin/create-category";
    }

    @Operation(
            summary = "Создать новую категорию",
            description = "Создает новую категорию с изображением",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Категория успешно создана"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные")
            }
    )
    @PostMapping("/categories/create")
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
        return "redirect:/admin/categories/create";
    }

    //Логика удаления от Джеки чана

    @DeleteMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable String id){
        Product product = productService.findById(id);
        product.setDeleted(true);
        productService.saveProduct(product);
        return "redirect:/admin/products/list";
    }
    @DeleteMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable String id){
        Category category = categoryService.findById(id);
        category.setDeleted(true);
        productService.deActiveProductByCategory(category);
        categoryService.saveCategory(category);
        return "redirect:/admin/categories/list";
    }


    //Логика получения, востонавление неактивных объектов от Джеки чана

    @GetMapping("/products/list_de_active")
    public String deActiveProductList(Model model) {
        List<Product> products = productService.findAllDeActive();
        model.addAttribute("products", products);
        return "admin/product-list-de-active";
    }
    @GetMapping("/categories/list_de_active")
    public String deActiveCategoriesList(Model model) {
        List<Category> categories = categoryService.findAllDeActive();
        model.addAttribute("categories", categories);
        return "admin/categories-list-de-active";
    }
    @PostMapping("/products/restore/{id}")
    public String restoreProduct(@PathVariable String id){
        Product product = productService.findById(id);
        product.setDeleted(false);
        productService.saveProduct(product);
        return "redirect:/admin/products/list_de_active";
    }

    @PostMapping("/categories/restore/{id}")
    public String restoreCategory(@PathVariable String id){
        Category category = categoryService.findById(id);
        category.setDeleted(false);
        categoryService.saveCategory(category);
        return "redirect:/admin/categories/list_de_active";
    }

    @PostMapping("/categories/restore_all_product/{id}")
    public String restoreCategoryAdnAllProductIsCategory(@PathVariable String id){
        Category category = categoryService.findById(id);
        category.setDeleted(false);
        productService.activeProductByCategory(category);
        categoryService.saveCategory(category);
        return "redirect:/admin/categories/list_de_active";
    }

}
