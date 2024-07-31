package com.pulse.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileStorageService {

    String saveFile(MultipartFile file);

    Resource loadFile(String fileName);
}
