package com.oop.ticketmasterswiftiebackend.user.service;

import com.oop.ticketmasterswiftiebackend.user.constants.AccountStatus;
import com.oop.ticketmasterswiftiebackend.user.models.entities.UserEntity;
import com.oop.ticketmasterswiftiebackend.user.models.request.CreateUserRequest;
import com.oop.ticketmasterswiftiebackend.user.models.request.UpdateUserRequest;
import com.oop.ticketmasterswiftiebackend.user.models.response.UserResponse;

public interface UserService {
    UserResponse createUser(CreateUserRequest createUserRequest);

    UserResponse getUserByEmail(String email);

    UserResponse updateUserDetails(UpdateUserRequest request);

    UserEntity getUserById(int userId);

    UserEntity getUseByIdAndStatusAndRoleId(int userId, AccountStatus status, Integer roleId);

    UserEntity getUserByIdAndStatus(int userId, AccountStatus status);
}
