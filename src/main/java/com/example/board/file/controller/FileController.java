package com.example.board.file.controller;

import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.exception.response.ExceptionResponse;
import com.example.board.file.controller.dto.DownloadFileDto;
import com.example.board.file.controller.response.FileResponse;
import com.example.board.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
@Tag(name = "파일 API Controller")
public class FileController {

    private final FileService fileService;

    @Operation(summary = "파일 업로드 API", description = "파일 업로드 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FileResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "400", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "파일이 비어있을 경우 반환"),
            @ApiResponse(responseCode = "401", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "인증되지 않은 사용자일 경우 반환"),
            @ApiResponse(responseCode = "500", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "파일 업로드 중 예외가 발생했을 경우 반환")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<FileResponse> upload(
            @Parameter(name = "file", description = "파일", required = true)
            @RequestParam(name = "file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            ){
        return ResponseEntity.ok(fileService.upload(file, customUserDetails));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/download")
    @SecurityRequirement(name = "Authentication Bearer")
    public ResponseEntity<Resource> download(
            @RequestParam(name = "fileSeq") Long fileSeq,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        DownloadFileDto dto = fileService.download(fileSeq, customUserDetails);

        Resource resource = dto.getResource();
        String contentDisposition = dto.getContentDisposition();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
