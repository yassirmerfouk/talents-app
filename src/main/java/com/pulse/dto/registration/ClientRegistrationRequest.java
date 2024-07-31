package com.pulse.dto.registration;

import com.pulse.enumeration.ClientType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @SuperBuilder
public class ClientRegistrationRequest extends RegistrationRequest{

    private String companyName;

    @NotBlank(message = "Sector is required.")
    private String sector;

    @Length(max = 255, message = "Description max characters is 255.")
    private String shortDescription;

    @NotBlank(message = "City is required.")
    private String city;

    @NotBlank(message = "Country is required.")
    private String country;

    @URL(message = "Website is not valid.")
    private String website;

    private String size;

    @NotNull(message = "Type is required.")
    private ClientType type;
}
