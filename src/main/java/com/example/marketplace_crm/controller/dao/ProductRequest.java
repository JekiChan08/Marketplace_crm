package com.example.marketplace_crm.controller.dao;


import com.example.marketplace_crm.Model.Product;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private Product product;
    private Set<String> tags;
}

