package com.carro.SobreRodas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.carro.SobreRodas.model.Usuario;
import com.carro.SobreRodas.model.PasswordResetToken;
 
import com.carro.SobreRodas.repository.UsuarioRepository;
import com.carro.SobreRodas.repository.PasswordResetTokenRepository;

import java.util.Optional;

/**
 * Serviço que lida com a lógica de negócio para usuários.
 * Inclui cadastro, busca, e o fluxo de redefinição de senha.
 */
@Service
public class UsuarioService {

    // Repositórios e componentes necessários para o serviço.
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository tokenRepository;
    
    /**
     * Construtor para injeção de dependências.
     * @param usuarioRepository Repositório de usuários.
     * @param passwordEncoder Codificador de senhas.
     * @param tokenRepository Repositório de tokens de redefinição de senha.
     */
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, PasswordResetTokenRepository tokenRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }
    
    /**
     * Cadastra um novo usuário no sistema.
     * Verifica se o e-mail já existe e criptografa a senha antes de salvar.
     * @param usuario O objeto Usuario a ser cadastrado.
     * @return O usuário salvo.
     * @throws IllegalArgumentException se o e-mail já estiver em uso.
     */
    @Transactional
    public Usuario cadastrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        String senhaPlana = usuario.getSenha();

        // Define a pergunta e resposta de segurança padrão
        String perguntaPadrao = "Qual é o seu nome de usuário?";
        // A resposta será o nome do usuário, que também será criptografada.
        String respostaPlana = usuario.getNome();

        usuario.setSenha(passwordEncoder.encode(senhaPlana));
        usuario.setPerguntaSeguranca(perguntaPadrao);
        usuario.setRespostaSeguranca(passwordEncoder.encode(respostaPlana));
        
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Busca um usuário pelo seu endereço de e-mail.
     * @param email O e-mail a ser procurado.
     * @return Um Optional contendo o usuário se encontrado.
     */
    public Optional<Usuario> findByEmail(String email) {
        // Operações de leitura não precisam ser transacionais por padrão, mas podem ser para consistência
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Cria ou atualiza um token de redefinição de senha para um usuário.
     * @param usuario O usuário para o qual o token será criado.
     * @param token A string do token gerado.
     */
    @Transactional
    public void createPasswordResetTokenForUser(Usuario usuario, String token) {
        // Procura se já existe um token para este usuário
        PasswordResetToken existingToken = tokenRepository.findByUsuario(usuario);
        if (existingToken != null) {
            existingToken.setToken(token);
            existingToken.resetExpiryDate(); // Reseta a data de expiração
            tokenRepository.save(existingToken);
        } else {
            PasswordResetToken myToken = new PasswordResetToken(token, usuario);
            tokenRepository.save(myToken);
        }
    }

    /**
     * Valida um token de redefinição de senha.
     * @param token O token a ser validado.
     * @return null se o token for válido, ou uma mensagem de erro ("Token inválido", "Token expirado") caso contrário.
     */
    @Transactional(readOnly = true)
    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = tokenRepository.findByToken(token);

        if (passToken == null) {
            return "Token inválido";
        }

        if (passToken.isExpired()) {
            return "Token expirado";
        }

        return null; // Token válido
    }

    /**
     * Obtém o usuário associado a um token de redefinição de senha.
     * @param token O token.
     * @return Um Optional contendo o usuário se o token for válido.
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(tokenRepository.findByToken(token))
                .map(PasswordResetToken::getUsuario);
    }

    /**
     * Valida se a resposta de segurança fornecida corresponde à resposta armazenada.
     * @param usuario O usuário.
     * @param respostaFornecida A resposta em texto plano.
     * @return true se a resposta for válida, false caso contrário.
     */
    public boolean validarRespostaSeguranca(Usuario usuario, String respostaFornecida) {
        return passwordEncoder.matches(respostaFornecida, usuario.getRespostaSeguranca());
    }

    /**
     * Altera a senha de um usuário.
     * A nova senha é criptografada antes de ser salva.
     * @param usuario O usuário cuja senha será alterada.
     * @param newPassword A nova senha (em texto plano).
     */
    @Transactional
    public void changeUserPassword(Usuario usuario, String newPassword) {
        // Busca a instância mais recente do usuário do banco de dados para evitar problemas de estado.
        Usuario userToUpdate = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado para atualização de senha."));
        
        userToUpdate.setSenha(passwordEncoder.encode(newPassword));
        usuarioRepository.save(userToUpdate);
    }

    /**
     * Exclui um token de redefinição de senha do banco de dados após seu uso.
     * @param token O token a ser excluído.
     */
    @Transactional
    public void deleteToken(String token) {
        PasswordResetToken passToken = tokenRepository.findByToken(token);
        if (passToken != null) {
            tokenRepository.delete(passToken);
        }
    }
}