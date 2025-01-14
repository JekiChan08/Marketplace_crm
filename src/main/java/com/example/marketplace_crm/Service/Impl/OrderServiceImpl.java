package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Model.Order;
import com.example.marketplace_crm.Repositories.OrderRepository;
import com.example.marketplace_crm.Service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Data
public class OrderServiceImpl implements OrderService {
    @Autowired
    private final OrderRepository orderRepository;

    @Override
    public Order findById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrderById(String id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    @Override
    public List<Order> getAllOrdersIfActive() {
        return orderRepository.getAllOrdersIfActive();
    }
}
