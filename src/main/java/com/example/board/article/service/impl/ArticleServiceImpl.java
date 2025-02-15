package com.example.board.article.service.impl;

import com.example.board.article.controller.request.ArticleRequest;
import com.example.board.article.controller.response.ArticleResponse;
import com.example.board.article.persistence.entity.ArticleEntity;
import com.example.board.article.persistence.repository.ArticleEntityRepository;
import com.example.board.article.service.ArticleService;
import com.example.board.auth.persistence.entity.UserEntity;
import com.example.board.auth.persistence.repository.UserEntityRepository;
import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.exception.UnauthorizedException;
import com.example.board.common.exception.UserNotFoundException;
import com.example.board.common.paging.Pagination;
import com.example.board.common.paging.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleEntityRepository articleEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Override
    public PagingResponse<ArticleResponse> list(Pageable pageable) {
        Page<ArticleEntity> page = articleEntityRepository.findAllByPublicYn(pageable, "Y");

        Pagination pagination = Pagination.builder()
                .size(page.getSize())
                .numberOfElement(page.getNumberOfElements())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .number(page.getNumber())
                .build();

        List<ArticleEntity> list = page.getContent();

        List<ArticleResponse> result = list.stream().map(value -> {
            return ArticleResponse.builder()
                    .articleId(value.getId())
                    .title(value.getTitle())
                    .content(value.getContent())
                    .publicYn(value.getPublicYn())
                    .build();
        }).toList();

        return PagingResponse.<ArticleResponse>builder()
                .content(result)
                .pagination(pagination)
                .build();
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
        if(userDetails == null){
            throw new UnauthorizedException();
        }

        UserEntity user = userEntityRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        ArticleEntity entity = ArticleEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .publicYn(request.getPublicYn())
                .user(user)
                .build();

        articleEntityRepository.save(entity);

        return ArticleResponse.builder()
                .articleId(entity.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .publicYn(request.getPublicYn())
                .build();
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
