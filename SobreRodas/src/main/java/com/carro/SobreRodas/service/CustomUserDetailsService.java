package com.carro.SobreRodas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.carro.SobreRodas.model.Usuario;
import com.carro.SobreRodas.repository.UsuarioRepository;

/**
 * Implementação customizada de UserDetailsService.
 * É usada pelo Spring Security para carregar os detalhes de um usuário a partir do banco de dados
 * durante o processo de autenticação.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Repositório para buscar dados do usuário.
    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor para injeção de dependência do UsuarioRepository.
     * @param usuarioRepository O repositório de usuários.
     */
    @Autowired
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Carrega os detalhes de um usuário pelo seu e-mail (usado como nome de usuário).
     * @param email O e-mail do usuário a ser autenticado.
     * @return Um objeto UserDetails contendo as informações do usuário.
     * @throws UsernameNotFoundException se o usuário não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));
        
        // Retorna o próprio objeto Usuario, pois ele já implementa a interface UserDetails.
        return usuario;
    }
}