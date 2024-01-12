package com.example.demo.ExpenseManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.ExpenseManagement.Entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

    @Query("Select a from Address a where a.addressId = :addressId")
    Address findAddressById(@Param("addressId") Long addressId);
    
}
