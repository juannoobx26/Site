package com.carro.SobreRodas.controller;

import com.carro.SobreRodas.model.Noticia;
import com.carro.SobreRodas.service.NoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Controller responsável por gerenciar as requisições das páginas públicas principais,
 * como a página inicial, página de eventos e de comparativos.
 */
@Controller
public class HomeController {

    private final NoticiaService noticiaService;

    @Autowired
    public HomeController(NoticiaService noticiaService) {
        this.noticiaService = noticiaService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("noticiaDestaque", noticiaService.findDestaque().orElse(null)); // Busca a notícia mais recente como destaque
        model.addAttribute("ultimasNoticias", noticiaService.findUltimasNoticias()); // Busca as 3 notícias mais recentes após o destaque.
        model.addAttribute("maisLidas", noticiaService.findMaisLidas()); // Busca as mais recentes da tag "Automotivo".
        model.addAttribute("testesEAvaliacoes", noticiaService.findTestesEAvaliacoes()); // Busca as mais recentes da tag "Avaliação".
        model.addAttribute("comparativosHome", noticiaService.findComparativosHome()); // Busca as mais recentes da tag "Comparativos".
        return "index";
    }

    @GetMapping("/eventos")
    public String eventos(Model model) {
        List<String> tags = Arrays.asList("Exposição");
        model.addAttribute("eventos", noticiaService.findByTags(tags));
        return "eventos";
    }

    @GetMapping("/comparativo")
    public String comparativo(Model model) {
        // Busca notícias apenas com a tag "Comparativos" para garantir a relevância.
        model.addAttribute("comparativos", noticiaService.findByTags(Arrays.asList("Comparativos")));
        return "comparativo";
    }

    @GetMapping("/noticia/{id}")
    public String noticia(@PathVariable("id") Long id, Model model) {
        Optional<Noticia> noticiaOpt = noticiaService.findById(id);
        if (noticiaOpt.isPresent()) {
            model.addAttribute("noticia", noticiaOpt.get());
            model.addAttribute("relacionadas", noticiaService.findLatest3());
            return "noticia";
        }
        return "redirect:/";
    }

    /**
     * Mapeia a requisição para "/pesquisa", exibindo os resultados da busca.
     * @param termo O termo de busca vindo do parâmetro "q" da URL.
     * @param model O objeto Model para adicionar os resultados e o termo pesquisado.
     * @return O nome da view "pesquisa".
     */
    @GetMapping("/pesquisa")
    public String pesquisa(@RequestParam(value = "q", required = false) String termo, Model model) {
        List<Noticia> resultados = Collections.emptyList();
        if (termo != null && !termo.trim().isEmpty()) {
            resultados = noticiaService.search(termo);
        }
        // Se a busca não retornar resultados ou o termo for vazio, a lista estará vazia,
        // o que pode ser tratado na view para exibir uma mensagem "Nenhum resultado encontrado".
        model.addAttribute("resultados", resultados);
        model.addAttribute("termo", termo);
        return "pesquisa";
    }
}