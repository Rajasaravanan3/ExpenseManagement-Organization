package com.example.demo.ExpenseManagement.Entity;

import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Category {
    
    @Id
    @Column(name = "category_id")
    private long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_description")
    private String categoryDescription;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "organization_id")
    private Organization organization;

    @OneToMany
    @JoinColumn(name = "budget_id", referencedColumnName = "budget_id")
    private Budget budgets;

    public Category() {}

    public Category(long categoryId, String categoryName, String categoryDescription, ZonedDateTime createdDate,
            ZonedDateTime modifiedDate, boolean isActive, Organization organization, Budget budgets) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.isActive = isActive;
        this.organization = organization;
        this.budgets = budgets;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Budget getBudgets() {
        return budgets;
    }

    public void setBudgets(Budget budgets) {
        this.budgets = budgets;
    }

    
    // public List<Budget> getBudgets() {
    //     return budgets;
    // }

    // public void setBudgets(List<Budget> budgets) {
    //     this.budgets = budgets;
    // }
    
}

    // category_id bigint unsigned not null auto_increment primary key,
    // category_name varchar(40) not null,
    // category_description varchar(300),
    // created_date timestamp not null default current_timestamp,
    // modified_date timestamp not null default current_timestamp on update current_timestamp,
    // is_active boolean not null default true,
    // organization_id bigint unsigned not null,
    // budget_id bigint unsigned not null,
    // foreign key (budget_id) references budget(budget_id),
    // foreign key (organization_id) references organization(organization_id)
