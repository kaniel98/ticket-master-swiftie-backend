package com.oop.ticketmasterswiftiebackend.user.repository;

import com.oop.ticketmasterswiftiebackend.user.constants.AccountStatus;
import com.oop.ticketmasterswiftiebackend.user.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUserIdAndStatus(int userId, AccountStatus status);

    Optional<UserEntity> findByUserIdAndStatusAndRoleId(int userId, AccountStatus status, Integer roleId);

}
