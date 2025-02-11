package com.example.board.common.paging;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PagingResponse<T> {

    private List<T> content;

    private Pagination pagination;
}
