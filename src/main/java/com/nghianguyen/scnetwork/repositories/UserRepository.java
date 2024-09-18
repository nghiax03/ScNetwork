package com.nghianguyen.scnetwork.repositories;

import com.nghianguyen.scnetwork.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByFullName(String fullName);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
