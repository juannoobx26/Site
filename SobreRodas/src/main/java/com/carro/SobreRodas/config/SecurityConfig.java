package com.carro.SobreRodas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

/**
 * Classe de configuração para o Spring Security.
 * Define as regras de autorização, configuração de login, logout e outros aspectos de segurança.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura a cadeia de filtros de segurança (Security Filter Chain).
     * Define quais URLs são públicas e quais requerem autenticação.
     * @param http O objeto HttpSecurity para configurar a segurança web.
     * @return A cadeia de filtros de segurança construída.
     * @throws Exception Se ocorrer um erro na configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Permite acesso público a URLs estáticas, páginas principais e de autenticação.
                .requestMatchers("/", "/index", "/eventos", "/comparativo", "/noticia/**", "/pesquisa", "/usuario/login", "/usuario/cadastro", "/usuario/esqueci-senha", "/usuario/resetar-senha", "/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                // Apenas usuários com a role 'ADMIN' podem acessar URLs que começam com /admin/
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated() // Exige autenticação para qualquer outra requisição.
            )
            .formLogin(form -> form
                .loginPage("/usuario/login") // Página de login customizada.
                .loginProcessingUrl("/usuario/login") // URL que processa o login.
                .defaultSuccessUrl("/") // Página para redirecionar após login bem-sucedido.
                .failureUrl("/usuario/login?error=true") // Página para redirecionar após falha no login.
                .permitAll() // Permite acesso à página de login para todos.
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/usuario/logout")) // URL que aciona o logout.
                .logoutSuccessUrl("/usuario/login?logout") // Página para redirecionar após logout.
                .permitAll() // Permite acesso à funcionalidade de logout para todos.
            )
            .csrf(csrf -> csrf.disable()); // Simplificado para desenvolvimento. Em produção, configure o CSRF corretamente.
        
        return http.build();
    }
    
    /**
     * Expõe o AuthenticationManager como um Bean para ser usado em outras partes da aplicação.
     * @param authConfig A configuração de autenticação.
     * @return O AuthenticationManager.
     * @throws Exception Se ocorrer um erro ao obter o AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Define o BCryptPasswordEncoder como o codificador de senhas padrão da aplicação.
     * @return Uma instância do BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}