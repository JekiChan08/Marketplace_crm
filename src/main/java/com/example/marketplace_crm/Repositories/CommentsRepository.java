package com.example.marketplace_crm.Repositories;

import com.example.marketplace_crm.Model.Comment;
import com.example.marketplace_crm.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentsRepository extends JpaRepository<Comment, String> {
    @Query("SELECT c FROM Comment c WHERE c.product = :product")
    List<Comment> findCommentsByProduct(@Param("product") Product product);
}
