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

@Controller
@Data
@RequestMapping("/delivery_panel")
public class DeliveryPanelController {
    private OrderService orderService;
    private UserService userService;


    public DeliveryPanelController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/list_orders")
    public String listOrders(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByLogin(currentPrincipalName);

        session.setAttribute("delivery", user);

        model.addAttribute("orders", orderService.getAllOrdersIfActive());
        return "list-orders";
    }
    @PostMapping("/on_the_way/{id}")
    public String onTheWay(Model model, HttpSession session, @PathVariable String id) {
        User delivery = (User) session.getAttribute("delivery");
        Order order = orderService.findById(id);
        order.setDelivery(delivery);
        order.setStatus("on_the_way");
        orderService.saveOrder(order);
        return "redirect:/delivery_panel/list_orders";
    }


}
