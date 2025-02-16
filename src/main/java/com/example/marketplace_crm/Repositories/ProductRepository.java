package com.example.marketplace_crm.Repositories;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Product findByName(String name);
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.isDeleted = false")
    List<Product> findByNameContaining(@Param("name") String name);


    @Query("SELECT p FROM Product p WHERE p.category = :category and p.isDeleted = false")
    List<Product> findByCategory(@Param("category") Category category);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false")
    List<Product> findAllActive();
    @Query("SELECT p FROM Product p WHERE p.isDeleted = true ")
    List<Product> findAllDeActive();

    @Query("SELECT p FROM Product p JOIN p.tags t WHERE t.name IN :tags GROUP BY p HAVING COUNT(DISTINCT t) = :tagCount")
    List<Product> findByTags(@Param("tags") Set<String> tags, @Param("tagCount") long tagCount);
}
