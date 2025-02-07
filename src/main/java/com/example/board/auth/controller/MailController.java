package com.example.board.auth.controller;

import com.example.board.auth.controller.request.PasswordResetRequest;
import com.example.board.auth.controller.response.UserResponse;
import com.example.board.auth.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
@Tag(name = "이메일 발송 API Controller", description = "이메일 발송 API Controller")
public class MailController {

    private final MailService mailService;

    @Operation(summary = "임시 비밀번호 발급 API", description = "임시 비밀번호를 발급 후 이메일을 발송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            }, description = "성공 시 반환")
    })
    @PostMapping("/password/reset")
    public ResponseEntity<UserResponse> reset(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "임시 비밀번호 발급 요청 객체",
                    content = @Content(schema = @Schema(implementation = PasswordResetRequest.class))
            )
            @RequestBody PasswordResetRequest request
    ){
        return ResponseEntity.ok(mailService.reset(request));
    }
}
