package com.pulse.service.user;

import com.pulse.enumeration.VerificationStatus;
import com.pulse.exception.custom.CustomException;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.model.User;
import com.pulse.repository.UserRepository;
import com.pulse.service.authentication.AuthenticationService;
import com.pulse.service.notification.NotificationService;
import com.pulse.service.storage.FileStorageService;
import com.pulse.validator.FileValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final FileValidator fileValidator;
    private final FileStorageService fileStorageService;

    private final NotificationService notificationService;

    @Override
    public void askForVerification(){
        User user = authenticationService.getAuthenticatedUser();
        if(user.getStatus() != VerificationStatus.NOT_VERIFIED)
            throw new CustomException("You can't ask for a verification.");
        user.setStatus(VerificationStatus.WAITING);
        userRepository.save(user);
        notificationService.sendVerificationRequestToAdmins(user);
    }

    @Override
    public void verifyUser(Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User %d not found.", id))
        );
        user.setStatus(VerificationStatus.VERIFIED);
        notificationService.sendVerificationNotificationToUser(user);
        userRepository.save(user);
    }

    @Override
    public void banUser(Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User %d not found.", id))
        );
        if(!user.isLocked()) user.setStatus(VerificationStatus.BANNED);
        else user.setStatus(VerificationStatus.NOT_VERIFIED);
        user.setLocked(!user.isLocked());
        userRepository.save(user);
    }


    @Override
    public String updateImage(MultipartFile image){
        fileValidator.validateImage(image);
        String imageName = fileStorageService.saveFile(image);
        User user = authenticationService.getAuthenticatedUser();
        user.setImage(imageName);
        userRepository.save(user);
        return imageName;
    }

    @Override
    public Resource getImage(String imageName){
        if(!userRepository.existsByImage(imageName))
            throw new CustomException("Image not found.");
        return fileStorageService.loadFile(imageName);
    }


}
