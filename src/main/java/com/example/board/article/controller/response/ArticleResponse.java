package com.example.board.article.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleResponse {

    private Long articleId;

    private String title;

    private String content;

    private String publicYn;

    private String fileUseYn;

    private Long fileSeq;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
