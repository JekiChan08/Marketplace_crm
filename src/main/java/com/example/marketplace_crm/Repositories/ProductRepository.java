package com.example.marketplace_crm.Repositories;

import com.example.marketplace_crm.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Product findByName(String name);

}
