package com.example.demo.ExpenseManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import com.example.demo.ExpenseManagement.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
    @Query("Select u from User u where u.userId = :userId")
    User findUserById(@Param("userId") Long userId);

    @Query("Select u from User u where u.username = :username")
    User findByUsername(@Param("username") String username);

    @Query("Select u from User u inner join u.organization o where o.organizationId = :organizationId and u.isActive = TRUE and o.isActive = TRUE")
    List<User> findAllUsers(@Param("organizationId") Long organizationId);
}
