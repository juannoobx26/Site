package com.carro.SobreRodas.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService() {
        // Define o caminho da pasta onde as imagens serão salvas
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

        try {
            // Cria a pasta 'uploads' se ela não existir
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o diretório para armazenar os arquivos.", ex);
        }
    }

    /**
     * Salva o arquivo no sistema de arquivos.
     * @param file O arquivo enviado no formulário.
     * @return O nome do arquivo gerado (para ser salvo no banco de dados).
     */
    public String storeFile(MultipartFile file) {
        // Pega o nome original do arquivo
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Validações básicas do arquivo
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Desculpe! O nome do arquivo contém uma sequência de caminho inválida " + originalFileName);
            }

            // Gera um nome de arquivo único para evitar conflitos
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // Copia o arquivo para a pasta de destino
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return newFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Não foi possível armazenar o arquivo " + originalFileName + ". Por favor, tente novamente!", ex);
        }
    }
}