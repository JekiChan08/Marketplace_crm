package com.example.marketplace_crm.Repositories;

import com.example.marketplace_crm.Model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    Optional<Tag> findByName(String name);

    @Query("SELECT t FROM Tag t JOIN t.products p WHERE p.id = :productId")
    Set<Tag> findTagsByProductId(@Param("productId") String productId);

}
