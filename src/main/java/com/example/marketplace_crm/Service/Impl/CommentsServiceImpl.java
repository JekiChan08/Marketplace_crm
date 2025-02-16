package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Model.Comment;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Repositories.CommentsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsServiceImpl extends BaseServiceImpl<Comment, String> {
    private final CommentsRepository commentsRepository;

    public CommentsServiceImpl(CommentsRepository commentsRepository) {
        super(commentsRepository);
        this.commentsRepository = commentsRepository;
    }

    public List<Comment> findCommentByProduct(Product product) {
        return commentsRepository.findCommentsByProduct(product);
    }

}
