package com.daniel.iroribank.service;

import com.daniel.iroribank.model.LoginDto;
import com.daniel.iroribank.model.ProfileDto;
import com.daniel.iroribank.model.Role;
import com.daniel.iroribank.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@DirtiesContext
class UserServiceTest {

    @Autowired
    private UserService userService;

    private User user;

    @BeforeAll
    public void init() {
        user = new User();
        user.setName("Daniel");
        user.setSsn("841026-5551");
        user.setPassword("123");
        user.setAccounts(new ArrayList<>());
    }

    @Test
    void saveUser() {
        userService.saveUser(user);

        User persistedUser = userService.saveUser(user);

        assertEquals("Daniel", persistedUser.getName());
        assertEquals("841026-5551", persistedUser.getSsn());
    }

    @Test
    void login() {

        User user2 = new User();
        user2.setRole(Role.USER);
        user2.setPassword("123");
        user2.setSsn("831026-5550");
        user2.setName("Peter");
        userService.saveUser(user2);

        LoginDto loginDto = new LoginDto("831026-5550", "123");
        ProfileDto profileDto = userService.login(loginDto);

        assertEquals("Peter", profileDto.getName());
        System.out.println("first assert");

        loginDto.setPassword("1234");
        BadCredentialsException thrown = assertThrows(
                BadCredentialsException.class,
                () -> userService.login(loginDto),
                "Expected login() to throw ResponseStatusException, but it didn't"
        );
    }
}