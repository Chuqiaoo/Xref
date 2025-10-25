package com.example.xrefintegration.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoiRequest {
    @Schema(
            description = "An array of DOIs (up to 200 items)",
            example = "[\"10.1038/s41586-020-2649-2\", " +
                    "\"10.1016/j.cell.2021.03.012\", " +
                    "\"10.5555/12345678\"," +
                    "\"10.5555/ixgpy3see9\"]"
    )
    public List<String> dois;
}
