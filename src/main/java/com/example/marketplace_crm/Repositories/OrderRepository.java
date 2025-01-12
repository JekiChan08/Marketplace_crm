package com.example.marketplace_crm.Repositories;

import com.example.marketplace_crm.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("SELECT o FROM Order o WHERE o.status is null")
    List<Order> getAllOrdersIfActive();
}
