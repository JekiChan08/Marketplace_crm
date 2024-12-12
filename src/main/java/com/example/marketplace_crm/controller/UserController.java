package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.User;
import com.example.marketplace_crm.Service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Data
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserService us;
    @GetMapping("/{id}")
    public String getById(@PathVariable String id, Model model){
        User user = us.findById(id);
        model.addAttribute("user", user);
        return "user";
    }


}
