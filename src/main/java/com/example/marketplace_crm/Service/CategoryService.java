package com.example.marketplace_crm.Service;

import com.example.marketplace_crm.Model.Category;

import java.util.List;

public interface CategoryService {

    Category findById(String Id);
    Category saveCategory(Category category);
    void deleteCategoryById(String Id);
    List<Category> getAllCategory();
    Category findByName(String name);
}
