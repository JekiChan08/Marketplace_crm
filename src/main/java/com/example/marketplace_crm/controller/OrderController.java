package com.example.marketplace_crm.controller;


import com.example.marketplace_crm.Model.Comment;
import com.example.marketplace_crm.Model.Order;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import com.example.marketplace_crm.Service.OrderService;
import com.example.marketplace_crm.Service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order Controller", description = "Управление заказами")
@Controller
@Data
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private final ProductService productService;
    @Autowired
    private final OrderService orderService;
    @Autowired
    private final UserService userService;


    @PostMapping("/create_order")
    public String createOrder(@ModelAttribute Order order) {
        orderService.saveOrder(order);
        return "redirect:/orders/create-order";
    }

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
