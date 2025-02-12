package com.example.board.user.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public PagingResponse<UserResponse> list(Pageable pageable) {
        Page<UserEntity> all = userEntityRepository.findAll(pageable);

        Pagination pagination = Pagination.builder()
                .totalElement(all.getTotalElements())
                .size(all.getSize())
                .totalPage(all.getTotalPages())
                .number(all.getNumber())
                .numberOfElement(all.getNumberOfElements())
                .build();

        List<UserResponse> list = all.stream()
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
                .collect(Collectors.toList());

        return PagingResponse.<UserResponse>builder()
                .content(list)
                .pagination(pagination)
                .build();
    }

    public UserResponse info(CustomUserDetails userDetails) {
        if(userDetails == null){
            throw new UnauthorizedException();
        }

        UserEntity user = userEntityRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

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
