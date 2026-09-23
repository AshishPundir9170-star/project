package com.sih26132.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path storageLocation;

    public LocalFileStorageService(
            @Value("${app.file-storage.location:uploads}") String storagePath)
            throws IOException {

        this.storageLocation = Paths.get(storagePath)
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(this.storageLocation);
    }

    @Override
    public String store(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();

        String extension = "";

        if (originalFilename != null &&
                originalFilename.contains(".")) {

            extension = originalFilename.substring(
                    originalFilename.lastIndexOf(".")
            );
        }

        String objectKey =
                UUID.randomUUID() + extension;

        Path targetLocation =
                storageLocation.resolve(objectKey)
                        .normalize();

        if (!targetLocation.startsWith(storageLocation)) {
            throw new IOException("Invalid file path");
        }

        Files.copy(
                file.getInputStream(),
                targetLocation,
                StandardCopyOption.REPLACE_EXISTING
        );

        return objectKey;
    }

    @Override
    public void delete(String objectKey) throws IOException {

        Path targetLocation =
                storageLocation.resolve(objectKey)
                        .normalize();

        if (!targetLocation.startsWith(storageLocation)) {
            throw new IOException("Invalid file path");
        }

        Files.deleteIfExists(targetLocation);
    }
}