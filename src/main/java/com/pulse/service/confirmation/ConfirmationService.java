package com.pulse.service.confirmation;

import com.pulse.model.ConfirmationToken;
import com.pulse.model.PasswordToken;
import com.pulse.model.User;

public interface ConfirmationService {
    ConfirmationToken generateConfirmationToken(User user);

    PasswordToken generatePasswordToken(User user);

    ConfirmationToken updateConfirmationToken(ConfirmationToken confirmationToken);

    PasswordToken updatePasswordToken(PasswordToken passwordToken);

    ConfirmationToken validateConfirmationToken(String token);

    PasswordToken validatePasswordToken(String token);
}
