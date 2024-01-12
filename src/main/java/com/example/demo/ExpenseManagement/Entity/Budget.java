package com.example.demo.ExpenseManagement.Entity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Budget {
    
    @Id
    @Column(name = "budget_id")
    private Long budgetId;

    @Column(name = "budget_amount")
    private BigDecimal budgetAmount;

    @Column(name = "budget_type")
    private String budgetType;

    @Column(name = "created_date")
    private ZonedDateTime createdTime;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedTime;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    public Budget() {}

    public Budget(Long budgetId, BigDecimal budgetAmount, String budgetType, ZonedDateTime createdTime,
            ZonedDateTime modifiedTime, boolean isActive, Category category) {
        this.budgetId = budgetId;
        this.budgetAmount = budgetAmount;
        this.budgetType = budgetType;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.isActive = isActive;
        this.category = category;
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

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
}

    // budget_id bigint unsigned not null auto_increment primary key,
    // budget_amount decimal(10,2) not null default 0.0,
    // budget_type varchar(30) not null,
    // created_date timestamp not null default current_timestamp,
    // modified_date timestamp not null default current_timestamp on update current_timestamp,
    // is_active boolean not null default true,
    // category_id bigint unsigned not null,
    // foreign key (category_id) references Category(category_id)