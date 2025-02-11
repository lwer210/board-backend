package com.example.board.common.paging;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagination {

    private int size;

    private int number;

    private int numberOfElement;

    private int totalPage;

    private Long totalElement;
}
