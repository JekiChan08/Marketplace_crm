package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Model.Product;
import com.example.marketplace_crm.Model.Rating;
import com.example.marketplace_crm.Repositories.ProductRepository;
import com.example.marketplace_crm.Repositories.RatingRepository;
import com.example.marketplace_crm.Service.RatingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Data
public class RatingServiceImpl implements RatingService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ProductServiceImpl productService;
    @Override
    public void addRating(String productId, Integer ratingValue) {
        if(ratingValue<1||ratingValue>10){
            throw new IllegalArgumentException("рейтинг должен быть от 1 до 10");
        }
        Product product= productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Продукт не найден"));

        Rating rating= new Rating();
        rating.setRating(ratingValue);
        ratingRepository.save(rating);

        product.getRatings().add(rating);
        productRepository.save(product);
        productService.calculateAverageRating(productId);
    }
}
