package com.example.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "BOARD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data // @Getter @Setter
@ToString
public class Board {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String content;
    private String author;
    private Date createdDt;
}