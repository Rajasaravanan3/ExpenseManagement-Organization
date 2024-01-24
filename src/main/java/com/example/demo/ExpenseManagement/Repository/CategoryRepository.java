package com.example.demo.ExpenseManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.ExpenseManagement.Entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    @Query("Select c from Category c where c.categoryId = :categoryId")
    Category findCategoryById(@Param("categoryId") Long categoryId);

    @Query("Select c from Category c where c.isActive = TRUE")
    List<Category> findAllCategories();
}
