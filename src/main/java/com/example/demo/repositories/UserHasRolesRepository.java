package com.example.demo.repositories;

import com.example.demo.models.UserHasRoles;
import com.example.demo.models.id.UserRoleId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHasRolesRepository extends JpaRepository<UserHasRoles,UserRoleId> {
}
 