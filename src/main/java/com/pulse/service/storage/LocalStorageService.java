package com.pulse.service.storage;

import com.pulse.exception.custom.CustomException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/*@Service
@RequiredArgsConstructor*/
public class LocalStorageService implements FileStorageService{

    @Value("${uploads.image.path}")
    private String uploadsImagePath;

    @PostConstruct
    public void init(){
        Path booksPath = Paths.get(uploadsImagePath);
        try{
            if(!Files.exists(booksPath))
                Files.createDirectories(booksPath);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String saveFile(MultipartFile file) {
        try{
            Path path = Paths.get(uploadsImagePath);
            if(!Files.exists(path))
                Files.createDirectories(path);
            String newFileName = generateRandomFileName(file);
            Files.copy(file.getInputStream(),path.resolve(newFileName));
            return newFileName;
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private String generateRandomFileName(MultipartFile file){
        return UUID.randomUUID().toString() + "." + getFileExtension(file);
    }

    private String getFileExtension(MultipartFile file){
        String originalFileName = file.getOriginalFilename();
        int lastDotIndex = originalFileName.lastIndexOf(".");
        return originalFileName.substring(lastDotIndex+1).toLowerCase();
    }

    @Override
    public Resource loadFile(String fileName){
        try{
            Path path = Paths.get(uploadsImagePath + "/" + fileName);
            Resource resource = new UrlResource(path.toUri());
            if(resource.exists() && resource.isReadable())
                return resource;
            else
                throw new CustomException("Could not read the file.");
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


}
