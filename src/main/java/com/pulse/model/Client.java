package com.pulse.model;

import com.pulse.enumeration.ClientType;
import com.pulse.enumeration.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "clients")
@DiscriminatorValue(value = "CLIENT")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @SuperBuilder
public class Client extends User{

    private String companyName;
    private String sector;
    @Column(length = 500)
    private String shortDescription;
    @Column(length = 5000)
    private String longDescription;
    private String city;
    private String country;
    private String website;
    private String size;

    @Enumerated(EnumType.STRING)
    private ClientType type;

    public void copyProperties(Client client){
        super.copyProperties(client);
        companyName = client.companyName;
        sector = client.sector;
        shortDescription = client.shortDescription;
        longDescription = client.longDescription;
        city = client.city;
        country = client.country;
        website = client.website;
        size = client.size;
        type = client.type;
    }
}
