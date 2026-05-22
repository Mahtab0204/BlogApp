package com.kgm.restful_web_services.service;

import com.kgm.restful_web_services.dto.LoginDTO;
import com.kgm.restful_web_services.dto.SignupDTO;

public interface AuthService {

    String login(LoginDTO loginDTO);

    String signup(SignupDTO signupDTO);
}
