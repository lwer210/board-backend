package com.example.board.user.controller.response;

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
public class UserInfoResponse {

    private Long id;

    private String email;

    private String nickname;

    private String role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<ArticleResponse> articles;
}
