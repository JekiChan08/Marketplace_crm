package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Model.Order;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Repositories.OrderRepository;
import com.example.marketplace_crm.Repositories.RoleRepository;
import com.example.marketplace_crm.Repositories.UserRepository;
import com.example.marketplace_crm.Service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class UserServiceImpl implements UserService {
    private final String USER_ROLE_ID = "1";
    private final String ADMIN_ROLE_ID = "2";
    private final String DELIVERY_ROLE_ID = "3";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }
    @Override
    public User saveUser(User myUser) {
        if (!userRepository.findByLogin(myUser.getLogin()).isPresent()) {
            myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
            myUser.setRoles(List.of(roleRepository.findById(USER_ROLE_ID).get()));
            return userRepository.save(myUser);
        }else {
            return null;
        }
    }

    @Override
    public List<Order> ordersByUser(User user) {
        return userRepository.ordersByUser(user);
    }

}
