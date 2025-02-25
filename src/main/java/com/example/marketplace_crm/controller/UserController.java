package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Order;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Service.Impl.OrderServiceImpl;
import com.example.marketplace_crm.Service.Impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Controller", description = "Управление пользователями")
@RestController
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
            description = "Возвращает информацию о пользователе по его ID. Если пользователь не найден, возвращается ошибка 404.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь найден", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(
            @Parameter(description = "ID пользователя", required = true) @PathVariable String id
    ) {
        User user = us.getById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(
            summary = "Получить заказы текущего пользователя",
            description = "Возвращает список заказов для авторизованного пользователя. В случае отсутствия заказов, возвращается ошибка 404.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список заказов пользователя", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Order.class))),
                    @ApiResponse(responseCode = "404", description = "Заказы не найдены")
            }
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

    @Operation(
            summary = "Получить заказы пользователя по его ID",
            description = "Возвращает список заказов пользователя по его ID. Если заказы не найдены, возвращается ошибка 404.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список заказов пользователя", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Order.class))),
                    @ApiResponse(responseCode = "404", description = "Заказы не найдены")
            }
    )
    @GetMapping("/my_orders/{userId}")
    public ResponseEntity<List<Order>> myOrders(@PathVariable String userId) {
        User user = us.getById(userId);

        List<Order> orders = us.ordersByUser(user);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Operation(
            summary = "Деактивировать заказ",
            description = "Изменяет статус заказа на 'de_active'. Возвращает деактивированный заказ.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заказ деактивирован", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Order.class))),
                    @ApiResponse(responseCode = "404", description = "Заказ не найден")
            }
    )
    @PostMapping("/de_active_order/{orderId}")
    public ResponseEntity<Order> deActiveOrder(@PathVariable String orderId) {
        Order order = os.getById(orderId);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        order.setStatus("de_active");
        os.save(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
