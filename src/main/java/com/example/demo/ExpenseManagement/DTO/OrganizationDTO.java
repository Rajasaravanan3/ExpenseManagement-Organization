package com.example.demo.ExpenseManagement.DTO;

public class OrganizationDTO {
    
    private Long organizationId;

    private String organizationName;

    private String organizationNumber;

    private Boolean isActive;

    private Long addressId;

    public OrganizationDTO() {}

    public OrganizationDTO(Long organizationId, String organizationName, String organizationNumber, Boolean isActive,
            Long addressId) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.organizationNumber = organizationNumber;
        this.isActive = isActive;
        this.addressId = addressId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
    
}
