package com.example.to_do_list_with_dto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDto {
    @NotBlank(message = "아이디를 입력하세요")
    @Size(min=3, max=10, message = "아이디는 3~50자여야 합니다")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min=6, max=20, message = "패스워드는 6~20자여야 합니다")
    private String password;
}