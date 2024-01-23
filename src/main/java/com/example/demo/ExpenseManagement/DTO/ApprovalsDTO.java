package com.example.demo.ExpenseManagement.DTO;

import java.time.ZonedDateTime;

public class ApprovalsDTO {
    
    private Long approvalId;

    private ZonedDateTime approvedDate;

    private Long userId;

    private Long expenseId;

    public ApprovalsDTO() {}

    public ApprovalsDTO(Long approvalId, ZonedDateTime approvedDate, Long userId, Long expenseId) {
        this.approvalId = approvalId;
        this.approvedDate = approvedDate;
        this.userId = userId;
        this.expenseId = expenseId;
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    public ZonedDateTime getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(ZonedDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }
    
}
