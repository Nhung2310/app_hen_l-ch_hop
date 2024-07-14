package com.example.doanthuctap;

public class Employee {
    private String fullName;
    private String email;

    public Employee(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
}
