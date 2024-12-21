package com.example.testingwithfx;

public enum AdminRank {
    RANK_1(15000),
    RANK_2(45000),
    RANK_3(75000);

    private double adminSalary;

    AdminRank(double adminSalary) {
        this.adminSalary = adminSalary;
    }

    public double getAdminSalary() {
        return adminSalary;
    }
}

