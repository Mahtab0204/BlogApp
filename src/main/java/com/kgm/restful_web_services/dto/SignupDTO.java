package com.kgm.restful_web_services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupDTO {

    private String name;
    private String email;
    private String password;
    private LocalDate birthDate;
}
