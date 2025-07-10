package com.example.to_do_list_with_dto.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotBlank(message = "아이디를 입력하세요")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;
}