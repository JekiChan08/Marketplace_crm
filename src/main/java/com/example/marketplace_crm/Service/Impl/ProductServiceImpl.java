package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.Tag;
import com.example.marketplace_crm.Repositories.ProductRepository;
import com.example.marketplace_crm.Repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service

public class ProductServiceImpl extends BaseServiceImpl<Product, String> {
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final TagRepository tagRepository;

    public ProductServiceImpl(ProductRepository productRepository, TagRepository tagRepository) {
        super(productRepository);
        this.productRepository = productRepository;
        this.tagRepository = tagRepository;
    }

    public Product findByName(String name) {
        return productRepository.findByName(name);
    }

    public List<Product> findByNameContaining(String name) {
        List<Product> products = productRepository.findByNameContaining(name);
        for (Product product : products) {
            Set<Tag> tags = tagRepository.findTagsByProductId(product.getId());
            product.setTags(tags);
        }
        return products;
    }
    public List<Product> findByCategory(Category category){
        List<Product> products = productRepository.findByCategory(category);
        for (Product product : products) {
            Set<Tag> tags = tagRepository.findTagsByProductId(product.getId());
            product.setTags(tags);
        }
        return products;
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
        for (Product product : products) {
            Set<Tag> tags = tagRepository.findTagsByProductId(product.getId());
            product.setTags(tags);
        }
        for(Product product : products){
            product.setDeleted(false);
            productRepository.save(product);
        }
    }
    public List<Product> findAllDeActive() {
        List<Product> products = productRepository.findAllDeActive();
        for (Product product : products) {
            Set<Tag> tags = tagRepository.findTagsByProductId(product.getId());
            product.setTags(tags);
        }
        return products;
    }
    public List<Product> findAllActive() {
        List<Product> products = productRepository.findAllActive();
        for (Product product : products) {
            Set<Tag> tags = tagRepository.findTagsByProductId(product.getId());
            product.setTags(tags);
        }
        return products;
    }

    public Product createProduct(Product product, Set<String> tagNames) {
        Set<Tag> tags = tagNames.stream()
                .map(name -> tagRepository.findByName(name).orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setName(name);
                    return tagRepository.save(newTag);
                }))
                .collect(Collectors.toSet());

        product.setTags(tags);
        return productRepository.save(product);
    }
    public List<Product> findByTags(Set<String> tagNames) {
        List<Product> products = productRepository.findByTags(tagNames, tagNames.size());
        for (Product product : products) {
            Set<Tag> tags = tagRepository.findTagsByProductId(product.getId());
            product.setTags(tags);
        }
        return products;
    }

    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            Set<Tag> tags = tagRepository.findTagsByProductId(product.getId());
            product.setTags(tags);
        }
        return products;
    }
}
