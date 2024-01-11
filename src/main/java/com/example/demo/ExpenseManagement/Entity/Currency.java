package com.example.demo.ExpenseManagement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Currency {
    
    @Id
    @Column(name = "currency_id")
    private int currencyId;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "currency_name")
    private String currencyName;

    public Currency() {}

    public Currency(int currencyId, String currencyCode, String currencyName) {
        this.currencyId = currencyId;
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

}

    // currency_id smallint unsigned not null auto_increment primary key,
    // currency_code varchar(3) not null,
    // currency_name varchar(30)