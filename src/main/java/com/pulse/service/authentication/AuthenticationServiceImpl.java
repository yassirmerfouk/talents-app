package com.pulse.service.authentication;

import com.pulse.amqp.producer.ActivationProducer;
import com.pulse.amqp.producer.ResetPasswordProducer;
import com.pulse.dto.authentication.AuthenticationRequest;
import com.pulse.dto.authentication.AuthenticationResponse;
import com.pulse.dto.registration.ClientRegistrationRequest;
import com.pulse.dto.registration.TalentRegistrationRequest;
import com.pulse.dto.reset.ResetPasswordRequest;
import com.pulse.enumeration.ClientType;
import com.pulse.enumeration.EmailTemplateName;
import com.pulse.enumeration.VerificationStatus;
import com.pulse.exception.custom.CustomException;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.exception.custom.ForbiddenException;
import com.pulse.mapper.UserMapper;
import com.pulse.model.*;
import com.pulse.repository.RoleRepository;
import com.pulse.repository.UserRepository;
import com.pulse.security.SecurityUser;
import com.pulse.security.service.JWTService;
import com.pulse.service.confirmation.ConfirmationService;
import com.pulse.service.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationService confirmationService;
    private final EmailService emailService;
    private final RoleRepository roleRepository;

    private final ActivationProducer activationProducer;
    private final ResetPasswordProducer resetPasswordProducer;

    @Value("${frontend.confirmation-url}")
    private String confirmationUrl;
    @Value("${frontend.reset-password-url}")
    private String passwordResetUrl;

    @Override
    public boolean isAuthenticated(){
        Authentication authentication = (Authentication) SecurityContextHolder
                .getContext().getAuthentication();
        return authentication.getPrincipal() != "anonymousUser";

    }

    @Override
    public User getAuthenticatedUser(){
        Authentication authentication = (Authentication) SecurityContextHolder
                .getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        return securityUser.getUser();
    }

    @Override
    public SecurityUser getAuthenticatedSecurityUser(){
        Authentication authentication = (Authentication) SecurityContextHolder
                .getContext().getAuthentication();
        return (SecurityUser) authentication.getPrincipal();
    }

    @Override
    public Long getAuthenticatedUserId(){
        return getAuthenticatedUser().getId();
    }

    @Override
    public boolean hasAuthority(String authority){
        long count = getAuthenticatedSecurityUser().getAuthorities().stream().filter(
                aut -> aut.getAuthority().equals(authority)
        ).count();
        return count > 0;
    }

    @Override
    public void checkVerification(){
        if(getAuthenticatedUser().getStatus() != VerificationStatus.VERIFIED)
            throw new ForbiddenException("You're not verified.");
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateAccessToken(userDetails))
                .refreshToken(jwtService.generateRefreshToken(userDetails))
                .build();
    }

    @Override
    public Long registerClient(ClientRegistrationRequest clientRegistrationRequest) throws MessagingException {
        if (userRepository.existsByEmail(clientRegistrationRequest.getEmail()))
            throw new CustomException("Email already used by another Client.");
        Role role = roleRepository.findByName("CLIENT").orElseThrow(
                () -> new EntityNotFoundException("Role not found.")
        );
        Client client = userMapper.mapFromRegistrationToClient(clientRegistrationRequest);
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        client.setRoles(List.of(role));
        userRepository.save(client);
        ConfirmationToken confirmationToken = confirmationService.generateConfirmationToken(client);
        emailService.sendEmail(
                client.getEmail(),
                EmailTemplateName.CONFIRM_ACCOUNT,
                "Account confirmation",
                Map.of(
                        "fullName", client.getFullName(),
                        "confirmationCode", confirmationToken.getToken(),
                        "confirmationUrl", confirmationUrl
                )
        );
/*        activationProducer.sendActivation(
                Activation.builder()
                        .email(client.getEmail())
                        .fullName(client.getFullName())
                        .confirmationCode(confirmationToken.getToken())
                        .build()
        );*/
        return client.getId();
    }

    @Override
    public Long registerTalent(TalentRegistrationRequest talentRegistrationRequest) throws MessagingException {
        if (userRepository.existsByEmail(talentRegistrationRequest.getEmail()))
            throw new CustomException("Email already used by another Talent.");
        Role role = roleRepository.findByName("TALENT").orElseThrow(
                () -> new EntityNotFoundException("Role not found.")
        );
        Talent talent = userMapper.mapFromRegistrationToTalent(talentRegistrationRequest);
        talent.setPassword(passwordEncoder.encode(talent.getPassword()));
        talent.setRoles(List.of(role));
        userRepository.save(talent);
        ConfirmationToken confirmationToken = confirmationService.generateConfirmationToken(talent);
        emailService.sendEmail(
                talent.getEmail(),
                EmailTemplateName.CONFIRM_ACCOUNT,
                "Account confirmation",
                Map.of(
                        "fullName", talent.getFullName(),
                        "confirmationCode", confirmationToken.getToken(),
                        "confirmationUrl", confirmationUrl
                )
        );
        /*activationProducer.sendActivation(
                Activation.builder()
                        .email(talent.getEmail())
                        .fullName(talent.getFullName())
                        .confirmationCode(confirmationToken.getToken())
                        .build()
        );*/
        return talent.getId();
    }

    @Override
    public void confirmAccount(String token) {
        ConfirmationToken confirmationToken =
                confirmationService.validateConfirmationToken(token);
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        User user = confirmationToken.getUser();
        user.setEnabled(true);
        confirmationService.updateConfirmationToken(confirmationToken);
        userRepository.save(user);
    }

    @Override
    public void resetPassword(String email) throws MessagingException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Email not found.")
        );
        if (!user.isEnabled())
            throw new CustomException("Account disabled, can't reset password.");
        PasswordToken passwordToken = confirmationService.generatePasswordToken(user);
        emailService.sendEmail(
                user.getEmail(),
                EmailTemplateName.RESET_PASSWORD,
                "Password reset",
                Map.of(
                        "fullName", user.getFullName(),
                        "resetPasswordUrl", passwordResetUrl + passwordToken.getToken() + "?email=" + user.getEmail()
                )
        );
        /*resetPasswordProducer.sendResetPassword(
                ResetPassword.builder()
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .token(passwordToken.getToken())
                        .build()
        );*/
    }

    @Override
    public void confirmResetPassword(ResetPasswordRequest resetPasswordRequest){
        if(!resetPasswordRequest.getPassword().equals(resetPasswordRequest.getRePassword()))
            throw new CustomException("Passwords passed are not matches.");
        PasswordToken passwordToken = confirmationService.validatePasswordToken(
                resetPasswordRequest.getToken()
        );
        User user = passwordToken.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        passwordToken.setConfirmedAt(LocalDateTime.now());
        confirmationService.updatePasswordToken(passwordToken);
        userRepository.save(user);
    }

    @Override
    public Integer getProfileCompletion(){
        int completion = 0;
        User user = userRepository.findById(getAuthenticatedUserId()).get();
        if(hasAuthority("TALENT")){
            Talent talent = (Talent) user;
            if(talent.getFirstName() != null && !talent.getFirstName().isEmpty())
                completion += 5;
            if(talent.getLastName() != null && !talent.getLastName().isEmpty())
                completion += 5;
            if(talent.getEmail() != null && !talent.getEmail().isEmpty())
                completion += 5;
            if(talent.getPhone() != null && !talent.getPhone().isEmpty())
                completion += 5;
            if(talent.getTitle() != null && !talent.getTitle().isEmpty())
                completion += 5;
            if(talent.getSummary() != null && !talent.getSummary().isEmpty())
                completion += 5;
            if(talent.getDateOfBirth() != null)
                completion += 5;
            if(talent.getCity() != null && !talent.getCity().isEmpty())
                completion += 5;
            if(talent.getAddress() != null && !talent.getAddress().isEmpty())
                completion += 5;
            if(talent.getImage() != null && !talent.getImage().isEmpty())
                completion += 10;
            if(!talent.getExperiences().isEmpty())
                completion += 20;
            if(!talent.getEducations().isEmpty())
                completion += 20;
            if(!talent.getSkills().isEmpty())
                completion += 5;
        }
        if(hasAuthority("CLIENT")){
            Client client = (Client) user;
            if(client.getFirstName() != null && !client.getFirstName().isEmpty())
                completion += 5;
            if(client.getLastName() != null && !client.getLastName().isEmpty())
                completion += 5;
            if(client.getEmail() != null && !client.getEmail().isEmpty())
                completion += 5;
            if(client.getPhone() != null && !client.getPhone().isEmpty())
                completion += 5;
            if(client.getCountry() != null && !client.getCountry().isEmpty())
                completion += 5;
            if(client.getCity() != null && !client.getCity().isEmpty())
                completion += 5;
            if(client.getSector() != null && !client.getSector().isEmpty())
                completion += 5;
            if(client.getShortDescription() != null && !client.getShortDescription().isEmpty())
                completion += 5;
            if(client.getCompanyName() != null && !client.getCompanyName().isEmpty())
                completion += 5;
            if(client.getWebsite() != null && !client.getWebsite().isEmpty())
                completion += 5;
            if(client.getSize() != null && !client.getSize().isEmpty())
                completion += 5;
            if(client.getImage() != null && !client.getImage().isEmpty())
                completion += 10;
            if(client.getType() == ClientType.COMPANY)
                completion += 35;
            if(client.getType() == ClientType.PERSONNEL)
                completion += 50;
        }
        return completion;
    }
}
