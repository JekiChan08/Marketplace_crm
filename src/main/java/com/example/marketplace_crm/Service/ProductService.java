package com.example.marketplace_crm.Service;

import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.User;

import java.util.List;

public interface ProductService {
    Product findById(String Id);
    Product saveProduct(Product product);
    void deleteProductById(String Id);
    List<Product> getAllProduct();
    Product findByName(String name);
}
