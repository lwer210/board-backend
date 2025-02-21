package com.example.board.file.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {

    private Long id;

    private String originalFileName;

    private String fileName;

    private String path;

    private String extension;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
