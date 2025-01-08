package com.example.marketplace_crm.Model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private double price;
    @Column(name = "description")
    private String description;
    @Column(name = "rating")
    private double averageRating;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)//связь один ко многим.связь продукта и рейтинга (если удалить продукт удаляется и все его оценки)
    private List<Rating> ratings;
    public Product() { }
}
