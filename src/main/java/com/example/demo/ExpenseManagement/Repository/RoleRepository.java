package com.example.demo.ExpenseManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.ExpenseManagement.Entity.Role;
import com.example.demo.ExpenseManagement.Entity.User;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("Select r from Role r where r.roleId = :roleId")
    Role findRoleById(@Param("roleId") Long roleId);

    @Query("Select u from User u inner join u.roles r inner join u.organization o where r.roleName = :roleName and o.organizationId = :organizationId")
    List<User> findUsersByRoleName(@Param("organizationId") Long organizationId, @Param("roleName") String roleName);
}
