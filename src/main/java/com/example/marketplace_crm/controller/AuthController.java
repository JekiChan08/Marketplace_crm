package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Repositories.RoleRepository;
import com.example.marketplace_crm.controller.Requests.JwtRequest;
import com.example.marketplace_crm.controller.Responses.JwtResponse;
import com.example.marketplace_crm.controller.dao.UserDao;
import com.example.marketplace_crm.Service.Impl.AuthServiceImpl;
import com.example.marketplace_crm.Service.UserService;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService service;
    private final RoleRepository repository;

    public AuthController(UserService service, RoleRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping("/registration")
    public String registrationForm(Model model){
        model.addAttribute("user", new UserDao());
        return "registration-form";
    }
    @PostMapping("/registration/save")
    public String register(@Valid @ModelAttribute("user") UserDao userDao, Model model, BindingResult result){
        User existingUser = service.findByLogin(userDao.getLogin());
        if (existingUser != null){
            result.reject("Login already exist");
        }
        if (result.hasErrors()){
            model.addAttribute("user", userDao);
            return "registration-form";
        }
        service.saveUser(new User(userDao.getLogin(), userDao.getPassword(), List.of(repository.findById("1").orElseThrow())));
        return "redirect:/auth/registration?success";
    }
    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("request", new JwtRequest());
        return "login";
    }
//
//    @PostMapping("/login")
//    public ResponseEntity<JwtResponse> doLogin(@ModelAttribute("request") JwtRequest request) throws AuthException {
//        JwtResponse response = authService.login(request);
//        return ResponseEntity.ok(response);
//    }
}
