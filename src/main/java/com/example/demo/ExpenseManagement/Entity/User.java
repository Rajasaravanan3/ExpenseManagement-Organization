package com.example.demo.ExpenseManagement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class User {
    
    @Id
    @Column(name = "user_id")
    private long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;

    @OneToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "organization_id")
    private Organization organization;

    public User() {}

    public User(long userId, String userName, String userEmail, String userPassword, boolean isActive, Role role,
            Organization organization) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.isActive = isActive;
        this.role = role;
        this.organization = organization;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    // user_id bigint unsigned not null auto_increment primary key,
    // user_name varchar(50) not null,
    // role_id bigint unsigned not null,
    // user_email varchar(350) unique not null,
    // user_password varchar(130) not null,
    // is_active boolean default true,
    // organization_id bigint unsigned not null,
    // foreign key (organization_id) references organization(organization_id)
}
