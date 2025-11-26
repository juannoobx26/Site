package com.carro.SobreRodas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * DTO para representar a resposta completa da NewsAPI.
 * A anotação @JsonIgnoreProperties(ignoreUnknown = true) é importante para
 * que o Jackson (biblioteca de conversão JSON) não falhe se a API retornar
 * campos que não mapeamos aqui.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsApiResponse {
    private String status;
    private int totalResults;
    private List<Article> articles;

    // Getters e Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTotalResults() { return totalResults; }
    public void setTotalResults(int totalResults) { this.totalResults = totalResults; }

    public List<Article> getArticles() { return articles; }
    public void setArticles(List<Article> articles) { this.articles = articles; }
}