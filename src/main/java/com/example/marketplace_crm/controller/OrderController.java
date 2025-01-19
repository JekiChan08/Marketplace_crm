package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Comment;
import com.example.marketplace_crm.Model.Order;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import com.example.marketplace_crm.Service.OrderService;
import com.example.marketplace_crm.Service.ProductService;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order Controller", description = "Управление заказами")
@Controller
@Data
@RequestMapping("/orders")
public class OrderController {
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(ProductService productService, OrderService orderService, UserService userService) {
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @Operation(
            summary = "Создание нового заказа",
            description = "Создает новый заказ на продукт"
    )
    @PostMapping("/create_order")
    public String createOrder(@ModelAttribute Order order) {
        orderService.saveOrder(order);
        return "redirect:/orders/create-order";
    }

    @Operation(
            summary = "Создание заказа для продукта",
            description = "Создает заказ для выбранного продукта по его ID"
    )
    @PostMapping("/{id}")
    public String orderById(
            @Parameter(description = "ID продукта", required = true) @PathVariable String id,
            Model model
    ) {
        Product product = productService.findById(id);
        Order order = new Order();
        order.setProduct(product);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByLogin(currentPrincipalName);

        order.setUser(user);
        orderService.saveOrder(order);

        return "redirect:/products/list";
    }
}
