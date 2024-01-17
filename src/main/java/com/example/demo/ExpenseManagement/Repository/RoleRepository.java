package com.example.demo.ExpenseManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.ExpenseManagement.Entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
    @Query("Select r from Role r where r.roleId = :roleId")
    Role findRoleById(@Param("roleId") Long roleId);
}
