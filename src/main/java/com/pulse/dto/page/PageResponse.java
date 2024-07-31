package com.pulse.dto.page;

import lombok.*;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class PageResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private int totalPages;
    private Long totalElements;
}
