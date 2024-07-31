package com.pulse.service.confirmation;

import com.pulse.exception.custom.CustomException;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.model.ConfirmationToken;
import com.pulse.model.PasswordToken;
import com.pulse.model.User;
import com.pulse.repository.ConfirmationTokenRepository;
import com.pulse.repository.PasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordTokenRepository passwordTokenRepository;

    @Override
    public ConfirmationToken generateConfirmationToken(User user){
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(generateSecureCode())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        return confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public PasswordToken generatePasswordToken(User user){
        PasswordToken passwordToken = PasswordToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        return passwordTokenRepository.save(passwordToken);
    }

    private ConfirmationToken getConfirmationToken(String token){
        return confirmationTokenRepository
                .findByToken(token).orElseThrow(
                        () -> new EntityNotFoundException("Token passed not found.")
                );
    }

    private PasswordToken getPasswordToken(String token){
        return passwordTokenRepository.findByToken(token).orElseThrow(
                () -> new EntityNotFoundException("Token passed not found.")
        );
    }

    @Override
    public ConfirmationToken updateConfirmationToken(ConfirmationToken confirmationToken){
        return confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public PasswordToken updatePasswordToken(PasswordToken passwordToken){
        return passwordTokenRepository.save(passwordToken);
    }

    @Override
    public ConfirmationToken validateConfirmationToken(String token){
        ConfirmationToken confirmationToken = getConfirmationToken(token);
        if(confirmationToken.getConfirmedAt() != null)
            throw new CustomException("Token passed already confirmed.");
        if(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new CustomException("Token passed already expired.");
        return confirmationToken;
    }

    @Override
    public PasswordToken validatePasswordToken(String token){
        PasswordToken passwordToken = getPasswordToken(token);
        if(passwordToken.getConfirmedAt() != null)
            throw new CustomException("Token passed already confirmed.");
        if(passwordToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new CustomException("Token passed already expired.");
        return passwordToken;
    }

    private String generateCode(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(900000) + 100000;
        return String.valueOf(randomNumber);
    }

    private String generateSecureCode(){
        boolean exists = true;
        String code = null;
        while(exists){
            code = generateCode();
            if(!confirmationTokenRepository.existsByToken(code))
                exists = false;
        }
        return code;
    }
}
