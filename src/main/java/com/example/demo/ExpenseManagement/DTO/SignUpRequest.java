package com.example.demo.ExpenseManagement.DTO;

public class SignUpRequest {
    
    private String fullName;

    private String username;

    private String password;

    private Long organizationId;

    public SignUpRequest() {}

    public SignUpRequest(String fullName, String username, String password, Long organizationId) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.organizationId = organizationId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}