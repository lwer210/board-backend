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
import org.springframework.transaction.annotation.Transactional;

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

    public UserResponse register(RegisterRequest request) {
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

    @Transactional
    public LoginResponse refresh(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        Claims claims = jwtUtil.parseToken(refreshToken);

        Date expiration = claims.getExpiration();
        RefreshTokenEntity refreshTokenEntity = refreshTokenEntityRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("토큰을 찾을 수 없습니다."));// TODO 커스텀 예외 교체 필요

        if(refreshTokenEntity.getUseYn().equals("N") || expiration.before(new Date())){
            throw new RuntimeException("만료된 토큰"); // TODO 커스텀 예외 교체 필요
        }

        Authentication authentication = jwtUtil.getAuthentication(refreshToken);
        JwtDto jwtDto = jwtUtil.generatedToken(authentication);

        String newRefreshToken = jwtDto.getRefreshToken();
        Claims newClaims = jwtUtil.parseToken(newRefreshToken);

        LocalDateTime expired = newClaims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime issuedAt = newClaims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Long id = newClaims.get("id", Long.class);
        UserEntity user = userEntityRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));// TODO 커스텀 예외 교체 필요

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
            throw new RuntimeException("인증되지 않은 사용자 입니다."); // TODO 커스텀 예외 교체 필요
        }

        String newPassword = request.getNewPassword();
        String confirmPassword = request.getConfirmPassword();

        if(!newPassword.equals(confirmPassword)){
            throw new RuntimeException("새 비밀번호가 일치하지 않습니다."); // TODO 커스텀 예외 교체 필요
        }

        UserEntity user = userEntityRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
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
