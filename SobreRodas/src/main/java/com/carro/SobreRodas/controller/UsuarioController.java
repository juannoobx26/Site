package com.carro.SobreRodas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.carro.SobreRodas.model.Usuario;
import com.carro.SobreRodas.service.UsuarioService;
import com.carro.SobreRodas.service.EmailService;
import jakarta.validation.Valid;

import java.util.Optional;
import java.util.UUID;

/**
 * Controller para gerenciar todas as requisições relacionadas ao usuário,
 * como login, cadastro, perfil e redefinição de senha.
 */
@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final MessageSource messages;
    private EmailService emailService; // Agora não é mais final

    /**
     * Construtor para injeção de dependências.
     * @param usuarioService O serviço de usuário.
     * @param messages O provedor de mensagens.
     */
    @Autowired
    public UsuarioController(UsuarioService usuarioService, MessageSource messages) {
        this.usuarioService = usuarioService;
        this.messages = messages;
    }

    @Autowired(required = false) // Injeta o EmailService apenas se ele existir
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Exibe o formulário de login.
     * @return O nome da view "login".
     */
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
    
    /**
     * Exibe o formulário de cadastro.
     * @param model O objeto Model para adicionar um novo objeto Usuario ao formulário.
     * @return O nome da view "cadastro".
     */
    @GetMapping("/cadastro")
    public String cadastroForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }
    
    /**
     * Processa a submissão do formulário de cadastro.
     * @param usuario O objeto Usuario preenchido com os dados do formulário.
     * @param result O resultado da validação.
     * @param redirectAttributes Atributos para passar mensagens após o redirecionamento.
     * @return Redireciona para a página de login em caso de sucesso, ou de volta para o cadastro em caso de erro.
     */
    @PostMapping("/cadastro")
    public String cadastrar(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                // Se houver erros de validação, retorna para o formulário de cadastro
                return "cadastro";
            }
            usuarioService.cadastrar(usuario);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastro realizado com sucesso! Faça login para continuar.");
            return "redirect:/usuario/login";
        } catch (IllegalArgumentException e) {
            // Verifica a mensagem da exceção para dar um feedback mais preciso
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/usuario/cadastro";
        }
    }

    /**
     * Exibe o formulário "Esqueci minha senha".
     * @return O nome da view "esqueci-senha".
     */
    @GetMapping("/esqueci-senha")
    public String showForgotPasswordForm() {
        return "esqueci-senha";
    }

    /**
     * Processa a solicitação de redefinição de senha.
     * @param userEmail O e-mail do usuário que esqueceu a senha.
     * @param redirectAttributes Atributos para passar mensagens.
     * @return Redireciona para a página de redefinição de senha com um token.
     */
    @PostMapping("/esqueci-senha")
    public String processForgotPassword(@RequestParam("email") String userEmail, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(userEmail);

        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não foi encontrado um usuário com este e-mail.");
            return "redirect:/usuario/esqueci-senha";
        } else {
            Usuario usuario = usuarioOpt.get();
            String token = UUID.randomUUID().toString();
            usuarioService.createPasswordResetTokenForUser(usuario, token);

            // Tenta enviar o e-mail se o serviço estiver configurado.
            if (emailService != null) {
                emailService.sendPasswordResetEmail(usuario.getEmail(), token);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Um e-mail com instruções para redefinir sua senha foi enviado.");
            } else {
                // Se o serviço de e-mail não estiver ativo, exibe o link para desenvolvimento.
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Ambiente de desenvolvimento: O envio de e-mail está desativado.");
                redirectAttributes.addFlashAttribute("resetLink", "/usuario/resetar-senha?token=" + token);
            }
            return "redirect:/usuario/login";
        }
    }

    /**
     * Exibe o formulário para o usuário inserir a nova senha.
     * @param token O token de redefinição recebido por e-mail (simulado na URL).
     * @param model O objeto Model para passar o token para a view.
     * @param redirectAttributes Atributos para mensagens de erro.
     * @return O nome da view "resetar-senha" se o token for válido, ou redireciona para o login.
     */
    @GetMapping("/resetar-senha")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
        String result = usuarioService.validatePasswordResetToken(token);
        if (result != null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Token de redefinição inválido ou expirado.");
            return "redirect:/usuario/login";
        }

        model.addAttribute("token", token);
        return "resetar-senha";
    }

    /**
     * Processa a submissão do formulário de nova senha.
     * @param token O token de validação.
     * @param senha A nova senha.
     * @param confirmarSenha A confirmação da nova senha.
     * @param redirectAttributes Atributos para mensagens de sucesso ou erro.
     * @return Redireciona para a página de login.
     */
    @PostMapping("/resetar-senha")
    public String handlePasswordReset(@RequestParam("token") String token,
                                      @RequestParam("senha") String senha,
                                      @RequestParam("confirmarSenha") String confirmarSenha,
                                      RedirectAttributes redirectAttributes) {

        String result = usuarioService.validatePasswordResetToken(token);
        if (result != null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Token de redefinição inválido ou expirado.");
            return "redirect:/usuario/login";
        }

        if (!senha.equals(confirmarSenha)) {
            redirectAttributes.addFlashAttribute("mensagemErro", "As senhas não coincidem.");
            redirectAttributes.addAttribute("token", token);
            return "redirect:/usuario/resetar-senha";
        }

        Optional<Usuario> userOptional = usuarioService.getUserByPasswordResetToken(token);
        if (userOptional.isPresent()) {
            usuarioService.changeUserPassword(userOptional.get(), senha);
            usuarioService.deleteToken(token); // Invalida o token após o uso
        } else {
             redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao encontrar usuário associado ao token.");
             return "redirect:/usuario/login";
        }

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Sua senha foi alterada com sucesso!");
        return "redirect:/usuario/login";
    }
    
    /**
     * Exibe a página de perfil do usuário autenticado.
     * @param model O objeto Model para adicionar os dados do usuário.
     * @param redirectAttributes Atributos para mensagens de erro.
     * @return O nome da view "perfil" ou redireciona para o login se o usuário não for encontrado.
     */
    @GetMapping("/perfil")
    public String perfil(@AuthenticationPrincipal Usuario userDetails, Model model) {
        // O Spring injeta diretamente o objeto Usuario (que é um UserDetails) do usuário logado.
        // Isso evita a necessidade de buscar o usuário no banco de dados novamente.
        model.addAttribute("usuario", userDetails);
        return "perfil";
    }
}