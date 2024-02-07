package com.example.demo.ExpenseManagement.DTO;

public class RoleUpdate {
    
    private Long userId;

    private Long roleId;

    public RoleUpdate() {}

    public RoleUpdate(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
}
