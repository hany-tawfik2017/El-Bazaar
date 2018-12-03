package com.hany.el_bazaar.Model;

/**
 * Created by Hany on 11/30/2018.
 */

public class Product {

    private String productName,productPrice;
    private boolean isFavorite;

    public Product(String productName,String productPrice,boolean isFavorite){
        this.productName = productName;
        this.productPrice = productPrice;
        this.isFavorite = isFavorite;
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
}
