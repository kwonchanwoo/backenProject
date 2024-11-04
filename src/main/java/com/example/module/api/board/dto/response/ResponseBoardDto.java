package com.example.module.api.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBoardDto {
    private Long id; //id (PK)
    private String title; // 제목
    private String contents; // 내용
    private int views; // 조회수
    private String author; // 작성자
    private int board_comment_count; // 게시판 댓글 수
}
