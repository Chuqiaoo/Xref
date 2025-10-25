package com.example.xrefintegration.controller;

import com.example.xrefintegration.model.Article;
import com.example.xrefintegration.repository.ArticleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CrossRefControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleRepository articleRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void cleanDb() {
        articleRepository.deleteAll();
    }

    @Test
    void getArticleInfoByDoi() throws Exception {
        List<String> dois = List.of("10.3180/r-hsa-5619084.3");
        String requestBody = objectMapper.writeValueAsString(dois);
        mockMvc.perform(post("/get-article-info-by-doi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
        // verify that the article is actually saved in DB
        List<Article> savedArticles = articleRepository.findAll();
        assertThat(savedArticles).hasSize(1);
    }

    @Test
    void testDoiNotFound() throws Exception {
        String requestBody = "[\"10.5555/nonexistent\"]";
        mockMvc.perform(post("/get-article-info-by-doi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testPublishedDate() throws Exception {
        String requestBody = "[\"10.1016/j.cell.2021.03.012\"]"; // no day info

        mockMvc.perform(post("/get-article-info-by-doi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].publishDate").value("2021-04-01")); // default month/day
    }

}