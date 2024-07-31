package com.pulse.service.storage;

import com.cloudinary.Cloudinary;
import com.pulse.exception.custom.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class CloudinaryStorage implements FileStorageService{

    private Cloudinary cloudinary;

    @Override
    public String saveFile(MultipartFile file) {
        try{
            Map<Object, Object> options = new HashMap<>();
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            String publicId = (String) uploadedFile.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);
        }catch (IOException e){
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public Resource loadFile(String fileName) {
        return null;
    }
}
