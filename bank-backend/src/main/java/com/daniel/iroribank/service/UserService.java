package com.daniel.iroribank.service;

import com.daniel.iroribank.config.JwtService;
import com.daniel.iroribank.model.LoginDto;
import com.daniel.iroribank.model.ProfileDto;
import com.daniel.iroribank.model.Role;
import com.daniel.iroribank.model.User;
import com.daniel.iroribank.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

public User saveUser(User user) {

    System.out.println(user.getPassword());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(Role.USER);

    var jwtToken = jwtService.generateToken(user);
    return userRepository.save(user);
}


public ProfileDto login(LoginDto loginDto) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getSsn(), loginDto.getPassword()));
    User user = userRepository.findBySsn(loginDto.getSsn()).orElse(null);
        if (user != null) {
                var jwtToken = jwtService.generateToken(user);
                ProfileDto profileDto = new ProfileDto();
                profileDto.setId(user.getId());
                profileDto.setName(user.getName());
                profileDto.setAccounts(user.getAccounts());
                profileDto.setToken(jwtToken);
                return profileDto;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

}
