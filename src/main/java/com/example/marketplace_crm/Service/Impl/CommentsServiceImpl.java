package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Model.Comment;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Repositories.CommentsRepository;
import com.example.marketplace_crm.Service.CommentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class CommentsServiceImpl implements CommentService {
    private final CommentsRepository commentsRepository;

    @Override
    public Comment findById(String id) {
        return commentsRepository.findById(id).orElse(null);
    }

    @Override
    public Comment saveComment(Comment comments) {
        return commentsRepository.save(comments);
    }

    @Override
    public void deleteCommentById(String Id) {
        commentsRepository.deleteById(Id);
    }

    @Override
    public List<Comment> getAllComment() {
        return commentsRepository.findAll();
    }
    @Override
    public List<Comment> findCommentByProduct(Product product) {
        return commentsRepository.findCommentsByProduct(product);
    }

}
