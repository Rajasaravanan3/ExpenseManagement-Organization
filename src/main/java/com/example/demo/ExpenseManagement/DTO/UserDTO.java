package com.example.demo.ExpenseManagement.DTO;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.ExpenseManagement.Entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class UserDTO implements UserDetails {
    
    private Long userId;

    private String fullName;

    private String username;

    private String password;

    private Boolean isActive;

    private Long organizationId;

    @JsonIgnoreProperties("users")
    private Set<Role> roles;

    public UserDTO() {}

    public UserDTO(Long userId, String fullName, String userEmail, String password, Boolean isActive, Set<Role> roles,
            Long organizationId) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = userEmail;
        this.password = password;
        this.isActive = isActive;
        this.roles = roles;
        this.organizationId = organizationId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : this.roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String passwordassword) {
        this.password = passwordassword;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    

}
