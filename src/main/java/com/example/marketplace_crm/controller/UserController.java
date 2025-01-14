package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Order;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Service.OrderService;
import com.example.marketplace_crm.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller", description = "Управление пользователями")
@Controller
@Data
@RequestMapping("/users")
public class UserController {
    private final UserService us;
    private final OrderService os;

    public UserController(UserService us, OrderService os) {
        this.us = us;
        this.os = os;
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает страницу с информацией о пользователе",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь найден"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    @GetMapping("/{id}")
    public String getById(
            @Parameter(description = "ID пользователя", required = true) @PathVariable String id,
            Model model
    ) {
        User user = us.findById(id);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/my_orders")
    public String myOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = us.findByLogin(currentPrincipalName);

        model.addAttribute("orders", us.ordersByUser(user));
        return "order-list-user";
    }
    @PostMapping("/de_active_order/{id}")
    public String deActiveOrder(Model model, @PathVariable String id) {
        Order order = os.findById(id);
        order.setStatus("de_active");
        os.saveOrder(order);
        return "redirect:/users/my_orders";
    }

}