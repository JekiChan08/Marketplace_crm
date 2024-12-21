package com.example.marketplace_crm.Repositories;

import com.example.marketplace_crm.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Product findByName(String name);
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    List<Product> findByNameContaining(@Param("name") String name);
    List<Product> findByNameContainingIgnoreCase(String name);

}
