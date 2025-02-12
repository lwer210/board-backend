package com.example.board.user.service;

import com.example.board.auth.controller.response.UserResponse;
import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.paging.PagingResponse;
import com.example.board.user.controller.request.ModifyRequest;
import com.example.board.user.controller.response.DeleteResponse;
import org.springframework.data.domain.Pageable;


public interface UserService {
    public PagingResponse<UserResponse> list(Pageable pageable);

    public UserResponse info(CustomUserDetails userDetails);

    public DeleteResponse delete(CustomUserDetails userDetails);

    public UserResponse modify(ModifyRequest request, CustomUserDetails userDetails);
}
