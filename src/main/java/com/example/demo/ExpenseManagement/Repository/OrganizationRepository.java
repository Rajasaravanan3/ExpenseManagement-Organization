package com.example.demo.ExpenseManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.ExpenseManagement.Entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long>{

    @Query("Select o from Organization o where o.organizationId = :organizationId")
    Organization findOrganizationById(@Param("organizationId") Long organizationId);

}
