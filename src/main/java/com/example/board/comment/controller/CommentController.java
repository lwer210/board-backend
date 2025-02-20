package com.example.board.comment.controller;

import com.example.board.comment.controller.request.CommentLikeRequest;
import com.example.board.comment.controller.request.CommentRequest;
import com.example.board.comment.controller.response.CommentResponse;
import com.example.board.comment.controller.response.DeleteCommentResponse;
import com.example.board.comment.service.CommentService;
import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.exception.response.ExceptionResponse;
import com.example.board.common.paging.PagingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "댓글 전체 조회 API", description = "페이징 처리 된 댓글 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "401", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "인증되지 않은 사용자일 경우 반환")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{articleSeq}")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<PagingResponse<CommentResponse>> list(
            @Parameter(name = "articleSeq", description = "게시글 시퀀스", required = true)
            @PathVariable Long articleSeq,
            @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
            return ResponseEntity.ok(commentService.list(articleSeq, pageable, customUserDetails));
    }

    @Operation(summary = "댓글 수정 API", description = "댓글 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "401", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "인증되지 않은 사용자일 경우 반환"),
            @ApiResponse(responseCode = "403", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "다른 사용자가 작성한 댓글을 수정하려할 경우 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "댓글 또는 사용자를 찾지 못했을 경우 반환")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/update")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<CommentResponse> update(
            @Parameter(name = "commentSeq", description = "댓글 시퀀스", required = true)
            @RequestParam Long commentSeq,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "댓글 수정 요청 객체",
                    content = @Content(schema = @Schema(implementation = CommentRequest.class))
            )
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return ResponseEntity.ok(commentService.update(commentSeq, request, customUserDetails));
    }

    @Operation(summary = "댓글 삭제 API", description = "댓글 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteCommentResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "403", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "다른 사용자가 작성한 댓글을 수정하려할 경우 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "댓글 또는 사용자를 찾지 못했을 경우 반환")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/delete")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<DeleteCommentResponse> delete(
            @Parameter(name = "commentSeq", description = "댓글 시퀀스", required = true)
            @RequestParam Long commentSeq,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return ResponseEntity.ok(commentService.delete(commentSeq, customUserDetails));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/like")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<?> like(
            @RequestBody CommentLikeRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return ResponseEntity.ok(commentService.likeAdd(request, customUserDetails));
    }
}
