package com.example.demo.ExpenseManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.math.BigDecimal;

import com.example.demo.ExpenseManagement.Entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    @Query("Select e from Expense e where e.expenseId = :expenseId")
    Expense findExpenseById(@Param("expenseId") Long expenseId);

    @Query("Select e from Expense e inner join e.category c where c.categoryId = :categoryId")
    List<Expense> getExpensesByCategory(@Param("categoryId") Long categoryId);



    //expenses by budgettype
    @Query("Select COALESCE(SUM(e.amount), 0) from Expense e inner join e.category c where c.categoryId = :categoryId and FUNCTION('DATEDIFF', e.expenseDate, CURRENT_DATE) <= 7")
    BigDecimal findSumOfLastSevenDaysExpenses(@Param("categoryId") Long categoryId);
        
    @Query("Select COALESCE(SUM(e.amount), 0) from Expense e inner join e.category c where c.categoryId = :categoryId and FUNCTION('DATEDIFF', e.expenseDate, CURRENT_DATE) <= 30")
    BigDecimal findSumOfLastOneMonthExpenses(@Param("categoryId") Long categoryId);

    @Query("Select COALESCE(SUM(e.amount), 0) from Expense e inner join e.category c where c.categoryId = :categoryId and FUNCTION('DATEDIFF', e.expenseDate, CURRENT_DATE) <= 365")
    BigDecimal findSumOfLastOneYearExpenses(@Param("categoryId") Long categoryId);



    // expense filters
    @Query("Select e from Expense e inner join e.category c inner join e.user u inner join u.organization o where c.categoryName = :categoryName and o.organizationId = :organizationId")
    List<Expense> findByCategoryName(@Param("organizationId") Long organizationId, @Param("categoryName") String categoryName);

    @Query("Select e from Expense e inner join e.currency c inner join e.user u inner join u.organization o where c.currencyCode = :currencyCode and o.organizationId = :organizationId")
    List<Expense> findByCurrencyCode(@Param("organizationId") Long organizationId, @Param("currencyCode") String currencyCode);

    @Query("Select e from Expense e inner join e.paymentMethod p inner join e.user u inner join u.organization o where p.paymentMethodName = :paymentMethodName and o.organizationId = :organizationId")
    List<Expense> findByPaymentMethodName(@Param("organizationId") Long organizationId, @Param("paymentMethodName") String paymentMethodName);
    
    @Query("Select e from Expense e inner join e.user u inner join u.organization o where o.organizationId = :organizationId order by e.amount desc")
    List<Expense> findByAmount(@Param("organizationId") Long organizationId);

    @Query("Select e from Expense e inner join e.user u where u.userId = :userId")
    List<Expense> findExpensesByUserId(@Param("userId") Long userId);



    // admin's accesss

    //getAllusers working for the organization

    //can update approver, category, budget -> in service



    //approver's access
    //list all expenses by recent date
    @Query("Select e from Expense e inner join e.user u inner join u.organization o where o.organizationId = :organizationId order by e.expenseDate desc")
    List<Expense> findByDate(@Param("organizationId") Long organizationId);

    //filter list by approval status
    @Query("Select e from Expense e inner join e.user u inner join u.organization o where o.organizationId = :organizationId and e.approvalStatus = :approvalStatus")
    List<Expense> findExpensesByStatus(@Param("organizationId") Long organizationId, @Param("approvalStatus") String approvalStatus);
    
}
