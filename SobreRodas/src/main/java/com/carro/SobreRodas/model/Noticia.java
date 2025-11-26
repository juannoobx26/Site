package com.carro.SobreRodas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Noticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    private String imagem;

    private String data;

    private String autor;

    private String tag;

    @Column(length = 500)
    private String resumo;

    // Construtores
    public Noticia() {
    }

    public Noticia(String titulo, String conteudo, String resumo, String imagem, String data, String autor, String tag) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.resumo = resumo;
        this.imagem = imagem;
        this.data = data;
        this.autor = autor;
        this.tag = tag;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTag() { return tag; }

    public void setTag(String tag) { this.tag = tag; }

    public String getResumo() { return resumo; }

    public void setResumo(String resumo) { this.resumo = resumo; }
}