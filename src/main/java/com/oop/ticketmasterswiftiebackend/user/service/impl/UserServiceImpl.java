package com.oop.ticketmasterswiftiebackend.user.service.impl;

import com.oop.ticketmasterswiftiebackend.common.CommonUtils;
import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import com.oop.ticketmasterswiftiebackend.user.constants.AccountStatus;
import com.oop.ticketmasterswiftiebackend.user.constants.UserError;
import com.oop.ticketmasterswiftiebackend.user.models.entities.UserEntity;
import com.oop.ticketmasterswiftiebackend.user.models.request.CreateUserRequest;
import com.oop.ticketmasterswiftiebackend.user.models.request.UpdateUserRequest;
import com.oop.ticketmasterswiftiebackend.user.models.response.UserResponse;
import com.oop.ticketmasterswiftiebackend.user.repository.UserRepository;
import com.oop.ticketmasterswiftiebackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper = new ModelMapper();


    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        UserEntity newUser = UserEntity.builder()
                .email(createUserRequest.getEmail())
                .username(createUserRequest.getUsername())
                .phoneNumber(createUserRequest.getPhoneNumber())
                .roleId(createUserRequest.getRoleId())
                .status(AccountStatus.ACTIVE)
                .build();

        try {
            return mapper.map(userRepository.save(newUser), UserResponse.class);
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate data detected: {}", e.getMessage());
            throw new BaseException(UserError.USER_ALREADY_EXISTS.getCode(),
                    UserError.USER_ALREADY_EXISTS.getBusinessCode(), UserError.USER_ALREADY_EXISTS.getDescription());
        } catch (Exception e) {
            log.error("Error occurred while updating user: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new BaseException(UserError.USER_NOT_FOUND.getCode(), UserError.USER_NOT_FOUND.getBusinessCode(),
                        UserError.USER_NOT_FOUND.getDescription())
        );
        return mapper.map(user, UserResponse.class);
    }

    // Helper function to check if user exist
    @Override
    public UserEntity getUserById(int userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new BaseException(UserError.USER_NOT_FOUND.getCode(), UserError.USER_NOT_FOUND.getBusinessCode(),
                        UserError.USER_NOT_FOUND.getDescription())
        );
    }

    @Override
    public UserEntity getUseByIdAndStatusAndRoleId(int userId, AccountStatus status, Integer roleId) {
        return userRepository.findByUserIdAndStatusAndRoleId(userId, status, roleId).orElseThrow(
                () -> new BaseException(UserError.USER_NOT_FOUND.getCode(), UserError.USER_NOT_FOUND.getBusinessCode(),
                        UserError.USER_NOT_FOUND.getDescription()));
    }

    @Override
    public UserEntity getUserByIdAndStatus(int userId, AccountStatus status) {
        return userRepository.findByUserIdAndStatus(userId, status).orElseThrow(
                () -> new BaseException(UserError.USER_NOT_FOUND.getCode(), UserError.USER_NOT_FOUND.getBusinessCode(),
                        UserError.USER_NOT_FOUND.getDescription()));
    }
    
    @Override
    public UserResponse updateUserDetails(UpdateUserRequest request) {
        UserResponse userResult = getUserByEmail(request.getEmail());
        UserEntity user = getUserById(userResult.getUserId());
        // Update user details
        user.setUsername(request.getUsername());
        user.setPhoneNumber(request.getPhoneNumber());
        try {
            return mapper.map(userRepository.save(user), UserResponse.class);
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate data detected: {}", e.getMessage());
            throw new BaseException(UserError.USER_ALREADY_EXISTS.getCode(),
                    UserError.USER_ALREADY_EXISTS.getBusinessCode(), UserError.USER_ALREADY_EXISTS.getDescription());
        } catch (Exception e) {
            log.error("Error occurred while updating user: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }
}
