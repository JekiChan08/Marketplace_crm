package com.example.marketplace_crm.Repositories;

import com.example.marketplace_crm.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CartRepository extends JpaRepository<Cart, String> {
}
