package com.dpycb.smartwaiter.model;

public class Table {
    private String Name;
    private String Password;
    private String IsStaff;

    public Table() {
    }

    public Table(String name, String password) {
        Name = name;
        Password = password;
        IsStaff = "false";
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
}
