package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Repositories.ProductRepository;
import com.example.marketplace_crm.Service.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class ProductServiceImpl implements ProductService {
    @Autowired
    private final ProductRepository productRepository;

    @Override
    public Product findById(String Id) {
        return productRepository.findById(Id).orElse(null);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProductById(String Id) {
        productRepository.deleteById(Id);
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAllActive();
    }

    @Override
    public Product findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> findByNameContaining(String name) {
        return productRepository.findByNameContaining(name);
    }
    @Override
    public List<Product> findByCategory(Category category){
        return productRepository.findByCategory(category);
    };
    @Override
    public void deActiveProductByCategory(Category category){
        List<Product> products = productRepository.findByCategory(category);
        for(Product product : products){
            product.setDeleted(true);
            productRepository.save(product);
        }
    }
    @Override
    public void activeProductByCategory(Category category){
        List<Product> products = productRepository.findByCategory(category);
        for(Product product : products){
            product.setDeleted(false);
            productRepository.save(product);
        }
    }

    @Override
    public List<Product> findAllDeActive() {
        return productRepository.findAllDeActive();
    }
}
