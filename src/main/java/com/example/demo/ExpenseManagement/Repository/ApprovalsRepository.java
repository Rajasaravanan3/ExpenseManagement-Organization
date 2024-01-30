package com.example.demo.ExpenseManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.ExpenseManagement.Entity.Approvals;
import java.util.List;

public interface ApprovalsRepository extends JpaRepository<Approvals, Long>{
    
    @Query("Select a from Approvals a where a.approvalId = :approvalId")
    Approvals findByApprovalId(@Param("approvalId") Long approvalId);

    @Query("Select a from Approvals a inner join a.approvedBy u where u.userId = :approvedBy")
    List<Approvals> findByApprovedUser(@Param("approvedBy") Long userId);

    @Query("Select a from Approvals a inner join a.expense e where e.expenseId = :expenseId")
    List<Approvals> findByExpenseId(@Param("expenseId") Long expenseId);
}
