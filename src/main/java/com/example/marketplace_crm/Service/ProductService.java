package com.example.marketplace_crm.Service;

import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    Product findById(String Id);
    Product saveProduct(Product product);
    void deleteProductById(String Id);
    List<Product> getAllProduct();
    Product findByName(String name);
    List<Product> findByNameContaining(String name);
    //Product addProduct(Product product, MultipartFile file) throws IOException ;
}
