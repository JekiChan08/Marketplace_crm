package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ProductServiceImpl extends BaseServiceImpl<Product, String> {
    @Autowired
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        super(productRepository);
        this.productRepository = productRepository;
    }

    public Product findByName(String name) {
        return productRepository.findByName(name);
    }

    public List<Product> findByNameContaining(String name) {
        return productRepository.findByNameContaining(name);
    }
    public List<Product> findByCategory(Category category){
        return productRepository.findByCategory(category);
    };
    public void deActiveProductByCategory(Category category){
        List<Product> products = productRepository.findByCategory(category);
        for(Product product : products){
            product.setDeleted(true);
            productRepository.save(product);
        }
    }
    public void activeProductByCategory(Category category){
        List<Product> products = productRepository.findByCategory(category);
        for(Product product : products){
            product.setDeleted(false);
            productRepository.save(product);
        }
    }
    public List<Product> findAllDeActive() {
        return productRepository.findAllDeActive();
    }
    public List<Product> findAllActive() {
        return productRepository.findAllActive();
    }
}
