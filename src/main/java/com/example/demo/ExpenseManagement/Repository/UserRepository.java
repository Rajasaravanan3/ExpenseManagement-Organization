package com.example.demo.ExpenseManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import com.example.demo.ExpenseManagement.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
    @Query("Select u from User u where u.userId = :userId")
    User findUserById(@Param("userId") Long userId);

    @Query("Select u from User u inner join u.organization o inner join u.role r where o.organizationId = :organizationId and r.roleName = :roleName")
    List<User> getusersByRoleName(@Param("organizationId") Long organizationId, @Param("roleName") String roleName);

    @Query("Select u from User u where u.userEmail = :userEmail")
    User findByUserEmail(@Param("userEmail") String userEmail);

    @Query("Select u from User u inner join u.organization o where o.organizationId = :organizationId")
    List<User> findAllUsers(@Param("organizationId") Long organizationId);
}
