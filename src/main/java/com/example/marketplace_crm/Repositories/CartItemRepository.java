package com.example.marketplace_crm.Repositories;

import com.example.marketplace_crm.Model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}

