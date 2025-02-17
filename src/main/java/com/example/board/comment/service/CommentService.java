package com.example.board.comment.service;

import com.example.board.comment.controller.request.CommentRequest;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    public void add(Long articleSeq, CommentRequest request);

    public void list(Long articleSeq, Pageable pageable);

    public void update(Long commentSeq, CommentRequest request);

    public void delete(Long commentSeq);

    public void countList(Long commentSeq);
}
