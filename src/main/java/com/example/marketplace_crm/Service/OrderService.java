package com.example.marketplace_crm.Service;

import com.example.marketplace_crm.Model.Order;

import java.util.List;

public interface OrderService {
    Order findById(String id);
    Order saveOrder(Order order);
    void deleteOrderById(String id);
    List<Order> getAllOrders();
}
