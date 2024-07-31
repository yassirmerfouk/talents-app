package com.pulse.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "admins")
@DiscriminatorValue(value = "ADMIN")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @SuperBuilder
public class Admin extends User{

    private String position;

    public void copyProperties(Admin admin){
        super.copyProperties(admin);
    }
}
