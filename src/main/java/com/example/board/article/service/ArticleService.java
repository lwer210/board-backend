package com.example.board.article.service;

import com.example.board.article.controller.request.ArticleRequest;
import com.example.board.article.controller.response.ArticleResponse;
import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.paging.PagingResponse;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    public PagingResponse<ArticleResponse> list(Pageable pageable);

    public PagingResponse<ArticleResponse> getUserArticleList(CustomUserDetails userDetails);

    public ArticleResponse info(Long articleId);

    public ArticleResponse add(CustomUserDetails userDetails, ArticleRequest request);

    public ArticleResponse update(CustomUserDetails userDetails, ArticleRequest request);

    public ArticleResponse delete(Long articleId);
}
