package com.carro.SobreRodas.service;

import com.carro.SobreRodas.model.Noticia;
import com.carro.SobreRodas.repository.NoticiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Serviço que encapsula a lógica de negócio para operações relacionadas a notícias.
 * Interage com o NoticiaRepository para buscar dados do banco.
 */
@Service
public class NoticiaService {

    // Repositório para acesso aos dados das notícias.
    private final NoticiaRepository noticiaRepository;

    /**
     * Construtor para injeção de dependência do NoticiaRepository.
     * @param noticiaRepository O repositório de notícias.
     */
    @Autowired
    public NoticiaService(NoticiaRepository noticiaRepository) {
        this.noticiaRepository = noticiaRepository;
    }

    /**
     * Busca todas as notícias no banco de dados.
     * @return Uma lista de todas as notícias.
     */
    public List<Noticia> findAll() {
        return noticiaRepository.findAll();
    }

    /**
     * Busca uma notícia pelo seu ID.
     * @param id O ID da notícia.
     * @return Um Optional contendo a notícia se encontrada, ou vazio caso contrário.
     */
    public Optional<Noticia> findById(Long id) {
        return noticiaRepository.findById(id);
    }

    /**
     * Busca as 3 últimas notícias inseridas no banco.
     * @return Uma lista com as 3 notícias mais recentes.
     */
    public List<Noticia> findLatest3() {
        return noticiaRepository.findTop3ByOrderByIdDesc();
    }

    /**
     * Busca a notícia de destaque (considerada a primeira inserida).
     * @return Um Optional contendo a notícia de destaque.
     */
    public Optional<Noticia> findDestaque() {
        // A notícia de destaque é simplesmente a mais recente do banco de dados.
        return noticiaRepository.findFirstByOrderByIdDesc();
    }

    /**
     * Busca as 3 notícias mais recentes, excluindo a de destaque.
     * @return Uma lista de notícias.
     */
    public List<Noticia> findUltimasNoticias() {
        // Busca as 4 mais recentes e remove a primeira (que é o destaque)
        List<Noticia> recentes = noticiaRepository.findTop4ByOrderByIdDesc();
        if (recentes.size() > 1) {
            // Retorna uma sublista a partir do segundo elemento até o fim.
            return recentes.subList(1, recentes.size());
        }
        return Collections.emptyList();
    }

    /**
     * Busca as notícias "mais lidas" para a home.
     * (Simulado buscando por tags específicas, já que não há contador de visualizações).
     * @return Uma lista de notícias.
     */
    public List<Noticia> findMaisLidas() {
        // Em um sistema real, isso seria baseado em um contador de views.
        // Como simulação, buscamos as 3 mais recentes com a tag principal "Automotivo".
        return noticiaRepository.findTop3ByTagInOrderByIdDesc(Arrays.asList("Automotivo"));
    }

    /**
     * Busca as notícias de "testes e avaliações" para a home.
     * @return Uma lista de notícias.
     */
    public List<Noticia> findTestesEAvaliacoes() {
        // Busca as 2 notícias mais recentes com a tag "Avaliação".
        return noticiaRepository.findTop2ByTagInOrderByIdDesc(Arrays.asList("Avaliação"));
    }

    /**
     * Busca os "comparativos" para a home.
     * @return Uma lista de notícias.
     */
    public List<Noticia> findComparativosHome() {
        // Busca os 2 comparativos mais recentes com a tag "Comparativos".
        return noticiaRepository.findTop2ByTagInOrderByIdDesc(Arrays.asList("Comparativos"));
    }

    /**
     * Busca notícias por uma lista de tags.
     * @param tags A lista de tags a serem pesquisadas.
     * @return Uma lista de notícias que correspondem a qualquer uma das tags.
     */
    public List<Noticia> findByTags(List<String> tags) {
        return noticiaRepository.findByTagIn(tags);
    }

    /**
     * Busca notícias cujo título ou conteúdo contenham o termo de pesquisa.
     * @param termo O texto a ser pesquisado.
     * @return Uma lista de notícias que correspondem ao termo.
     */
    public List<Noticia> search(String termo) {
        return noticiaRepository.findByTituloContainingIgnoreCaseOrResumoContainingIgnoreCase(termo, termo);
    }

    /**
     * Salva (cria ou atualiza) uma notícia no banco de dados.
     * @param noticia A notícia a ser salva.
     * @return A notícia salva.
     */
    public Noticia save(Noticia noticia) {
        return noticiaRepository.save(noticia);
    }

    /**
     * Deleta uma notícia pelo seu ID.
     * @param id O ID da notícia a ser deletada.
     */
    public void deleteById(Long id) {
        noticiaRepository.deleteById(id);
    }
}