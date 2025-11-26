package com.carro.SobreRodas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Serviço para lidar com o envio de e-mails.
 */
@Service
// Esta anotação garante que o EmailService só será criado se a propriedade "spring.mail.host" estiver definida.
@ConditionalOnProperty(name = "spring.mail.host")
public class EmailService {

    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    // Injeta o e-mail do remetente a partir do application.properties
    // O ": " fornece um valor padrão nulo se a propriedade não for encontrada, evitando erro na inicialização.
    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envia um e-mail de redefinição de senha.
     * @param to O e-mail do destinatário.
     * @param token O token de redefinição de senha.
     */
    @Async
    public void sendPasswordResetEmail(String to, String token) {
        // A URL base deve ser configurada em application.properties para ser flexível
        String resetUrl = "http://localhost:8080/usuario/resetar-senha?token=" + token;
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("Redefinição de Senha - SobreRodas");
            message.setText("Olá,\n\nPara redefinir sua senha, clique no link abaixo:\n" + resetUrl +
                            "\n\nSe você não solicitou esta redefinição, por favor, ignore este e-mail.");
            
            mailSender.send(message);
            logger.info("E-mail de redefinição de senha enviado para {}", to);
        } catch (MailException e) {
            // Loga o erro em vez de quebrar a aplicação.
            // Em um sistema de produção, você poderia adicionar o e-mail a uma fila de retentativa.
            logger.error("Falha ao enviar e-mail de redefinição de senha para {}: {}", to, e.getMessage());
        }
    }
}