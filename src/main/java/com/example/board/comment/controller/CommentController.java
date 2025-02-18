package com.example.board.comment.controller;

import com.example.board.comment.controller.request.CommentRequest;
import com.example.board.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add/{articleSeq}")
    public ResponseEntity<?> add(
            @PathVariable Long articleSeq,
            @RequestBody CommentRequest request
            ){
        return ResponseEntity.ok(commentService.add(articleSeq, request));
    }
}
