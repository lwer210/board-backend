package com.example.board.auth.controller.response;

import com.example.board.common.jwt.JwtDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String nickname;

    private JwtDto dto;
}
