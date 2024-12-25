package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller", description = "Управление пользователями")
@RequiredArgsConstructor
@Controller
@Data
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserService us;

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает страницу с информацией о пользователе",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь найден"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    @GetMapping("/{id}")
    public String getById(
            @Parameter(description = "ID пользователя", required = true) @PathVariable String id,
            Model model
    ) {
        User user = us.findById(id);
        model.addAttribute("user", user);
        return "user";
    }
}