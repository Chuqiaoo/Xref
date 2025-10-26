package com.example.xrefintegration.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String given;
    private String family;
    private String sequence;
    private String oricd;
    private Boolean authenticatedOrcid;
    private String suffix;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Affiliation> affiliation = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "article_id")
    @JsonIgnore  // breaks the Article ↔ Author loop
    private Article article;
}

