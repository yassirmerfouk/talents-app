package com.pulse.model;

import com.pulse.enumeration.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING, length = 10)
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @SuperBuilder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(unique = true)
    protected String email;
    protected String password;

    protected String firstName;
    protected String lastName;
    protected String phone;
    protected String image;

    @Enumerated(EnumType.STRING)
    protected VerificationStatus status;

    protected boolean enabled;
    protected boolean locked;

    protected LocalDateTime createdAt = LocalDateTime.now();


    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    public String getFullName(){
        return lastName + " " + firstName;
    }

    public void copyProperties(User user){
        email = user.email;
        firstName = user.firstName;
        lastName = user.lastName;
        phone = user.phone;
    }

    public boolean hasAuthority(String authority){
        long counter = 0L;
        counter = roles.stream().filter(role -> role.getName().equals(authority)).count();
        return counter > 0;
    }
}
