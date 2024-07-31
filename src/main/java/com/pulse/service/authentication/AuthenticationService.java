package com.pulse.service.authentication;

import com.pulse.dto.authentication.AuthenticationRequest;
import com.pulse.dto.authentication.AuthenticationResponse;
import com.pulse.dto.registration.ClientRegistrationRequest;
import com.pulse.dto.registration.TalentRegistrationRequest;
import com.pulse.dto.reset.ResetPasswordRequest;
import com.pulse.model.User;
import com.pulse.security.SecurityUser;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    boolean isAuthenticated();

    User getAuthenticatedUser();

    SecurityUser getAuthenticatedSecurityUser();

    Long getAuthenticatedUserId();

    boolean hasAuthority(String authority);

    void checkVerification();

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    Long registerClient(ClientRegistrationRequest request) throws MessagingException;

    Long registerTalent(TalentRegistrationRequest talentRegistrationRequest) throws MessagingException;

    void confirmAccount(String token);

    void resetPassword(String email) throws MessagingException;

    void confirmResetPassword(ResetPasswordRequest resetPasswordRequest);

    Integer getProfileCompletion();
}
