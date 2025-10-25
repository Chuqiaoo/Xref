package com.example.xrefintegration.controller;


import com.example.xrefintegration.model.Article;
import com.example.xrefintegration.service.CrossrefService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;


@RestController
@Tag(name = "CrossRef API", description = "Crossref integration service")
@RequiredArgsConstructor
public class CrossRefController {

    private final CrossrefService crossrefService;

    @Operation(summary = "Fetch articl info by DOI")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "406", description = "Not acceptable according to the accept headers sent in the request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/get-article-info-by-doi")
    public List<Article> getArticleInfoByDoi(@RequestBody List<String> dois) {
        if (dois.size() > 200) throw new InvalidParameterException("DOI must be less than 200");
        return crossrefService.fetchAndSaveArticles(dois);

    }

}
