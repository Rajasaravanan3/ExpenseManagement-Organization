package com.example.demo.ExpenseManagement.DTO;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.example.demo.ExpenseManagement.Entity.ApprovalStatus;

public class ExpenseDTO {
    
    private Long expenseId;

    private BigDecimal amount;

    private ZonedDateTime expenseDate;

    private String expenseDescription;

    private ApprovalStatus approvalStatus;

    private Long categoryId;

    private Integer currencyId;

    private Integer paymentMethodId;

    private Long userId;

    public ExpenseDTO() {}

    public ExpenseDTO(Long expenseId, BigDecimal amount, ZonedDateTime expenseDate, String expenseDescription,
        ApprovalStatus approvalStatus, Long categoryId, Integer currencyId, Integer paymentMethodId, Long userId) {
        this.expenseId = expenseId;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.expenseDescription = expenseDescription;
        this.approvalStatus = approvalStatus;
        this.categoryId = categoryId;
        this.currencyId = currencyId;
        this.paymentMethodId = paymentMethodId;
        this.userId = userId;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ZonedDateTime getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(ZonedDateTime expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
}
