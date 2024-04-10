package com.oop.ticketmasterswiftiebackend.user.repository;

import com.oop.ticketmasterswiftiebackend.user.models.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
}
