package com.example.xrefintegration.service;


import com.example.xrefintegration.model.Affiliation;
import com.example.xrefintegration.model.Article;
import com.example.xrefintegration.model.Author;
import com.example.xrefintegration.dto.*;
import com.example.xrefintegration.model.Review;
import com.example.xrefintegration.repository.ArticleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor // auto-generates constructor for all final fields(constructor injection)
@Service
public class CrossrefService {

    private final ArticleRepository articleInfoRepository;
    private final RestTemplate restTemplate = new RestTemplate(); // HTTP client to make HTTP calls
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ArticleDto> fetchAndSaveArticles(List<String> dois) {

        List<ArticleDto> articles = new ArrayList<>();
        for (String doi : dois) {
            try {
                log.debug("Fetching article metadata for DOI: {}", doi);
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
                // Map to DTO
                ArticleDto dto = mapToDto(article);
                articles.add(dto);
                log.info("Saved article '{}' successfully for DOI {}", article.getTitle(), doi);
            } catch (Exception e) {
                log.error("Failed to fetch or save article for DOI {}: {}", doi, e.getMessage());
            }
        }
        log.info("Fetch completed: {} / {} DOIs processed", articles.size(), dois.size());
        return articles;
    }

    private ArticleDto mapToDto(Article article) {
        List<AuthorDto> authors = article.getAuthor() != null
                ? article.getAuthor().stream()
                .map(a -> new AuthorDto(
                        a.getGiven(),
                        a.getFamily(),
                        a.getSequence(),
                        a.getOricd(),
                        a.getAuthenticatedOrcid(),
                        a.getSuffix(),
                        a.getAffiliation() != null
                                ? a.getAffiliation().stream()
                                .map(aff -> new AffiliationDto(aff.getName()))
                                .toList()
                                : Collections.emptyList()
                ))
                .toList()
                : Collections.emptyList();


        ReviewDto reviewDto = null;
        if (article.getReview() != null) {
            Review r = article.getReview();
            reviewDto = new ReviewDto(r.getType(), r.getRecommendation(), r.getStage());
        }

        return new ArticleDto(article.getTitle(), article.getPublishDate(), authors, reviewDto);
    }


}
