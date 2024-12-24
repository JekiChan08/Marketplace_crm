package com.example.marketplace_crm.Service;

import com.example.marketplace_crm.Model.User;

import java.util.List;

public interface UserService {
    User findById(String id);
    User saveUser(User user);
    void deleteUserById(String id);
    List<User> getAllUsers();
    User findByLogin(String login);
}
