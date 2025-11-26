package com.carro.SobreRodas.repository;

import com.carro.SobreRodas.model.PasswordResetToken;
import com.carro.SobreRodas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data JPA para a entidade {@link PasswordResetToken}.
 * Fornece métodos para interagir com a tabela de tokens de redefinição de senha no banco de dados.
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    /**
     * Encontra um {@link PasswordResetToken} pelo seu valor de token.
     * @param token O token a ser procurado.
     * @return O PasswordResetToken correspondente, ou null se não for encontrado.
     */
    PasswordResetToken findByToken(String token);

    /**
     * Encontra um {@link PasswordResetToken} associado a um {@link Usuario}.
     * @param usuario O usuário a ser procurado.
     * @return O PasswordResetToken correspondente, ou null se não for encontrado.
     */
    PasswordResetToken findByUsuario(Usuario usuario);
}