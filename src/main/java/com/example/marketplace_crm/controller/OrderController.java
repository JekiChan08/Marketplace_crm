package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Order;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Service.Impl.OrderServiceImpl;
import com.example.marketplace_crm.Service.Impl.ProductServiceImpl;
import com.example.marketplace_crm.Service.Impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Order Controller", description = "Управление заказами")
@Controller
@Data
@RequestMapping("/orders")
public class OrderController {
    private final ProductServiceImpl productService;
    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;

    public OrderController(ProductServiceImpl productService, OrderServiceImpl orderService, UserServiceImpl userService) {
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @Operation(
            summary = "Создание нового заказа",
            description = "Создает новый заказ на продукт"
    )
    @PostMapping("/create_order")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        if (order.getProduct() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        orderService.save(order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);

    }

    @Operation(
            summary = "Создание заказа для продукта",
            description = "Создает заказ для выбранного продукта по его ID"
    )
    @PostMapping("/{productId}/create")
    public ResponseEntity<Order> orderById(
            @Parameter(description = "ID продукта", required = true) @PathVariable String productId
    ) {
        Product product = productService.getById(productId);
        Order order = new Order();
        order.setProduct(product);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByLogin(currentPrincipalName);

        order.setUser(user);
        orderService.save(order);
        if (order.getProduct() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
