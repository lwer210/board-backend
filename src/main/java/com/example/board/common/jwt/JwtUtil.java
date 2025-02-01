package com.example.board.common.jwt;

import com.example.board.auth.persistence.entity.UserEntity;
import com.example.board.auth.persistence.repository.UserEntityRepository;
import com.example.board.common.custom.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final Key key;
    private final UserEntityRepository userEntityRepository;

    public JwtUtil(@Value("${jwt.secret}") String value, UserEntityRepository userEntityRepository){
        byte[] bytes = Decoders.BASE64.decode(value);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.userEntityRepository = userEntityRepository;
    }

    public JwtDto generatedToken(Authentication authentication){
        String authenticates = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        long nowLong = now.getTime();

        String email = authentication.getName();
        UserEntity user = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없음"));// TODO 커스텀 예외로 변경 필요

        Date accessExpired = new Date(nowLong + 24 * 60 * 60 * 1000);
        String accessToken = Jwts.builder()
                .setSubject("access")
                .setIssuedAt(now)
                .setExpiration(accessExpired)
                .claim("auth", authenticates)
                .claim("id", user.getId())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Date refreshExpired = new Date(nowLong + 7 * 24 * 60 * 60 * 1000);
        String refreshToken = Jwts.builder()
                .setSubject("refresh")
                .setIssuedAt(now)
                .setExpiration(refreshExpired)
                .claim("auth", authenticates)
                .claim("id", user.getId())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtDto.builder()
                .type("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpired(accessExpired.getTime())
                .build();
    }

    public Authentication getAuthentication(String token){
        Claims claims = parseToken(token);

        if(claims.get("auth") == null){
            throw new RuntimeException("토큰에 권한 정보가 없습니다."); // TODO 커스텀 예외로 교체 필요
        }

        Collection<? extends GrantedAuthority> auth = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Long id = claims.get("id", Long.class);
        UserEntity user = userEntityRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾지 못했습니다."));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        return new UsernamePasswordAuthenticationToken(userDetails, null, auth);
    }

    public Claims parseToken(String token){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage()); // TODO 커스텀 예외로 교체 필요
        }
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (Exception e){ // TODO catch 추가 필요
            return false;
        }
    }
}
