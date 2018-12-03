package com.hany.el_bazaar.Model;

/**
 * Created by Hany on 11/24/2018.
 */

public class Vendor {

    private String vendorName,brandName;
    public Vendor(String vendorName,String brandName){
        this.vendorName = vendorName;
        this.brandName =brandName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getBrandName() {
        return brandName;
    }
}
