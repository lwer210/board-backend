package com.example.board.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtDto {

    private String type;

    private String accessToken;

    private String refreshToken;

    private Long accessTokenExpired;
}
