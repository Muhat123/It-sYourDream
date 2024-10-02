package com.maju_mundur.MajuMundur.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file, String id);

    byte[] loadImage(String fileName);
}
