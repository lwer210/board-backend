package com.example.board.comment.controller;

import com.example.board.comment.controller.request.CommentRequest;
import com.example.board.comment.controller.response.CommentResponse;
import com.example.board.comment.service.CommentService;
import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
@Tag(name = "댓글 API Controller", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 추가 API", description = "댓글 추가 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            })
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/add/{articleSeq}")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<CommentResponse> add(
            @Parameter(name = "articleSeq", required = true, description = "게시글 seq")
            @PathVariable Long articleSeq,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "댓글 추가 요청 객체",
                    content = @Content(schema = @Schema(implementation = CommentRequest.class))
            )
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            ){
        return ResponseEntity.ok(commentService.add(articleSeq, request, customUserDetails));
    }
}
