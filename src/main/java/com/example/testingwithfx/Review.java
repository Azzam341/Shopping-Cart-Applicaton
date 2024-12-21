package com.example.testingwithfx;

public class Review
{
    //Attributes
    private String reviewerName;
    private String reviewText;
    private double reviewRating;


    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public double getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(double reviewRating) {
        this.reviewRating = reviewRating;
    }


    public Review(String reviewerName, double reviewRating, String reviewText) {
        this.reviewText = reviewText;
        this.reviewRating = reviewRating;
        this.reviewerName = reviewerName;
    }
}



