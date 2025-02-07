package com.example.board.auth.controller;

import com.example.board.auth.controller.request.LoginRequest;
import com.example.board.auth.controller.request.PasswordUpdateRequest;
import com.example.board.auth.controller.request.RefreshRequest;
import com.example.board.auth.controller.request.RegisterRequest;
import com.example.board.auth.controller.response.LoginResponse;
import com.example.board.auth.controller.response.UserResponse;
import com.example.board.auth.service.AuthService;
import com.example.board.common.custom.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API Controller", description = "사용자 Auth API Controller")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "사용자 회원가입 API", description = "사용자 회원가입 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            }, description = "회원가입 성공 시 반환")
    })
    public ResponseEntity<UserResponse> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "사용자 회원가입 요청 객체",
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class))
            )
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "사용자 로그인 API", description = "사용자 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
            }, description = "로그인 성공 시 반환")
    })
    public ResponseEntity<LoginResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = ("로그인 요청 객체"),
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            )
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급 API", description = "refresh 토큰을 사용한 jwt 토큰 재발급 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
            }, description = "재발급 성공 시 반환")
    })
    public ResponseEntity<LoginResponse> refresh(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "토큰 재발급 요청 객체",
                    content = @Content(schema = @Schema(implementation = RefreshRequest.class))
            )
            @RequestBody RefreshRequest request
    ) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @Operation(summary = "비밀번호 재설정 API", description = "사용자 비밀번호 재설정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            })
    })
    @PatchMapping("/password/update")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<UserResponse> updatePassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "비밀번호 재설정 요청 객체",
                    content = @Content(schema = @Schema(implementation = PasswordUpdateRequest.class))
            )
            @RequestBody PasswordUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.ok(authService.updatePassword(request, userDetails));
    }
}
