package com.kgm.restful_web_services.controller;

import com.kgm.restful_web_services.ResponseMessage.MessageResponse;
import com.kgm.restful_web_services.dto.JwtAuthResponse;
import com.kgm.restful_web_services.dto.LoginDTO;
import com.kgm.restful_web_services.dto.SignupDTO;
import com.kgm.restful_web_services.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Build login REST API

    @PostMapping(value = {"/login","/signin"})
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDTO loginDTO){
        String token = authService.login(loginDTO);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    @PostMapping(value = {"/signup","/register"})
    public ResponseEntity<MessageResponse> signup(@RequestBody SignupDTO signupDTO){
        String response = authService.signup(signupDTO);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(response);
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }
}
