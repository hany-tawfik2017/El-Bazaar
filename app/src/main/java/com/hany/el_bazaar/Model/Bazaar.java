package com.hany.el_bazaar.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;
import java.util.Map;

/**
 * Created by Hany on 12/1/2018.
 */
@IgnoreExtraProperties
public class Bazaar {

    public String bazaarName, organizerName, bazaarPlace,bazaarDesc,bazaarId;
    public String vendorNumbers;
    public List<String> images;
    public List<Map<String,String>> vendors;
    public long bazaarRate;

    public Bazaar(){

    }
    public Bazaar(String bazaarName, String organizerName, String bazaarPlace,String bazaarDesc, String vendorNumbers,List<String> images,List<Map<String,String>> vendors) {
        this.bazaarName = bazaarName;
        this.organizerName = organizerName;
        this.bazaarPlace = bazaarPlace;
        this.bazaarDesc = bazaarDesc;
        this.vendorNumbers = vendorNumbers;
        this.images = images;
        this.vendors = vendors;
    }

    public List<Map<String, String>> getVendors() {
        return vendors;
    }

    public void setVendors(List<Map<String, String>> vendors) {
        this.vendors = vendors;
    }

    public long getBazaarRate() {
        return bazaarRate;
    }

    public void setBazaarRate(long bazaarRate) {
        this.bazaarRate = bazaarRate;
    }

    public String getBazaarId() {
        return bazaarId;
    }

    public void setBazaarId(String bazaarId) {
        this.bazaarId = bazaarId;
    }

    public void setBazaarName(String bazaarName) {
        this.bazaarName = bazaarName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public void setBazaarPlace(String bazaarPlace) {
        this.bazaarPlace = bazaarPlace;
    }

    public void setVendorNumbers(String vendorNumbers) {
        this.vendorNumbers = vendorNumbers;
    }

    public String getBazaarDesc() {
        return bazaarDesc;
    }

    public void setBazaarDesc(String bazaarDesc) {
        this.bazaarDesc = bazaarDesc;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getBazaarName() {
        return bazaarName;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public String getBazaarPlace() {
        return bazaarPlace;
    }

    public String getVendorNumbers() {
        return vendorNumbers;
    }
}
