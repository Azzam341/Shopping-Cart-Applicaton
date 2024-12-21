package com.example.testingwithfx;

import java.util.ArrayList;

public abstract class User {
    private String name;
    private String email;
    private String userID;
    private String userPassword;
    private Address address;
    static private ArrayList<Product> inventory = new ArrayList<Product>(100);
    private ArrayList<Product> shoppingCart = new ArrayList<Product>(100);


    public ArrayList<Product> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ArrayList<Product> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public static ArrayList<Product> getInventory() {
        return inventory;
    }

    public static void setInventory(ArrayList<Product> inventory) {
        User.inventory = inventory;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public Address getAddress() {
        return address;
    }

    public User() {
    }

    public User(String name, String email, String userID, String userPassword, Address address) {
        this.name = name;
        this.email = email;
        this.userID = userID;
        this.userPassword = userPassword;
        this.address = address;
    }

    abstract User signUp(String name, String email, String userID, String userPassword, String address, String town, String city);

    abstract boolean signIn(String userID, String userPassword, String a, String b);
    abstract boolean updatePassword(String username,String privateAnswer,String Password);
    abstract boolean savePrivateQusetion(String username,String question,String answer);
    abstract boolean checkIfPrivateQuestionExists(String username);

}