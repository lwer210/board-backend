package com.example.board.article.service.impl;

import com.example.board.article.controller.request.ArticleRequest;
import com.example.board.article.controller.response.ArticleResponse;
import com.example.board.article.persistence.repository.ArticleEntityRepository;
import com.example.board.article.service.ArticleService;
import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.paging.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleEntityRepository articleEntityRepository;

    @Override
    public PagingResponse<ArticleResponse> list(Pageable pageable) {
        return null;
    }

    @Override
    public PagingResponse<ArticleResponse> getUserArticleList(CustomUserDetails userDetails) {
        return null;
    }

    @Override
    public ArticleResponse info(Long articleId) {
        return null;
    }

    @Override
    public ArticleResponse add(CustomUserDetails userDetails, ArticleRequest request) {
        return null;
    }

    @Override
    public ArticleResponse update(CustomUserDetails userDetails, ArticleRequest request) {
        return null;
    }

    @Override
    public ArticleResponse delete(Long articleId) {
        return null;
    }
}
