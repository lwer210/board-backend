package com.example.board.article.controller.response;

import com.example.board.comment.controller.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleInfoResponse {

    private Long articleId;

    private String title;

    private String content;

    private String publicYn;

    private List<CommentResponse> comments;
}
