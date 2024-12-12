package com.example.marketplace_crm.Service.Impl;


import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Repositories.CategoryRepository;
import com.example.marketplace_crm.Service.CategoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repository;
    @Override
    public Category findById(String Id) {
        return repository.findById(Id).orElse(null);
    }

    @Override
    public Category saveCategory(Category category) {
        return repository.save(category);
    }

    @Override
    public void deleteCategoryById(String Id) {
        repository.deleteById(Id);
    }

    @Override
    public List<Category> getAllCategory() {
        return repository.findAll();
    }

    @Override
    public Category findByName(String name) {
        return repository.findByName(name);
    }
}
