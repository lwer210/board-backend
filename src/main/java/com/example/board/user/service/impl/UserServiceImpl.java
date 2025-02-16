package com.example.board.user.service.impl;

import com.example.board.article.controller.response.ArticleResponse;
import com.example.board.article.persistence.entity.ArticleEntity;
import com.example.board.auth.controller.response.UserResponse;
import com.example.board.auth.persistence.entity.UserEntity;
import com.example.board.auth.persistence.repository.UserEntityRepository;
import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.exception.AlreadyRegisteredEmailException;
import com.example.board.common.exception.AlreadyRegisteredNicknameException;
import com.example.board.common.exception.UnauthorizedException;
import com.example.board.common.exception.UserNotFoundException;
import com.example.board.common.paging.Pagination;
import com.example.board.common.paging.PagingResponse;
import com.example.board.user.controller.request.ModifyRequest;
import com.example.board.user.controller.response.DeleteResponse;
import com.example.board.user.controller.response.UserInfoResponse;
import com.example.board.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserEntityRepository userEntityRepository;

    @Override
    public PagingResponse<UserResponse> list(Pageable pageable) {
        Page<UserEntity> all = userEntityRepository.findAll(pageable);

        Pagination pagination = Pagination.builder()
                .totalElement(all.getTotalElements())
                .size(all.getSize())
                .totalPage(all.getTotalPages())
                .number(all.getNumber())
                .numberOfElement(all.getNumberOfElements())
                .build();

        List<UserEntity> content = all.getContent();

        List<UserResponse> list = content.stream()
                .map(value -> {
                    return UserResponse.builder()
                            .id(value.getId())
                            .nickname(value.getNickname())
                            .role(value.getRole())
                            .email(value.getEmail())
                            .createdAt(value.getCreatedAt())
                            .updatedAt(value.getUpdatedAt())
                            .build();
                })
                .toList();

        return PagingResponse.<UserResponse>builder()
                .content(list)
                .pagination(pagination)
                .build();
    }

    @Override
    public UserInfoResponse info(CustomUserDetails userDetails) {
        if(userDetails == null){
            throw new UnauthorizedException();
        }

        UserEntity user = userEntityRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        List<ArticleResponse> articles = user.getArticles().stream()
                .map(value -> {
                    return ArticleResponse.builder()
                            .articleId(value.getId())
                            .title(value.getTitle())
                            .content(value.getContent())
                            .publicYn(value.getPublicYn())
                            .build();
                }).toList();

        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .articles(articles)
                .build();
    }

    @Override
    public DeleteResponse delete(CustomUserDetails userDetails) {
        if(userDetails == null){
            throw new UnauthorizedException();
        }

        UserEntity user = userEntityRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        userEntityRepository.deleteById(user.getId());

        return DeleteResponse.builder()
                .isDeleted(true)
                .build();
    }

    @Override
    public UserResponse modify(ModifyRequest request, CustomUserDetails userDetails) {
        if(userDetails == null){
            throw new UnauthorizedException();
        }

        if(userEntityRepository.existsByEmail(request.getEmail())){
            throw new AlreadyRegisteredEmailException("이미 존재하는 이메일입니다.");
        }

        if(userEntityRepository.existsByNickname(request.getNickname())){
            throw new AlreadyRegisteredNicknameException("이미 존재하는 닉네임입니다.");
        }

        UserEntity user = userEntityRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if(request.getEmail() != null){
            user.setEmail(request.getEmail());
        }

        if(request.getNickname() != null){
            user.setNickname(request.getNickname());
        }

        UserEntity newUser = userEntityRepository.save(user);

        return UserResponse.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .nickname(newUser.getNickname())
                .role(newUser.getRole())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .build();
    }
}
