package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Order;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Service.OrderService;
import com.example.marketplace_crm.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
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
    public String listOrders(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByLogin(currentPrincipalName);

        session.setAttribute("delivery", user);

        model.addAttribute("orders", orderService.getAllOrdersIfActive());
        return "list-orders";
    }

    @Operation(
            summary = "Обновление статуса заказа",
            description = "Обновляет статус заказа на 'в пути' и назначает доставщика"
    )
    @ApiResponse(responseCode = "200", description = "Статус заказа обновлен")
    @ApiResponse(responseCode = "404", description = "Заказ не найден")
    @PostMapping("/on_the_way/{id}")
    public String onTheWay(
            @Parameter(description = "ID заказа", required = true) @PathVariable String id,
            Model model,
            HttpSession session
    ) {
        User delivery = (User) session.getAttribute("delivery");
        Order order = orderService.findById(id);
        if (order != null) {
            order.setDelivery(delivery);
            order.setStatus("on_the_way");
            orderService.saveOrder(order);
        }
        return "redirect:/delivery_panel/list_orders";
    }
}
