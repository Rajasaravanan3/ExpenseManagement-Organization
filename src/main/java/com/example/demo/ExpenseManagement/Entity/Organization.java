package com.example.demo.ExpenseManagement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Organization {
    
    @Id
    @Column(name = "organization_id")
    private long organizationId;

    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "organization_number")
    private String organizationNumber;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    private Address address;

    public Organization() {}

    public Organization(long organizationId, String organizationName, String organizationNumber, boolean isActive,
            Address address) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.organizationNumber = organizationNumber;
        this.isActive = isActive;
        this.address = address;
    }

    public long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationNumber() {
        return organizationNumber;
    }

    public void setOrganizationNumber(String organizationNumber) {
        this.organizationNumber = organizationNumber;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}

    // organization_id bigint unsigned not null auto_increment primary key,
	// organization_name varchar(40) not null,
    // organization_number varchar(20) unique not null,
    // address_id bigint unsigned not null,
    // is_active boolean default true,
    // foreign key (address_id) references address(address_id)