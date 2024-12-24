package com.example.marketplace_crm.Service;

import com.example.marketplace_crm.Model.Category;
import com.example.marketplace_crm.Model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    Category findById(String id);
    Category saveCategory(Category category);
    void deleteCategoryById(String id);
    List<Category> getAllCategory();
    Category findByName(String name);
//    Category addCategory(Category category, MultipartFile file) throws IOException;
}
