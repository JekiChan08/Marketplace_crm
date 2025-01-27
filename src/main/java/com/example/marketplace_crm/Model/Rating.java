package com.example.marketplace_crm.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "ratings",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id","product_id"})})//уникальность пары user_id и product_id
public class Rating {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "rating")
    private Integer rating;
}