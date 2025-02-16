package com.example.marketplace_crm.Model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "tags")
public class Tag {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "tag")
    private String tag;
}
