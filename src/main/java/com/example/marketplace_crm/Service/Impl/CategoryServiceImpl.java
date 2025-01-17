package com.example.marketplace_crm.Service.Impl;


import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Repositories.CategoryRepository;
import com.example.marketplace_crm.Service.CategoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category findById(String Id) {
        return categoryRepository.findById(Id).orElse(null);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(String Id) {
        categoryRepository.deleteById(Id);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAllActive();
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> findAllDeActive() {
        return categoryRepository.findAllDeActive();
    }
}
