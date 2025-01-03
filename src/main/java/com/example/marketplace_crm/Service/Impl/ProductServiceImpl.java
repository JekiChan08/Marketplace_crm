package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Repositories.ProductRepository;
import com.example.marketplace_crm.Service.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

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
        return productRepository.findAll();
    }

    @Override
    public Product findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> findByNameContaining(String name) {
        return productRepository.findByNameContaining(name);
    }
}
