package com.example.marketplace_crm.Repositories;

import com.example.marketplace_crm.Model.Order;
import com.example.marketplace_crm.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByLogin(String login);
    @Query("SELECT o FROM Order o WHERE o.user = :user and o.status = 'on_the_way'")
    List<Order> ordersByUser(@Param("user") User user);

}
