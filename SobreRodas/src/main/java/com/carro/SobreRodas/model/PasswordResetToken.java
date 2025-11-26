package com.carro.SobreRodas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade JPA que representa um token de redefinição de senha.
 * Armazena o token, o usuário associado e uma data de expiração.
 */
@Entity
public class PasswordResetToken {

    // Define a duração da validade do token em minutos (24 horas).
    private static final int EXPIRATION = 60 * 24; // Token expira em 24 horas

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // O token único gerado para a redefinição de senha.
    private String token;

    // Relacionamento um-para-um com a entidade Usuario. Cada token pertence a um único usuário.
    @OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "usuario_id")
    private Usuario usuario;

    // Data e hora em que o token irá expirar.
    private LocalDateTime expiryDate;

    /**
     * Construtor padrão. Calcula a data de expiração ao ser instanciado.
     */
    public PasswordResetToken() {
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    /**
     * Construtor que associa um token e um usuário.
     * @param token O token de redefinição.
     * @param usuario O usuário associado.
     */
    public PasswordResetToken(String token, Usuario usuario) {
        this();
        this.token = token;
        this.usuario = usuario;
    }

    /**
     * Método privado para calcular a data de expiração com base no tempo atual.
     * @param expiryTimeInMinutes O tempo de validade em minutos.
     * @return A data e hora de expiração.
     */
    private LocalDateTime calculateExpiryDate(int expiryTimeInMinutes) {
        return LocalDateTime.now().plusMinutes(expiryTimeInMinutes);
    }

    /**
     * Reseta a data de expiração do token, útil ao reutilizar um token existente.
     */
    public void resetExpiryDate() {
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    /**
     * Verifica se o token já expirou.
     * @return true se a data atual for posterior à data de expiração, false caso contrário.
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getExpiryDate() { return expiryDate; }
}