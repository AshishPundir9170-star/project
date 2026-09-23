package com.sih26132.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {

    String store(MultipartFile file) throws IOException;

    void delete(String objectKey) throws IOException;
}