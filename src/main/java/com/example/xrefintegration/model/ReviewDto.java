package com.example.xrefintegration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private String type;
    private String recommendation;
    private String stage;

}
