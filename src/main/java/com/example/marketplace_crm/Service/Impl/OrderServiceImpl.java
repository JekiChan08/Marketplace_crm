package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Model.Order;
import com.example.marketplace_crm.Repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class OrderServiceImpl extends BaseServiceImpl<Order, String> {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrdersIfActive() {
        return orderRepository.getAllOrdersIfActive();
    }
}
