package com.example.marketplace_crm.Repositories;


import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Category findByName(String name);

}
