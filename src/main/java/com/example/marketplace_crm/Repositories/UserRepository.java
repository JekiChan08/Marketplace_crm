package com.example.marketplace_crm.Repositories;

import com.example.marketplace_crm.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "SELECT * FROM users WHERE length(login) >= :asda", nativeQuery = true)
    ArrayList<User> getAllUsersWithLoginGreaterThan8(@Param("asda") int length);
    @Query(nativeQuery = true, value = "SELECT (id) FROM users WHERE person_id IS NULL")
    List<String> getUsersWithoutPerson();
    Optional<User> findByLogin(String login);
}
