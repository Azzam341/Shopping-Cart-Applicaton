package com.example.testingwithfx;

import java.util.ArrayList;

public class Orders {

    private String productName;
    private int productQuantity;
    private double totalBill;
    private double priceAfterDiscount;
    ArrayList <Orders> orders = new ArrayList<>();

    public Orders(String productName, int productQuantity, double totalBill, double priceAfterDiscount) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.totalBill = totalBill;
        this.priceAfterDiscount = priceAfterDiscount;

    }


    public Orders() {
    }

    public String getProductName() {
        return productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public double getTotalBill() {
        return totalBill;
    }

}
