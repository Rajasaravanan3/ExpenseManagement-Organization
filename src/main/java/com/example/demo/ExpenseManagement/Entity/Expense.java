// package com.example.demo.ExpenseManagement.Entity;

// import java.math.BigDecimal;
// import java.time.ZonedDateTime;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;

// @Entity
// public class Expense {
    
//     @Id
//     @Column(name = "expense_id")
//     private long expenseId;

//     @Column(name = "amount")
//     private BigDecimal amount;

//     @Column(name = "expense_date")
//     private ZonedDateTime expenseDate;

//     @Column(name = "expense_description")
//     private String expenseDescription;

//     @JoinColumn(name = "category_id", referencedColumnName = "category_id")
//     private Category category;

//     @JoinColumn(name = "currency_id", referencedColumnName = "currency_id")
//     private Currency currency;

//     @JoinColumn(name = "payment_method_id", referencedColumnName = "payment_method_id")
//     private PaymentMethod paymentMethod;

//     @JoinColumn(name = "expense_made_by", referencedColumnName = "user_id")
//     private User user;
// }

//     // expense_id bigint unsigned not null auto_increment primary key,
//     // amount Decimal(10,2) not null,
//     // expense_date timestamp not null default current_timestamp,
//     // expense_description varchar(1000) not null,
//     // category_id bigint unsigned not null,
//     // currency_id int unsigned not null,
//     // payment_method_id int unsigned not null,
//     // expense_made_by bigint unsigned not null,
//     // foreign key (category_id) references category(category_id),
//     // foreign key (currency_id) references currency(currency_id),
//     // foreign key (payment_method_id) references payment_method(payment_method_id),
//     // foreign key (expense_made_by) references user(user_id)