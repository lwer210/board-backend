package com.example.board.article.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArticleRequest {

    Long articleId;

    private String title;

    private String content;

    private String publicYn;
}
