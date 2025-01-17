package com.example.marketplace_crm.Service;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    Product findById(String id);
    Product saveProduct(Product product);
    void deleteProductById(String id);
    List<Product> getAllProduct();
    Product findByName(String name);
    List<Product> findByNameContaining(String name);
    List<Product> findByCategory(Category category);
    void deActiveProductByCategory(Category category);
    //Product addProduct(Product product, MultipartFile file) throws IOException ;
    List<Product> findAllDeActive();
    void activeProductByCategory(Category category);
}
