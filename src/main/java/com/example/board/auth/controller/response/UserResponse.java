package com.example.board.auth.controller.response;

import com.example.board.article.controller.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String email;

    private String nickname;

    private String role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<ArticleResponse> articles;
}
