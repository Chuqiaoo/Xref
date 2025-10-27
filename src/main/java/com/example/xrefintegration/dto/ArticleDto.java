package com.example.xrefintegration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private String title;
    private LocalDate publishDate;
    private List<AuthorDto> author;
    private ReviewDto review;
}
