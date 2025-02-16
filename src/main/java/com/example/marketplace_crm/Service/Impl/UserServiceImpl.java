package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Model.Order;
import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Repositories.OrderRepository;
import com.example.marketplace_crm.Repositories.RoleRepository;
import com.example.marketplace_crm.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class UserServiceImpl extends BaseServiceImpl<User, String> {
    private final String USER_ROLE_ID = "1";
    private final String ADMIN_ROLE_ID = "2";
    private final String DELIVERY_ROLE_ID = "3";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.orderRepository = orderRepository;
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }
    @Override
    public User save(User myUser) {
        if (!userRepository.findByLogin(myUser.getLogin()).isPresent()) {
            myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
            myUser.setRoles(List.of(roleRepository.findById(USER_ROLE_ID).get()));
            return userRepository.save(myUser);
        }else {
            return null;
        }
    }
    public List<Order> ordersByUser(User user) {
        return userRepository.ordersByUser(user);
    }

}
