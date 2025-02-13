package com.example.marketplace_crm.controller.Responses;


import com.example.marketplace_crm.Model.Comment;
import com.example.marketplace_crm.Model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductResponse {
    private final Product product;
    private final List<Comment> comments;
}
