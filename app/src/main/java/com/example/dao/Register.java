package com.example.dao;

public class Register {
    private String userName;
    private String password;
    private String roleId;

    public void setRole(String roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return roleId;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
