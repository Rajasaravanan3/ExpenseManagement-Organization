package com.example.demo.ExpenseManagement.DTO;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.example.demo.ExpenseManagement.Entity.BudgetType;

public class BudgetDTO {
    
    private Long budgetId;

    private BigDecimal budgetAmount;

    private BudgetType budgetType;

    private ZonedDateTime createdTime;

    private ZonedDateTime modifiedTime;

    private Boolean isActive;

    private Long categoryId;

    public BudgetDTO() {
    }

    public BudgetDTO(Long budgetId, BigDecimal budgetAmount, BudgetType budgetType, ZonedDateTime createdTime,
            ZonedDateTime modifiedTime, Boolean isActive, Long categoryId) {
        this.budgetId = budgetId;
        this.budgetAmount = budgetAmount;
        this.budgetType = budgetType;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.isActive = isActive;
        this.categoryId = categoryId;
    }

    public Long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public BudgetType getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(BudgetType budgetType) {
        this.budgetType = budgetType;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(ZonedDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
}
