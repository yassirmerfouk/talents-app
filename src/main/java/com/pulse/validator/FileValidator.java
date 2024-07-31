package com.pulse.validator;

import com.pulse.exception.custom.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileValidator {

    private final List<String> extensions = List.of("jpg", "jpeg", "png");

    public void validateImage(MultipartFile image){
        if(image == null || image.isEmpty())
            throw new CustomException("Image file is required.");
        if(image.getOriginalFilename() == null || image.getOriginalFilename().isEmpty() || image.getOriginalFilename().contains(".."))
            throw new CustomException("Image name is not valid.");
        if(!extensions.contains(getExtension(image)))
            throw new CustomException("Image is not valid.");
    }

    private String getExtension(MultipartFile image){
        String originalFileName = image.getOriginalFilename();
        int lastDotIndex = originalFileName.lastIndexOf(".");
        return originalFileName.substring(lastDotIndex+1).toLowerCase();
    }
}
