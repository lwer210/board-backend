package com.example.board.auth.service;

import com.example.board.auth.controller.request.LoginRequest;
import com.example.board.auth.controller.request.PasswordUpdateRequest;
import com.example.board.auth.controller.request.RefreshRequest;
import com.example.board.auth.controller.request.RegisterRequest;
import com.example.board.auth.controller.response.LoginResponse;
import com.example.board.auth.controller.response.UserResponse;
import com.example.board.auth.persistence.entity.RefreshTokenEntity;
import com.example.board.auth.persistence.entity.UserEntity;
import com.example.board.auth.persistence.repository.RefreshTokenEntityRepository;
import com.example.board.auth.persistence.repository.UserEntityRepository;
import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.exception.*;
import com.example.board.common.jwt.JwtDto;
import com.example.board.common.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserEntityRepository userEntityRepository;
    private final RefreshTokenEntityRepository refreshTokenEntityRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserResponse register(RegisterRequest request) {
        if(userEntityRepository.existsByNickname(request.getNickname())) {
            throw new AlreadyRegisteredNicknameException();
        }

        if(userEntityRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyRegisteredEmailException();
        }

        UserEntity entity = UserEntity.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .build();

        UserEntity saveEntity = userEntityRepository.save(entity);

        return UserResponse.builder()
                .nickname(saveEntity.getNickname())
                .email(saveEntity.getEmail())
                .id(saveEntity.getId())
                .role(saveEntity.getRole())
                .createdAt(saveEntity.getCreatedAt())
                .updatedAt(saveEntity.getUpdatedAt())
                .build();
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {

        UserEntity user = userEntityRepository.findByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);

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

    @Transactional
    public LoginResponse refresh(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        Claims claims = jwtUtil.parseToken(refreshToken);

        Date expiration = claims.getExpiration();
        RefreshTokenEntity refreshTokenEntity = refreshTokenEntityRepository.findByToken(refreshToken)
                .orElseThrow(TokenNotFoundException::new);

        if(refreshTokenEntity.getUseYn().equals("N") || expiration.before(new Date())){
            throw new ExpiredTokenException();
        }

        Authentication authentication = jwtUtil.getAuthentication(refreshToken);
        JwtDto jwtDto = jwtUtil.generatedToken(authentication);

        String newRefreshToken = jwtDto.getRefreshToken();
        Claims newClaims = jwtUtil.parseToken(newRefreshToken);

        LocalDateTime expired = newClaims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime issuedAt = newClaims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Long id = newClaims.get("id", Long.class);
        UserEntity user = userEntityRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        RefreshTokenEntity newRefreshEntity = RefreshTokenEntity.builder()
                .token(newRefreshToken)
                .expiredAt(expired)
                .issuedAt(issuedAt)
                .useYn("Y")
                .userId(user.getId())
                .build();

        refreshTokenEntityRepository.delete(refreshTokenEntity);

        refreshTokenEntityRepository.save(newRefreshEntity);

        return LoginResponse.builder()
                .nickname(user.getNickname())
                .dto(jwtDto)
                .build();
    }

    public UserResponse updatePassword(PasswordUpdateRequest request, CustomUserDetails userDetails) {
        if(userDetails == null){
            throw new UnauthorizedException();
        }

        String newPassword = request.getNewPassword();
        String confirmPassword = request.getConfirmPassword();

        if(!newPassword.equals(confirmPassword)){
            throw new NewPasswordNotMatchesException();
        }

        UserEntity user = userEntityRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
        String encodeNewPassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodeNewPassword);

        UserEntity newUser = userEntityRepository.save(user);

        return UserResponse.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .nickname(user.getNickname())
                .role(newUser.getRole())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .build();
    }
}
