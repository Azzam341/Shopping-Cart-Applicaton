package com.example.testingwithfx;

import java.util.ArrayList;


class Product
{
    private String productName;
    private double productPrice;
    private int productStock;
    private int productQuantity;
    private Category category;
    private String productDescription;
    private String imageFilepath;
    public ArrayList<Review> review = new ArrayList<Review>();

    public String getImageFilepath() {
        return imageFilepath;
    }

    public void setImageFilepath(String imageFilepath) {
        this.imageFilepath = imageFilepath;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public  ArrayList<Review> getReview() {
        return review;
    }

    public  void setReview(ArrayList<Review> review) {
        review = review;
    }

    public Product(String productName, double productPrice, int productStock, Category category, String productDescription, String imageFilepath) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.category = category;
        this.productDescription = productDescription;
        this.imageFilepath = imageFilepath;

    }
    public Product(String productName, double productPrice, int productQuantity, Category category,String productDescription, ArrayList<Review> review, String imageFilepath) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.category = category;
        this.productDescription = productDescription;
        this.review = review;
        this.imageFilepath = imageFilepath;
    }

    Product()
    {}
}





