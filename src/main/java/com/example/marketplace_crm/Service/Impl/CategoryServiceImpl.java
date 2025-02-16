package com.example.marketplace_crm.Service.Impl;


import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category, String> {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public List<Category> findAllDeActive() {
        return categoryRepository.findAllDeActive();
    }
    public List<Category> findAllActive() {
        return categoryRepository.findAllActive();
    }
}
