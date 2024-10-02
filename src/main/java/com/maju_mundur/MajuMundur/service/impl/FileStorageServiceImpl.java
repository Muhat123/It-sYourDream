package com.maju_mundur.MajuMundur.service.impl;

import com.maju_mundur.MajuMundur.Config.FileStorageProperties;
import com.maju_mundur.MajuMundur.exception.OurException;
import com.maju_mundur.MajuMundur.service.FileStorageService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path storageLocation;

    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.storageLocation = Paths.get(fileStorageProperties.getUpLoadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(storageLocation);
        } catch (
                IOException e) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }


    @Override
    public String storeFile(MultipartFile file, String id){
        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        try{
            if (fileName.contains("..")){
                throw new OurException("Invalid Path sequence" + fileName);
            }
            Path targetLocation = storageLocation.resolve(uniqueFileName);
            Files.write(targetLocation, file.getBytes());
            return uniqueFileName;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public byte[] loadImage(String fileName){
        try{
            Path filePath = storageLocation.resolve(fileName).normalize();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(filePath.toFile())
                    .size(1000,1000)
                    .keepAspectRatio(true)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);
            return outputStream.toByteArray();
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
