package com.example.demo.ExpenseManagement.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.ExpenseManagement.Entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    
    @Query("Select b from Budget b where b.budgetId  = :budgetId")
    Budget findBudgetById(@Param("budgetId") Long budgetId);

    @Query("Select b from Budget b inner join b.category c where c.categoryId = :categoryId")
    List<Budget> getBudgetsByCategory(@Param("categoryId") Long categoryId);
}
