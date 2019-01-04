package com.hany.el_bazaar.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;
import java.util.Map;

/**
 * Created by Hany on 11/30/2018.
 */

@IgnoreExtraProperties
public class Product {


    public String productName, productPrice, productCurrency,productDesc,productId,favoriteEmail;
    public boolean isFavorite;
    public List<String> images;
    public Map<String,String> vendor;
    public  List<Map<String,String>> bazaars;
    public List<String> favoriteUsers;
    public long productRate;

    public Product() {

    }

    public Product(String productName, String productPrice, String productCurrency,String productDesc,List<String> images,Map<String,String> vendor,boolean isFavorite,List<Map<String,String>> bazaars) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.isFavorite = isFavorite;
        this.vendor = vendor;
        this.productCurrency = productCurrency;
        this.productDesc = productDesc;
        this.images = images;
        this.bazaars = bazaars;
    }


    public List<String> getFavoriteUsers() {
        return favoriteUsers;
    }

    public void setFavoriteUsers(List<String> favoriteUsers) {
        this.favoriteUsers = favoriteUsers;
    }

    public List<Map<String, String>> getBazaars() {
        return bazaars;
    }

    public void setBazaars(List<Map<String, String>> bazaars) {
        this.bazaars = bazaars;
    }

    public String getFavoriteEmail() {
        return favoriteEmail;
    }

    public void setFavoriteEmail(String favoriteEmail) {
        this.favoriteEmail = favoriteEmail;
    }

    public long getProductRate() {
        return productRate;
    }

    public void setProductRate(long productRate) {
        this.productRate = productRate;
    }

    public Map<String, String> getVendor() {
        return vendor;
    }

    public void setVendor(Map<String, String> vendor) {
        this.vendor = vendor;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductCurrency(String productCurrency) {
        this.productCurrency = productCurrency;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public String getProductCurrency() {
        return productCurrency;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
