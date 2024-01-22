package com.example.demo.ExpenseManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.math.BigDecimal;

import com.example.demo.ExpenseManagement.Entity.Expense;
import com.example.demo.ExpenseManagement.Entity.User;

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
    @Query("Select e from Expense e inner join e.category c where c.categoryName = :categoryName")
    List<Expense> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("Select e from Expense e inner join e.currency c where c.currencyCode = :currencyCode")
    List<Expense> findByCurrencyCode(@Param("currencyCode") String currencyCode);

    @Query("Select e from Expense e inner join e.paymentMethod p where p.paymentMethodName = :paymentMethodName")
    List<Expense> findByPaymentMethodName(@Param("paymentMethodName") String paymentMethodName);
    
    @Query("Select e from Expense e order by e.amount desc")
    List<Expense> findByAmount();


    // admin accesss -> first check if user is an admin
    //list all users
    @Query("Select e from Expense e inner join e.user u where u.userId = :userId")
    List<Expense> findAllExpensesByUser(@Param("userId") Long userId);
    //can modify approver, category, budget -> do it in service


    //approver's access -> check if user is approver
    //list all expenses by recent date
    @Query("Select e from Expense e order by e.expenseDate desc")
    List<Expense> findByDate();
    //filter list by approval status
    @Query("Select e from Expense e where e.approvalStatus = :approvalStatus")
    List<Expense> getExpensesByStatus(@Param("approvalStatus") String approvalStatus);
    //only approver can modify expense approval status
    @Query("Update Expense e set e.approvalStatus = :approvalStatus")
    void updateApprovalStatus(@Param("approvalStatus") String approvalStatus);
}
