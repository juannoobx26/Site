package com.carro.SobreRodas.config;

import com.carro.SobreRodas.model.Noticia;
import com.carro.SobreRodas.model.Usuario;
import com.carro.SobreRodas.repository.NoticiaRepository;
import com.carro.SobreRodas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Componente que é executado na inicialização da aplicação.
 * Implementa CommandLineRunner para popular o banco de dados com dados iniciais (notícias),
 * garantindo que a aplicação tenha conteúdo ao ser iniciada pela primeira vez.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    // Repositório para interagir com a entidade Noticia no banco de dados.
    private final NoticiaRepository noticiaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor para injeção de dependência do NoticiaRepository.
     * @param noticiaRepository O repositório de notícias.
     */
    @Autowired
    public DataInitializer(NoticiaRepository noticiaRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.noticiaRepository = noticiaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Método executado automaticamente pelo Spring Boot na inicialização.
     * Verifica se o banco de dados já contém notícias e, se não, insere uma lista pré-definida.
     */
    @Override
    public void run(String... args) throws Exception {
        // Popula notícias apenas se o repositório estiver vazio
        if (noticiaRepository.count() == 0) {
            popularNoticias();
        }

        // Cria um usuário admin se ele não existir
        if (usuarioRepository.findByEmail("juanadm@gmail.com").isEmpty()) {
            criarUsuarioAdmin();
        }
    }

    private void popularNoticias() {
        System.out.println("Populando banco de dados com notícias...");
        List<Noticia> noticias = Arrays.asList(
                new Noticia("Novo SUV da Fiat Chega ao Mercado", "O mais recente lançamento da Fiat promete agitar o segmento de SUVs compactos.", "Conteúdo completo sobre o novo SUV...", "/images/fiat.jfif", "01/01/2024", "Revista Carro", "Automotivo"),
                new Noticia("Teste Completo: Honda Civic Híbrido", "Avaliamos a nova geração do Honda Civic em sua versão híbrida. Vale a pena?", "Análise detalhada do desempenho, consumo e tecnologia do Civic...", "/images/civichibrido.webp", "02/01/2024", "Auto Esporte", "Avaliação"),
                new Noticia("Comparativo: Honda Civic vs. Toyota Corolla", "Os dois sedãs mais famosos do mundo se enfrentam em mais um duelo.", "Descubra qual deles oferece o melhor pacote de tecnologia, desempenho e conforto.", "/images/civic.jpg", "03/01/2024", "Quatro Rodas", "Comparativos"),
                new Noticia("Salão do Automóvel de São Paulo: As Grandes Novidades", "Confira os lançamentos e conceitos que marcaram o maior evento automotivo da América Latina.", "Cobertura completa do que rolou no São Paulo Expo.", "/images/salaosaopaulo.webp", "04/01/2024", "Motor1", "Exposição")
        );
        noticiaRepository.saveAll(noticias);
        System.out.println("Notícias inseridas com sucesso.");
    }

    private void criarUsuarioAdmin() {
        System.out.println("Criando usuário ADMIN padrão...");
        Usuario admin = new Usuario();
        admin.setNome("Juan Admin");
        admin.setEmail("juanadm@gmail.com");
        admin.setSenha(passwordEncoder.encode("1234")); // A senha será "1234"
        admin.setRole("ROLE_ADMIN"); // Define o papel como administrador
        admin.setPerguntaSeguranca("Qual é o seu nome de usuário?");
        admin.setRespostaSeguranca(passwordEncoder.encode(admin.getNome()));
        usuarioRepository.save(admin);
        System.out.println("Usuário ADMIN criado com sucesso (email: juanadm@gmail.com, senha: 1234)");
    }
}