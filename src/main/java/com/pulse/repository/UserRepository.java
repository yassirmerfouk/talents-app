package com.pulse.repository;

import com.pulse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByImage(String image);

    @Query(
            "select case when count(user)>0 then true else false end from User user where user.email = :email and user.id != :id"
    )
    boolean checkEmailAvailability(Long id, String email);
}
