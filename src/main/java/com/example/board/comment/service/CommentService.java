package com.example.board.comment.service;

import com.example.board.comment.controller.request.CommentRequest;
import com.example.board.comment.controller.response.CommentResponse;
import com.example.board.common.custom.CustomUserDetails;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    public CommentResponse add(Long articleSeq, CommentRequest request, CustomUserDetails customUserDetails);

    public void list(Long articleSeq, Pageable pageable);

    public void update(Long commentSeq, CommentRequest request, CustomUserDetails customUserDetails);

    public void delete(Long commentSeq, CustomUserDetails customUserDetails);

    public void countAdd(Long commentSeq, CustomUserDetails customUserDetails);
}
