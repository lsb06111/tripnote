package edu.example.tripnote.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PageResponseDTO<T> {
    private List<T> content;   // 현재 페이지 데이터
    private int page;          // 현재 페이지 번호
    private int size;          // 페이지 크기
    private int totalCount;    // 전체 결과 개수
    private int totalPages;    // 전체 페이지 수
    private boolean hasPrev;   // 이전 페이지 존재 여부
    private boolean hasNext;   // 다음 페이지 존재 여부
}
