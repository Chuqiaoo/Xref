package com.example.xrefintegration.controller;


import com.example.xrefintegration.model.Article;
import com.example.xrefintegration.service.CrossrefService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;
import java.util.List;


@RestController
@Tag(name = "Article", description = "Article Events")
@RequiredArgsConstructor
public class CrossRefController {

    private final CrossrefService crossrefService;

    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "406", description = "Not acceptable according to the accept headers sent in the request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/get-article-info-by-doi")
    @Operation(
            summary = "Fetch article info by DOI",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "[\"10.1038/s41586-020-2649-2\", \"10.1016/j.cell.2021.03.012\"," +
                                            "\"10.5555/12345678\", \"10.5555/ixgpy3see9\"]"
                            )
                    )
            )
    )
    public List<Article> getArticleInfoByDoi(@RequestBody List<String> dois) {
        if (dois.size() > 200) throw new InvalidParameterException("DOI must be less than 200");
        return crossrefService.fetchAndSaveArticles(dois);
    }

}
