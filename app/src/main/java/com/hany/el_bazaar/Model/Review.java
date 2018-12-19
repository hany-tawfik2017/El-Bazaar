package com.hany.el_bazaar.Model;

/**
 * Created by Hany on 12/15/2018.
 */

public class Review {

    private String userName,reviewText,reviewDate;
    private float userRate;
    public Review(String userName,String reviewText,String reviewDate,float userRate){
        this.userName = userName;
        this.reviewDate = reviewDate;
        this.reviewText = reviewText;
        this.userRate = userRate;
    }

    public String getUserName() {
        return userName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public float getUserRate() {
        return userRate;
    }
}
