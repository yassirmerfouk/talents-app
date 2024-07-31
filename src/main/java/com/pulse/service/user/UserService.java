package com.pulse.service.user;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void verifyUser(Long id);

    void askForVerification();

    void banUser(Long id);

    String updateImage(MultipartFile image);

    Resource getImage(String imageName);
}
