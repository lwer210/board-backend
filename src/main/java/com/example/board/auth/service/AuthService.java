package com.example.board.auth.service;

import com.example.board.auth.controller.request.LoginRequest;
import com.example.board.auth.controller.request.RegisterRequest;
import com.example.board.auth.controller.response.LoginResponse;
import com.example.board.auth.controller.response.RegisterResponse;
import com.example.board.auth.persistence.entity.RefreshTokenEntity;
import com.example.board.auth.persistence.entity.UserEntity;
import com.example.board.auth.persistence.repository.RefreshTokenEntityRepository;
import com.example.board.auth.persistence.repository.UserEntityRepository;
import com.example.board.common.jwt.JwtDto;
import com.example.board.common.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserEntityRepository userEntityRepository;
    private final RefreshTokenEntityRepository refreshTokenEntityRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public RegisterResponse register(RegisterRequest request) {
        if(userEntityRepository.existsByNickname(request.getNickname())) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

        if(userEntityRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        UserEntity entity = UserEntity.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER") // TODO 권한 수정 필요
                .build();

        UserEntity saveEntity = userEntityRepository.save(entity);

        return RegisterResponse.builder()
                .nickname(saveEntity.getNickname())
                .email(saveEntity.getEmail())
                .id(saveEntity.getId())
                .role(saveEntity.getRole())
                .createdAt(saveEntity.getCreatedAt())
                .updatedAt(saveEntity.getUpdatedAt())
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        JwtDto jwtDto = jwtUtil.generatedToken(authenticate);

        String refreshToken = jwtDto.getRefreshToken();
        Claims claims = jwtUtil.parseToken(refreshToken);

        Date expiration = claims.getExpiration();
        Date issuedAt = claims.getIssuedAt();

        LocalDateTime expired = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime issued = issuedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Long id = claims.get("id", Long.class);

        UserEntity user = userEntityRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));// TODO 커스텀 예외 변경 필요

        RefreshTokenEntity refreshEntity = RefreshTokenEntity.builder()
                .expiredAt(expired)
                .issuedAt(issued)
                .token(refreshToken)
                .useYn("Y")
                .userId(user.getId())
                .build();

        if(refreshTokenEntityRepository.existsByUserId(id)){
            refreshTokenEntityRepository.deleteByUserId(id);
        }

        refreshTokenEntityRepository.save(refreshEntity);

        return LoginResponse.builder()
                .nickname(user.getNickname())
                .dto(jwtDto)
                .build();
    }
}
