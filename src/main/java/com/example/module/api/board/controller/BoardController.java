package com.example.module.api.board.controller;

import com.example.module.api.board.dto.request.RequestBoardDto;
import com.example.module.api.board.dto.response.ResponseBoardDto;
import com.example.module.api.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public Page<ResponseBoardDto> getBoardList(@RequestParam(required = false) Map<String,Object> filters, Pageable pageable){
        return boardService.getBoardList(filters, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void postBoard(@RequestBody RequestBoardDto requestBoardDto){
        boardService.postBoard(requestBoardDto);
    }
}
