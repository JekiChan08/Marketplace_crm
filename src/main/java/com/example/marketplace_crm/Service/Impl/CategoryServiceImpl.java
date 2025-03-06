package com.example.marketplace_crm.Service.Impl;


import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.Tag;
import com.example.marketplace_crm.Repositories.CategoryRepository;
import com.example.marketplace_crm.Repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category, String> {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, TagRepository tagRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public Category findByName(String name) {
        Category category = categoryRepository.findByName(name);
            for (Product product : category.getProducts()) {
                Set<Tag> tags = tagRepository.findTagsByProductId(product.getId());
                product.setTags(tags);
            }

        return category;
    }

    public List<Category> findAllDeActive() {
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            for (Product product : category.getProducts()) {
                Set<Tag> tags = tagRepository.findTagsByProductId(product.getId());
                product.setTags(tags);
            }
        }
        return categories;
    }
    public List<Category> findAllActive() {
        List<Category> categories = categoryRepository.findAllActive();
        for (Category category : categories) {
            for (Product product : category.getProducts()) {
                Set<Tag> tags = tagRepository.findTagsByProductId(product.getId());
                product.setTags(tags);
            }
        }
        return categories;
    }
    public List<Category> findAllWithProducts() {
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            for (Product product : category.getProducts()) {
                Set<Tag> tags = tagRepository.findTagsByProductId(product.getId());
                product.setTags(tags);
            }
        }

        return categories;
    }

}
