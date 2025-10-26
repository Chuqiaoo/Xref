package com.example.xrefintegration.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable // creates a separate table automatically and as an element collection tied to Article
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Review {
    private String type;
    private String recommendation;
    private String stage;

}
