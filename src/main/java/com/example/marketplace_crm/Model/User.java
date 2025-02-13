package com.example.marketplace_crm.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "users")
@Builder
@AllArgsConstructor
public class User{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "login")
    private String login;
    @JsonBackReference
    @Column(name = "password")
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
    @Column(name = "email")
    private String email;
    public User() {

    }
    public User(String login, String password, List<Role>  role){
        this.login = login;
        this.password = password;
        this.roles = role;
    }
}
