package com.example.marketplace_crm.Model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "category_id")
    private Category category;
    @JsonBackReference
    @Column(name = "image", columnDefinition = "LONGTEXT")
    private String image;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;


    public Product() {

    }
}
