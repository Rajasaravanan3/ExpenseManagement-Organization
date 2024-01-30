package com.example.demo.ExpenseManagement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PaymentMethod {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_method_id")
    private Integer paymentMethodId;

    @Column(name = "payment_method_name")
    private String paymentMethodName;

    @Column(name = "payment_method_description")
    private String paymentMethodDescription;

    @Column(name = "is_active")
    private Boolean isActive;

    public PaymentMethod() {}

    public PaymentMethod(Integer paymentMethodId, String paymentMethodName, String paymentMethodDescription) {
        this.paymentMethodId = paymentMethodId;
        this.paymentMethodName = paymentMethodName;
        this.paymentMethodDescription = paymentMethodDescription;
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getPaymentMethodDescription() {
        return paymentMethodDescription;
    }

    public void setPaymentMethodDescription(String paymentMethodDescription) {
        this.paymentMethodDescription = paymentMethodDescription;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
}
