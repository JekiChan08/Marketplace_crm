package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Repositories.ProductRepository;
import com.example.marketplace_crm.Service.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository repository;

    @Override
    public Product findById(String Id) {
        return repository.findById(Id).orElse(null);
    }

    @Override
    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    @Override
    public void deleteProductById(String Id) {
        repository.deleteById(Id);
    }

    @Override
    public List<Product> getAllProduct() {
        return repository.findAll();
    }

    @Override
    public Product findByName(String name) {
        return repository.findByName(name);
    }
}
