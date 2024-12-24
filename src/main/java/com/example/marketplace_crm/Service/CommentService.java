package com.example.marketplace_crm.Service;

import com.example.marketplace_crm.Model.Comment;
import com.example.marketplace_crm.Model.Product;

import java.util.List;

public interface CommentService {
    Comment findById(String id);
    Comment saveComment(Comment comments);
    void deleteCommentById(String id);
    List<Comment> getAllComment();
    List<Comment> findCommentByProduct(Product product);
}
