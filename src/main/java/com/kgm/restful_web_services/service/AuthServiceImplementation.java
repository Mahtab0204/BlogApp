package com.kgm.restful_web_services.service;

import com.kgm.restful_web_services.dto.SignupDTO;
import com.kgm.restful_web_services.model.Role;
import com.kgm.restful_web_services.model.User;
import com.kgm.restful_web_services.repository.RoleRepository;
import com.kgm.restful_web_services.repository.UserRepository;
import com.kgm.restful_web_services.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kgm.restful_web_services.dto.LoginDTO;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImplementation implements AuthService{


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(LoginDTO loginDTO) {
       Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

       return token;
    }

    @Override
    public String signup(SignupDTO signupDTO) {

        if(userRepository.existsByEmail(signupDTO.getEmail())){
            throw new BadCredentialsException("Email is already exists!.");
        }

        User user = new User();
        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        user.setBirthDate(signupDTO.getBirthDate());

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
        return "User Registered Successfully";
    }
}
