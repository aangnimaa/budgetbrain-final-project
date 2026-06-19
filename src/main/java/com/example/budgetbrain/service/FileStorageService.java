package com.example.budgetbrain.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");

    private final Path uploadPath;

    public FileStorageService(@Value("${app.upload-dir}") String uploadDir) throws IOException {
        this.uploadPath = Path.of(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadPath);
    }

    public String save(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "receipt" : file.getOriginalFilename());
        String extension = getExtension(originalName);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IOException("Only image files are allowed: jpg, jpeg, png, gif, webp");
        }

        String storedName = UUID.randomUUID() + "." + extension;
        Path destination = this.uploadPath.resolve(storedName).normalize();

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        }

        return "/uploads/" + storedName;
    }

    private String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1 || index == filename.length() - 1) {
            return "jpg";
        }
        return filename.substring(index + 1);
    }
}
