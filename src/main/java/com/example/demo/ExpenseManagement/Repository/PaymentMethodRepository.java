package com.example.demo.ExpenseManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.ExpenseManagement.Entity.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    
    @Query("Select p from PaymentMethod p where p.paymentMethodId = :paymentMethodId")
    PaymentMethod findPaymentMethodById(@Param("paymentMethodId") Integer paymentMethodId);

}
