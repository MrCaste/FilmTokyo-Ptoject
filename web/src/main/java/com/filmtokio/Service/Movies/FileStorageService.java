package com.filmtokio.Service.Movies;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path uploadPath = Paths.get("uploads");

    public String savePoster(MultipartFile file) throws IOException {

        Files.createDirectories(uploadPath);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

}
