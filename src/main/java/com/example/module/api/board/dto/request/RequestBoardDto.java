package com.example.module.api.board.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestBoardDto {
    private String title;
    private String contents;
}
