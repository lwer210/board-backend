package com.example.board.article.service;

import com.example.board.article.controller.request.ArticleRequest;
import com.example.board.article.controller.request.UpdateArticleRequest;
import com.example.board.article.controller.response.ArticleInfoResponse;
import com.example.board.article.controller.response.ArticleResponse;
import com.example.board.article.controller.response.DeleteArticleResponse;
import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.paging.PagingResponse;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    public PagingResponse<ArticleResponse> list(Pageable pageable);

    public PagingResponse<ArticleResponse> getUserArticleList(CustomUserDetails userDetails, Pageable pageable);

    public ArticleInfoResponse info(Long articleId, CustomUserDetails customUserDetails);

    public ArticleResponse add(CustomUserDetails userDetails, ArticleRequest request);

    public ArticleResponse update(CustomUserDetails userDetails, UpdateArticleRequest request, Long articleSeq);

    public DeleteArticleResponse delete(Long articleId, CustomUserDetails customUserDetails);
}
