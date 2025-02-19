package com.example.board.article.controller;

import com.example.board.article.controller.request.ArticleRequest;
import com.example.board.article.controller.request.UpdateArticleRequest;
import com.example.board.article.controller.response.ArticleInfoResponse;
import com.example.board.article.controller.response.ArticleResponse;
import com.example.board.article.controller.response.DeleteArticleResponse;
import com.example.board.article.service.ArticleService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
@Tag(name = "게시글 API Controller", description = "게시글 API")
public class ArticleController {

    private final ArticleService articleService;

    @Operation(summary = "게시물 전체 조회 API", description = "공개 여부가 Y인 게시물 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))
            }, description = "성공 시 반환")
    })
    @GetMapping("/list")
    public ResponseEntity<PagingResponse<ArticleResponse>> list(
            @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ){
        return ResponseEntity.ok(articleService.list(pageable));
    }

    @Operation(summary = "게시글 추가 API", description = "로그인 한 사용자 게시글 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "401", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "인증되지 않은 사용자일 경우 반환")
    })
    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_USER')")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<ArticleResponse> add(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "게시글 추가 요청 객체",
                    content = @Content(schema = @Schema(implementation = ArticleRequest.class))
            )
            @RequestBody ArticleRequest articleRequest
    ){
        return ResponseEntity.ok(articleService.add(customUserDetails, articleRequest));
    }

    @Operation(summary = "본인 게시글 조회 API", description = "로그인한 사용자 본인 게시글 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content ={
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "사용자를 찾지 못했을 경우 반환")
    })
    @GetMapping("/my/article")
    @PreAuthorize("hasRole('ROLE_USER')")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<PagingResponse<ArticleResponse>> myArticle(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ){
        return ResponseEntity.ok(articleService.getUserArticleList(customUserDetails, pageable));
    }

    @Operation(summary = "게시글 상세 조회 API", description = "게시글 상세 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleInfoResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "401", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "인증되지 않은 사용자일 경우 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(mediaType = "application", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "게시글 또는 사용자를 찾지 못했을 경우 반환")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{articleSeq}")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<ArticleInfoResponse> info(
            @Parameter(name = "articleSeq", required = true, description = "게시글 시퀀스")
            @PathVariable Long articleSeq,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return ResponseEntity.ok(articleService.info(articleSeq, customUserDetails));
    }

    @Operation(summary = "게시글 수정 API", description = "게시글 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "401", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "인증되지 않은 사용자일 경우 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "게시글 또는 사용자를 찾지 못했을 경우 반환")
    })
    @PatchMapping("/update/{articleSeq}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<?> update(
            @Parameter(name = "articleSeq", description = "게시글 시퀀스", required = true)
            @PathVariable Long articleSeq,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "게시글 수정 요청 객체",
                    content = @Content(schema = @Schema(implementation = UpdateArticleRequest.class))
            )
            @RequestBody UpdateArticleRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return ResponseEntity.ok(articleService.update(customUserDetails, request, articleSeq));
    }

    @Operation(summary = "게시글 삭제 API", description = "게시글 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteArticleResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "401", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "인증되지 않은 사용자일 경우 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "게시글 또는 사용자를 찾지 못했을 경우 반환")
    })
    @DeleteMapping("/{articleSeq}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<DeleteArticleResponse> delete(
            @Parameter(name = "articleSeq", description = "게시글 시퀀스", required = true)
            @PathVariable Long articleSeq,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    )
    {
        return ResponseEntity.ok(articleService.delete(articleSeq, customUserDetails));
    }
}
