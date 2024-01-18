package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.demo.domain.Board;

import java.util.Date;

@Data
@NoArgsConstructor
public class BoardDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Date createdDt;

    public BoardDTO(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.author = board.getAuthor();
        this.createdDt = board.getCreatedDt();
    }
}
