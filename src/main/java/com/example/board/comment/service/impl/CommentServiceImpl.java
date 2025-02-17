package com.example.board.comment.service.impl;

import com.example.board.comment.controller.request.CommentRequest;
import com.example.board.comment.persistence.repository.CommentEntityRepository;
import com.example.board.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentEntityRepository commentEntityRepository;

    @Override
    public void add(Long articleSeq, CommentRequest request) {

    }

    @Override
    public void list(Long articleSeq, Pageable pageable) {

    }

    @Override
    public void update(Long commentSeq, CommentRequest request) {

    }

    @Override
    public void delete(Long commentSeq) {

    }

    @Override
    public void countList(Long commentSeq) {

    }
}
