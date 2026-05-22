package com.kgm.restful_web_services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDTO {

    private Long id;
    private String name;
    private String email;
    private String body;
}
