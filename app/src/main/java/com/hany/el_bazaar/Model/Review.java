package com.hany.el_bazaar.Model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Hany on 12/15/2018.
 */
@IgnoreExtraProperties
public class Review {

    public String userName,reviewText,reviewDate,productName,bazaarName,organizerName,vendorName;
    public long userRate;
    public Review(String userName, String reviewText, String reviewDate, long userRate, String productName, String bazaarName, String organizerName, String vendorName){
        this.userName = userName;
        this.reviewDate = reviewDate;
        this.reviewText = reviewText;
        this.userRate = userRate;
        this.productName = productName;
        this.bazaarName = bazaarName;
        this.organizerName = organizerName;
        this.vendorName = vendorName;
    }

    public Review() {

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setUserRate(long userRate) {
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

    public long getUserRate() {
        return userRate;
    }
}
