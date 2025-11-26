package com.carro.SobreRodas.repository;

import com.carro.SobreRodas.model.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Long> {

    // Métodos para a página inicial
    Optional<Noticia> findFirstByOrderByIdDesc();
    Optional<Noticia> findFirstByTagOrderByIdDesc(String tag);
    List<Noticia> findTop4ByOrderByIdDesc();
    List<Noticia> findTop3ByTagInOrderByIdDesc(List<String> tags);
    List<Noticia> findTop2ByTagInOrderByIdDesc(List<String> tags);

    // Método para a página de notícia
    List<Noticia> findTop3ByOrderByIdDesc();

    // Método para busca
    List<Noticia> findByTituloContainingIgnoreCaseOrResumoContainingIgnoreCase(String titulo, String resumo);

    // Método para páginas de categoria
    List<Noticia> findByTagIn(List<String> tags);

    // Método para o NewsApiService verificar duplicatas
    boolean existsByTitulo(String titulo);
}