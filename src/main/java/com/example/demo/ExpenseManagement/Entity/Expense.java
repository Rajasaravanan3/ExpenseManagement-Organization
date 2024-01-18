package com.example.demo.ExpenseManagement.Entity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Expense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long expenseId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "expense_date")
    private ZonedDateTime expenseDate;

    @Column(name = "expense_description")
    private String expenseDescription;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @OneToOne
    @JoinColumn(name = "currency_id", referencedColumnName = "currency_id")
    private Currency currency;

    @OneToOne
    @JoinColumn(name = "payment_method_id", referencedColumnName = "payment_method_id")
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "expense_made_by", referencedColumnName = "user_id")
    private User user;

    public Expense() {}

    public Expense(Long expenseId, BigDecimal amount, ZonedDateTime expenseDate, String expenseDescription,
            Category category, Currency currency, PaymentMethod paymentMethod, User user) {
        this.expenseId = expenseId;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.expenseDescription = expenseDescription;
        this.category = category;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.user = user;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}

    // expense_id bigint unsigned not null auto_increment primary key,
    // amount Decimal(10,2) not null,
    // expense_date timestamp not null default current_timestamp,
    // expense_description varchar(1000) not null,
    // category_id bigint unsigned not null,
    // currency_id int unsigned not null,
    // payment_method_id int unsigned not null,
    // expense_made_by bigint unsigned not null,
    // foreign key (category_id) references category(category_id),
    // foreign key (currency_id) references currency(currency_id),
    // foreign key (payment_method_id) references payment_method(payment_method_id),
    // foreign key (expense_made_by) references user(user_id)