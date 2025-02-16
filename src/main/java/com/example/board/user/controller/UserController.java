package com.example.board.user.controller;

import com.example.board.auth.controller.response.UserResponse;
import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.exception.response.ExceptionResponse;
import com.example.board.common.paging.PagingResponse;
import com.example.board.user.controller.request.ModifyRequest;
import com.example.board.user.controller.response.DeleteResponse;
import com.example.board.user.controller.response.UserInfoResponse;
import com.example.board.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "사용자 API Controller", description = "사용자 관련 API Controller")
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 전체 조회 API", description = "사용자 전체 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))
            }, description = "성공 시 반환")
    })
    @GetMapping("/list")
    public ResponseEntity<PagingResponse<UserResponse>> list(
            @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.list(pageable));
    }

    @Operation(summary = "사용자 정보 조회 API", description = "jwt 토큰을 사용하여 인증된 사용자 정보를 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfoResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "401", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "인증되지 않은 사용자일 경우 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "사용자를 찾지 못했을 경우 반환")
    })
    @GetMapping("/info")
    @PreAuthorize("hasRole('ROLE_USER')")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<UserInfoResponse> info(@AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(userService.info(userDetails));
    }

    @Operation(summary = "사용자 삭제 API", description = "사용자 계정 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "401", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "인증되지 않은 사용자일 경우 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "사용자를 찾지 못할 경우 반환")
    })
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<DeleteResponse> delete(@AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(userService.delete(userDetails));
    }

    @Operation(summary = "사용자 정보 수정 API", description = "로그인한 사용자 정보 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "401", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "인증되지 않은 사용자일 경우 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "사용자를 찾을 수 없을 경우 반환"),
            @ApiResponse(responseCode = "409", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "이미 가입된 이메일 또느 닉네임일 경우 반환")
    })
    @PatchMapping("/modify")
    @PreAuthorize("hasRole('ROLE_USER')")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<UserResponse> modify(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "사용자 정보 수정 요청 객체",
                    content = @Content(schema = @Schema(implementation = ModifyRequest.class))
            )
            @RequestBody ModifyRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.modify(request, userDetails));
    }
}
