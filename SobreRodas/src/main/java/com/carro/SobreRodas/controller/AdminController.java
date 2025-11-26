package com.carro.SobreRodas.controller;

import com.carro.SobreRodas.model.Noticia;
import com.carro.SobreRodas.service.FileStorageService;
import com.carro.SobreRodas.service.NoticiaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controller para gerenciar as operações de CRUD (Create, Read, Update, Delete) de Notícias.
 * Acessível apenas por usuários com a role 'ADMIN'.
 */
@Controller
@RequestMapping("/admin/noticias")
public class AdminController {

    private final NoticiaService noticiaService;
    private final FileStorageService fileStorageService;

    @Autowired
    public AdminController(NoticiaService noticiaService, FileStorageService fileStorageService) {
        this.noticiaService = noticiaService;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Lista todas as notícias para gerenciamento.
     */
    @GetMapping
    public String listarNoticias(Model model) {
        model.addAttribute("noticias", noticiaService.findAll());
        return "admin/noticias";
    }

    /**
     * Exibe o formulário para criar uma nova notícia.
     */
    @GetMapping("/nova")
    public String novaNoticiaForm(Model model) {
        model.addAttribute("noticia", new Noticia());
        return "admin/noticia-form";
    }

    /**
     * Processa o envio do formulário de criação de notícia.
     */
    @PostMapping("/nova")
    public String salvarNovaNoticia(@Valid @ModelAttribute("noticia") Noticia noticia,
                                    BindingResult result,
                                    @RequestParam("imagemFile") MultipartFile imagemFile,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/noticia-form";
        }

        if (!imagemFile.isEmpty()) {
            String fileName = fileStorageService.storeFile(imagemFile);
            noticia.setImagem("/uploads/" + fileName);
        }
        noticiaService.save(noticia);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Notícia criada com sucesso!");
        return "redirect:/admin/noticias";
    }

    /**
     * Exibe o formulário para editar uma notícia existente.
     */
    @GetMapping("/editar/{id}")
    public String editarNoticiaForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Noticia> noticiaOpt = noticiaService.findById(id);
        if (noticiaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Notícia não encontrada.");
            return "redirect:/admin/noticias";
        }
        model.addAttribute("noticia", noticiaOpt.get());
        return "admin/noticia-form";
    }

    /**
     * Processa o envio do formulário de edição de notícia.
     */
    @PostMapping("/editar/{id}")
    public String salvarEdicaoNoticia(@PathVariable("id") Long id, @Valid @ModelAttribute("noticia") Noticia noticia, BindingResult result, @RequestParam("imagemFile") MultipartFile imagemFile, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/noticia-form";
        }

        if (!imagemFile.isEmpty()) {
            String fileName = fileStorageService.storeFile(imagemFile);
            noticia.setImagem("/uploads/" + fileName);
        } else {
            // Mantém a imagem antiga se nenhuma nova for enviada
            noticiaService.findById(id).ifPresent(noticiaExistente -> noticia.setImagem(noticiaExistente.getImagem()));
        }
        noticia.setId(id); // Garante que estamos atualizando a notícia correta
        noticiaService.save(noticia);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Notícia atualizada com sucesso!");
        return "redirect:/admin/noticias";
    }

    /**
     * Deleta uma notícia.
     */
    @GetMapping("/deletar/{id}")
    public String deletarNoticia(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            noticiaService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Notícia deletada com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao deletar notícia.");
        }
        return "redirect:/admin/noticias";
    }
}