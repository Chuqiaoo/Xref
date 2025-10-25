package com.example.xrefintegration.service;


import com.example.xrefintegration.model.*;
import com.example.xrefintegration.repository.ArticleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor // auto-generates constructor for all final fields(constructor injection)
@Service
public class CrossrefService {

    private final ArticleRepository articleInfoRepository;
    private final RestTemplate restTemplate = new RestTemplate(); // HTTP client to make HTTP calls
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Article> fetchAndSaveArticles(List<String> dois) {

        List<Article> articles = new ArrayList<>();
        for (String doi : dois) {
            try {
                String url = "https://api.crossref.org/works/" + doi;
                JsonNode message = restTemplate.getForObject(url, JsonNode.class).get("message");
                // create article
                Article article = new Article();
                // title is a list, take the first element, why is a list?
                JsonNode titleNode = message.get("title");
                if (titleNode != null && titleNode.isArray() && !titleNode.isEmpty()) {
                    article.setTitle(titleNode.get(0).asText());
                }
                // authors
                JsonNode authorNodes = message.get("author");
                if (authorNodes != null && authorNodes.isArray()) {
                    List<Author> authors = new ArrayList<>();
                    for (JsonNode authorNode : authorNodes) {
                        Author author = objectMapper.treeToValue(authorNode, Author.class);
                        author.setArticle(article); // link back to parent
                        // link affiliations to this author
                        if (author.getAffiliation() != null) {
                            for (Affiliation affiliation : author.getAffiliation()) {
                                affiliation.setAuthor(author);
                            }
                        }
                        authors.add(author);
                    }
                    article.setAuthor(authors);
                }
                // data
                JsonNode publishedNode = message.get("published");
                if (publishedNode != null) {
                    Published published = objectMapper.treeToValue(publishedNode, Published.class);
                    article.setPublishDate(published.getPublishedDate());
                }
                // review
                JsonNode reviewNode = message.get("review");
                if (reviewNode != null) {
                    Review review = objectMapper.treeToValue(reviewNode, Review.class);
                    article.setReview(review);
                }
                // Save everything
                articleInfoRepository.save(article);
                articles.add(article);

            } catch (Exception e) {
                System.err.println("Error fetching DOI: " + doi + " -> " + e.getMessage());
            }
        }
        return articles;
    }
}
