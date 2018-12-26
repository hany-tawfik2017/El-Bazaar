package com.hany.el_bazaar.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by Hany on 11/30/2018.
 */

@IgnoreExtraProperties
public class Product {


    public String productName, productPrice, productCurrency,productDesc,productId,vendorName;
    public boolean isFavorite;
    public List<String> images;

    public Product() {

    }

    public Product(String productName, String productPrice, String productCurrency,String productDesc,List<String> images ,boolean isFavorite) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.isFavorite = isFavorite;
        this.productCurrency = productCurrency;
        this.productDesc = productDesc;
        this.images = images;
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
