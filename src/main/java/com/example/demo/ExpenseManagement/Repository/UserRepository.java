package com.example.demo.ExpenseManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.ExpenseManagement.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
    @Query("Select u from User u where u.userId = :userId")
    User findUserById(@Param("userId") Long userId);
}
