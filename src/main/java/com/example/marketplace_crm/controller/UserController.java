package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Order;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Service.Impl.OrderServiceImpl;
import com.example.marketplace_crm.Service.Impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Controller", description = "Управление пользователями")
@RestController
@Data
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl us;
    private final OrderServiceImpl os;

    public UserController(UserServiceImpl us, OrderServiceImpl os) {
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
    public ResponseEntity<User> getById(
            @Parameter(description = "ID пользователя", required = true) @PathVariable String id,
            Model model
    ) {
        User user = us.getById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(
            summary = "Получить заказы текущего пользователя",
            description = "Возвращает список заказов для авторизованного пользователя"
    )
    @GetMapping("/my_orders")
    public ResponseEntity<List<Order>> myOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = us.findByLogin(currentPrincipalName);

        List<Order> orders = us.ordersByUser(user);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @GetMapping("/my_orders/{userId}")
    public ResponseEntity<List<Order>> myOrders(@PathVariable String userId) {
        User user = us.findByLogin(userId);

        List<Order> orders = us.ordersByUser(user);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Operation(
            summary = "Деактивировать заказ",
            description = "Изменяет статус заказа на 'de_active'"
    )
    @PostMapping("/de_active_order/{orderId}")
    public ResponseEntity<Order> deActiveOrder(Model model, @PathVariable String orderId) {
        Order order = os.getById(orderId);
        order.setStatus("de_active");
        os.save(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
