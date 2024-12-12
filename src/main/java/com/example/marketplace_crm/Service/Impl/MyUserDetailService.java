package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Model.Role;
import com.example.marketplace_crm.Repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository repository;

    // Constructor injection for MyUserRepository
    public MyUserDetailService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Loads the user by username.
     *
     * @param username the username to search for
     * @return the UserDetails object
     * @throws UsernameNotFoundException if the username is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username)
                .map(user -> {
                    String[] roles = user.getRoles()
                            .stream()
                            .map(role -> role.getRoleName().replace("ROLE_", ""))
                            .toArray(String[]::new);
                    return org.springframework.security.core.userdetails.User.builder()
                            .username(user.getLogin())
                            .password(user.getPassword())
                            .roles(roles)
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
