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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Data
@RequestMapping("/delivery_panel")
@Tag(name = "Delivery Panel Controller", description = "Управление доставкой заказов")
public class DeliveryPanelController {
    private final OrderService orderService;
    private final UserService userService;

    public DeliveryPanelController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @Operation(
            summary = "Список заказов",
            description = "Возвращает список всех активных заказов для доставки"
    )
    @GetMapping("/list_orders")
    public ResponseEntity<List<Order>> listOrders() {
        List<Order> orders = orderService.getAllOrdersIfActive();
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Operation(
            summary = "Обновление статуса заказа",
            description = "Обновляет статус заказа на 'в пути' и назначает доставщика"
    )
    @ApiResponse(responseCode = "200", description = "Статус заказа обновлен")
    @ApiResponse(responseCode = "404", description = "Заказ не найден")
    @PostMapping("/on_the_way/{orderId}")
    public ResponseEntity<Order> onTheWay(
            @Parameter(description = "ID заказа", required = true) @PathVariable String orderId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByLogin(currentPrincipalName);
        Order order = orderService.findById(orderId);
        if (order != null) {
            order.setDelivery(user);
            order.setStatus("on_the_way");
            orderService.saveOrder(order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
