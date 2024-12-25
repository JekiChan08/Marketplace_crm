package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Repositories.RoleRepository;
import com.example.marketplace_crm.Service.UserService;
import com.example.marketplace_crm.controller.Requests.JwtRequest;
import com.example.marketplace_crm.controller.dao.UserDao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Auth Controller", description = "Управление аутентификацией и регистрацией")
@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService service;
    private final RoleRepository repository;

    public AuthController(UserService service, RoleRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Operation(
            summary = "Страница регистрации",
            description = "Возвращает страницу для регистрации нового пользователя"
    )
    @GetMapping("/registration")
    public String registrationForm(Model model) {
        model.addAttribute("user", new UserDao());
        return "registration-form";
    }

    @Operation(
            summary = "Зарегистрировать нового пользователя",
            description = "Создает нового пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные")
            }
    )
    @PostMapping("/registration/save")
    public String register(
            @Parameter(description = "Данные пользователя", required = true) @Valid @ModelAttribute("user") UserDao userDao,
            Model model,
            BindingResult result
    ) {
        User existingUser = service.findByLogin(userDao.getLogin());
        if (existingUser != null) {
            result.reject("Login already exist");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", userDao);
            return "registration-form";
        }
        service.saveUser(new User(userDao.getLogin(), userDao.getPassword(), List.of(repository.findById("1").orElseThrow())));
        return "redirect:/auth/registration?success";
    }

    @Operation(
            summary = "Страница входа",
            description = "Возвращает страницу для входа в систему"
    )
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("request", new JwtRequest());
        return "login";
    }
}
//
//    @PostMapping("/login")
//    public ResponseEntity<JwtResponse> doLogin(@ModelAttribute("request") JwtRequest request) throws AuthException {
//        JwtResponse response = authService.login(request);
//        return ResponseEntity.ok(response);
//    }

