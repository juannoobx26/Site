package com.carro.SobreRodas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para representar a fonte (source) de um artigo.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Source {
    private String id;
    private String name;

    // Getters e Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}