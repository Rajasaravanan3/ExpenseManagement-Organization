package com.example.demo.ExpenseManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.ExpenseManagement.Entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
    
    @Query("Select c from Currency c where c.currencyId = :currencyId")
    Currency findCurrencyById(@Param("currencyId") Integer currencyId);
}
