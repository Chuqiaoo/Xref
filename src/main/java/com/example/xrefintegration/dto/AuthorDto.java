package com.example.xrefintegration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    private String given;
    private String family;
    private String sequence;
    private String oricd;
    private Boolean authenticatedOrcid;
    private String suffix;
    private List<AffiliationDto> affiliation = new ArrayList<>();
}
