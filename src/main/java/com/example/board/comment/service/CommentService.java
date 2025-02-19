package com.example.board.comment.service;

import com.example.board.comment.controller.request.CommentRequest;
import com.example.board.comment.controller.response.CommentResponse;
import com.example.board.comment.controller.response.DeleteCommentResponse;
import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.paging.PagingResponse;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    public CommentResponse add(Long articleSeq, CommentRequest request, CustomUserDetails customUserDetails);

    public PagingResponse<CommentResponse> list(Long articleSeq, Pageable pageable, CustomUserDetails customUserDetails);

    public CommentResponse update(Long commentSeq, CommentRequest request, CustomUserDetails customUserDetails);

    public DeleteCommentResponse delete(Long commentSeq, CustomUserDetails customUserDetails);

    public void countAdd(Long commentSeq, CustomUserDetails customUserDetails);
}
