package com.oop.ticketmasterswiftiebackend.user.service.impl;

import com.oop.ticketmasterswiftiebackend.user.models.entities.RoleEntity;
import com.oop.ticketmasterswiftiebackend.user.repository.RoleRepository;
import com.oop.ticketmasterswiftiebackend.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleEntity getRoleById(Integer roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }
}
