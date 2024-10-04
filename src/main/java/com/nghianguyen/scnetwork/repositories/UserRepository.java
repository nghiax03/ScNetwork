package com.nghianguyen.scnetwork.repositories;

import com.nghianguyen.scnetwork.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByFullName(String fullName);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query(
            value = "SELECT u FROM User as u "
            + "WHERE u.id <> :id AND "
            + "(LOWER(u.fullName) LIKE CONCAT('%', :keyWord, '%')) "
    )
    List<User> findAllUsersLike(@Param(value = "id") Long id, @Param(value = "keyWord") String keyWord);

    @Transactional
    @Modifying
    @Query(
           value = "UPDATE User as u "
            + "SET u.isOnline = false "
            + "WHERE u.isOnline = true "
    )
    void setIsOnlineToFalse();
}
