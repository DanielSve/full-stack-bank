package com.daniel.iroribank.controller;

import com.daniel.iroribank.model.LoginDto;
import com.daniel.iroribank.model.ProfileDto;
import com.daniel.iroribank.model.User;
import com.daniel.iroribank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@CrossOrigin (origins = "*" , exposedHeaders = "**")
public class UserController {

    private UserService userService;

    @PostMapping("/add")
    public User addUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public ProfileDto login(@RequestBody LoginDto loginDto){
       return userService.login(loginDto);
    }
}
