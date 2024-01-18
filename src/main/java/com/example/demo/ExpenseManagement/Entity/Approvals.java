package com.example.demo.ExpenseManagement.Entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

public class Approvals {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approval_id")
    private Long approvalId;

    @Column(name = "approved_date")
    private ZonedDateTime approvedDate;

    @OneToOne
    @JoinColumn(name = "approved_by", referencedColumnName = "user_id")
    private Long approvedBy;

    @OneToOne
    @JoinColumn(name = "expense_id", referencedColumnName = "expense_id")
    private Long expense;

    public Approvals() {}

    public Approvals(Long approvalId, ZonedDateTime approvedDate, Long approvedBy, Long expense) {
        this.approvalId = approvalId;
        this.approvedDate = approvedDate;
        this.approvedBy = approvedBy;
        this.expense = expense;
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

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Long getExpenseId() {
        return expense;
    }

    public void setExpenseId(Long expense) {
        this.expense = expense;
    }
    
}

    // approval_id bigint unsigned not null auto_increment primary key,
    // approved_by bigint unsigned not null,
    // approved_date timestamp not null default current_timestamp,
    // expense_id bigint unsigned not null,
    // unique key unique_approval(approved_by, expense_id),
    // foreign key (expense_id) references expense(expense_id),
    // foreign key (approved_by) references user(user_id)