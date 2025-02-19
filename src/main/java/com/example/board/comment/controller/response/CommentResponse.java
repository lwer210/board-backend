package com.example.board.comment.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Long commentId;

    private String nickname;

    private String answer;

    private Long like;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
