package com.hany.el_bazaar.Model;

/**
 * Created by Hany on 11/24/2018.
 */

public class Vendor {

    private String vendorName, brandName,vendorId;
    public long vendorRate;

    public Vendor() {

    }

    public Vendor(String vendorName, String brandName) {
        this.vendorName = vendorName;
        this.brandName = brandName;
    }

    public long getVendorRate() {
        return vendorRate;
    }

    public void setVendorRate(long vendorRate) {
        this.vendorRate = vendorRate;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
